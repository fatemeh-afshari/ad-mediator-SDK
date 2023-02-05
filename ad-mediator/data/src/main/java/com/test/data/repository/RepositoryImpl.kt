package com.test.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.test.data.data_source.local.dao.WaterfallDao
import com.test.data.data_source.local.entity.Waterfall
import com.test.data.data_source.mapper.map
import com.test.data.data_source.remote.api.Api
import com.test.data.data_source.remote.callback.GeneralAdRequestCallback
import com.test.data.data_source.remote.dto.AdRequestDto
import com.test.data.data_source.remote.state.AdRequestResultState
import com.test.data.data_source.remote.state.AdShowState
import com.test.data.data_source.remote.state.ResultState
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.tapsell.sdk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val waterfallDao: WaterfallDao,
    private val api: Api,
) : Repository {
    override fun initializeAdNetworks(app: Application): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val response = api.getAdNetworks()
            if (response.isSuccessful) {
                response.body()?.let {
                    it.adNetworks.forEach { item ->
                        if (item.name == "UnityAds") {
                            initializeUnityAds(token = item.id, app = app)
                        }
                        if (item.name == "Tapsell") {
                            initializeTapsell(token = item.id, app = app)
                        }
                    }
                }
                emit(
                    ResultState.Success(
                        data = "initialized successfully",
                    )
                )
            } else {
                emit(ResultState.Error("server failure"))
            }
        } catch (ex: Throwable) {
            when (ex) {
                is IOException -> {
                    emit(ResultState.ConnectionFailure("check your connection"))
                }
                else -> emit(ResultState.Error(ex.message.toString()))
            }
        }

    }.flowOn(Dispatchers.IO)

    override fun requestAd(): Flow<AdRequestResultState<AdRequestDto>> =
        callbackFlow {
            val callback = object : GeneralAdRequestCallback {
                override fun onAvailable(result: AdRequestDto) {
                    trySend(AdRequestResultState.adIsReady(result))
                    channel.close()
                }

                override fun onFailure(error: String) {
                    trySend(AdRequestResultState.adFailed(error))
                }
            }
            trySend(AdRequestResultState.Loading)
            try {
                val waterfalls = getAvailableWaterfalls()
                waterfalls.map {
                    async {
                        if (it.name == "Tapsell") {
                            requestTapsellAd(it.id, callback)
                        }
                        if (it.name == "UnityAds") {
                            requestUnityAd(it.id, callback)

                        }
                    }
                }.awaitAll()
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                        trySend(AdRequestResultState.adFailed("check your connection"))
                    }
                    else -> trySend(AdRequestResultState.adFailed(e.message.toString()))
                }
                channel.close()
            }
            awaitClose { }
        }.flowOn(Dispatchers.IO)


    private fun initializeUnityAds(token: String, app: Application) {
        UnityAds.initialize(
            app,
            token,
            true,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    Log.i("initializeUnityAds", "onInitializationComplete")
                }

                override fun onInitializationFailed(
                    p0: UnityAds.UnityAdsInitializationError?,
                    p1: String?,
                ) {
                    throw (Exception( "unity ad initialization exception: ${p0.toString()}"))
                }

            })
    }

    private fun initializeTapsell(token: String, app: Application) {
        Tapsell.initialize(
            app,
            token
        )
    }

    private fun requestTapsellAd(waterfallId: String, callback: GeneralAdRequestCallback) {
        val tapsellCallback =
            object : TapsellAdRequestListener() {
                override fun onAdAvailable(adId: String) {
                    val model = AdRequestDto(
                        "Tapsell",
                        adId,
                        waterfallId
                    )
                    callback.onAvailable(model)
                }

                override fun onError(message: String) {
                    callback.onFailure(message)
                }
            }

        Tapsell.requestAd(
            context,
            waterfallId,
            TapsellAdRequestOptions(),
            tapsellCallback
        )
    }

    private fun requestUnityAd(waterfallId: String, callback: GeneralAdRequestCallback) {
        val unityCallback = object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(adId: String?) {
                val model = AdRequestDto(
                    "UnityAds",
                    adId,
                    waterfallId
                )
                callback.onAvailable(model)
            }

            override fun onUnityAdsFailedToLoad(
                message: String?,
                p1: UnityAds.UnityAdsLoadError?,
                p2: String?,
            ) {
                callback.onFailure(message ?: "unity ad request in unavailable")
            }

        }

        if (UnityAds.isInitialized())
            UnityAds.load(waterfallId, unityCallback)
        else
            callback.onFailure("Unity ad not initialized")
    }

    private suspend fun getAvailableWaterfalls(): List<Waterfall> {

        val availableWaterfalls = waterfallDao.getAllAvailable(System.currentTimeMillis())
        if (availableWaterfalls.isNotEmpty()) {
            return availableWaterfalls
        }
        val response = api.getWaterfalls()
        if (response.isSuccessful) {
            response.body()?.let {
                waterfallDao.deleteAll()
                waterfallDao.insert(it.list.map { item -> item.map() })
            }
            val waterfalls = waterfallDao.getAllAvailable(System.currentTimeMillis())
            return waterfalls
        } else {
            throw Exception( "server failure")
        }
    }

    override fun showAd(ad: AdRequestDto): Flow<AdShowState> = callbackFlow {
        trySend(AdShowState.Loading)
        try {
            if (ad.waterfallName == "Tapsell") {
                Tapsell.showAd(context,
                    ad.zoneId,
                    ad.adId,
                    TapsellShowOptions(),
                    object : TapsellAdShowListener() {
                        override fun onOpened() {
                            trySend(AdShowState.Started)
                        }

                        override fun onError(message: String) {
                            trySend(AdShowState.Failed)
                            channel.close()
                        }

                        override fun onRewarded(completed: Boolean) {
                            trySend(AdShowState.Completed)
                            channel.close()
                        }
                    })
            }
            if (ad.waterfallName == "UnityAds") {
                UnityAds.show(
                    context as ComponentActivity,
                    ad.adId,
                    object : IUnityAdsShowListener {
                        override fun onUnityAdsShowFailure(
                            p0: String?,
                            p1: UnityAds.UnityAdsShowError?,
                            p2: String?,
                        ) {
                            trySend(AdShowState.Failed)
                            channel.close()
                        }

                        override fun onUnityAdsShowStart(p0: String?) {
                            trySend(AdShowState.Started)
                        }

                        override fun onUnityAdsShowClick(p0: String?) {
                            TODO("Not yet implemented")
                        }

                        override fun onUnityAdsShowComplete(
                            p0: String?,
                            p1: UnityAds.UnityAdsShowCompletionState?,
                        ) {
                            trySend(AdShowState.Completed)
                            channel.close()
                        }

                    }

                )
            }
        } catch (e: Exception) {
            channel.close()
        }
        awaitClose {}

    }
}


