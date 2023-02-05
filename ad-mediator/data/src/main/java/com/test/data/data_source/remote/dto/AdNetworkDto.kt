package com.test.data.data_source.remote.dto

import com.squareup.moshi.Json


sealed class AdNetworkDto {
    data class AdList(
        @field:Json(name = "adNetworks") var adNetworks:List<AdNetwork>,
    ) : AdNetworkDto()


    data class AdNetwork(
        @field:Json(name = "id") var id: String,
        @field:Json(name = "name") var name: String,
    ) : AdNetworkDto()

}