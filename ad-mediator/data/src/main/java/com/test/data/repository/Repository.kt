package com.test.data.repository

import android.app.Application
import com.test.data.data_source.local.entity.Waterfall
import com.test.data.data_source.remote.dto.AdRequestDto
import com.test.data.data_source.remote.state.AdRequestResultState
import com.test.data.data_source.remote.state.AdShowState
import com.test.data.data_source.remote.state.ResultState
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun initializeAdNetworks(app: Application): Flow<ResultState<String>>
    fun requestAd(): Flow<AdRequestResultState<AdRequestDto>>
    fun showAd(ad: AdRequestDto): Flow<AdShowState>
}