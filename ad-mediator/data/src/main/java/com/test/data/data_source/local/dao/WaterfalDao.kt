package com.test.data.data_source.local.dao

import androidx.room.*
import com.test.data.data_source.local.entity.Waterfall

@Dao
interface WaterfallDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(waterfalls: List<Waterfall>)

    @Query("SELECT * FROM TBL_WATERFALL WHERE expireTimestamp >= :timeStamp")
    fun getAllAvailable(timeStamp: Long): List<Waterfall>


    @Query("DELETE FROM TBL_WATERFALL")
    fun deleteAll()

}