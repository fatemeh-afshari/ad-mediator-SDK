package com.test.data.data_source.remote.dto

import com.squareup.moshi.Json


sealed class WaterfallDto {
    data class Result(
        @field:Json(name = "type") var id: String,
        @field:Json(name = "waterfall") var list: List<Item>,
    ) : WaterfallDto()


    data class Item(
        @field:Json(name = "id") var id: String,
        @field:Json(name = "name") var name: String,
    ) : WaterfallDto()

}