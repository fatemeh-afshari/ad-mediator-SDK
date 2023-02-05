package com.test.data.data_source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "tbl_waterfall")
data class Waterfall(

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String = "",

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NotNull
    var name: String,

    @ColumnInfo(name = "timestamp")
    @Expose
    var savedTimestamp: Long = 0,

    @ColumnInfo(name = "expireTimestamp")
    @Expose
    var expireTimestamp: Long = 0
)