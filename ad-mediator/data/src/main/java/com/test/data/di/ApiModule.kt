package com.test.data.di

import com.test.data.data_source.remote.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthFreeApiService( retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}
