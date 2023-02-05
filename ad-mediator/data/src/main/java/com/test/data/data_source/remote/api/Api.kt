package com.test.data.data_source.remote.api

import com.test.data.data_source.remote.dto.AdNetworkDto
import com.test.data.data_source.remote.dto.WaterfallDto
import retrofit2.Response
import retrofit2.http.GET


interface Api {
    @GET("adnets")
    suspend fun getAdNetworks(): Response<AdNetworkDto.AdList>

    @GET("waterfall")
    suspend fun getWaterfalls(): Response<WaterfallDto.Result>

}