package com.test.data.data_source.local.data_base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.data.data_source.local.dao.WaterfallDao
import com.test.data.data_source.local.entity.Waterfall


@Database(entities = [Waterfall::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterfallDao(): WaterfallDao
}