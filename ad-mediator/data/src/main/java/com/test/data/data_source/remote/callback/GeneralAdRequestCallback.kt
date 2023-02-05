package com.test.data.data_source.remote.callback

import com.test.data.data_source.remote.dto.AdRequestDto

interface GeneralAdRequestCallback {
    fun onAvailable(result: AdRequestDto)
    fun onFailure(error: String)
}