package com.test.admediator

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.test.data.repository.Repository
import com.test.data.repository.RepositoryImpl
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        MainScope().launch(Dispatchers.IO){
            repository.initializeAdNetworks(this@App).collect()
        }
    }

}
