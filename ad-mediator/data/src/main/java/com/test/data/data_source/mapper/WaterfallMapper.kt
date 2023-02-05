package com.test.data.data_source.mapper

import com.test.data.data_source.local.entity.Waterfall
import com.test.data.data_source.remote.dto.WaterfallDto
import java.util.*

@JvmName("mapDashboardDtoToDashboardEntity")
fun WaterfallDto.Item.map(): Waterfall {
    return Waterfall(
        id = this.id ,
        name =  this.name ,
        savedTimestamp = System.currentTimeMillis(),
        expireTimestamp = System.currentTimeMillis() + 3600000
    )
}
