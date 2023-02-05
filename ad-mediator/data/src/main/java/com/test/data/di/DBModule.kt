package com.test.data.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.test.core.util.Constants.BASE_URL
import com.test.data.BuildConfig
import com.test.data.data_source.local.dao.WaterfallDao
import com.test.data.data_source.local.data_base.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    fun provideWaterfallDao(appDatabase: AppDatabase): WaterfallDao {
        return appDatabase.waterfallDao()
    }
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}
