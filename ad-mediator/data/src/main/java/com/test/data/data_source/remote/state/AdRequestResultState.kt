package com.test.data.data_source.remote.state

import com.test.data.data_source.remote.dto.AdRequestDto


sealed class AdRequestResultState<out T> {
    object Loading : AdRequestResultState<Nothing>()
    data class adIsReady(val data: AdRequestDto?) : AdRequestResultState<AdRequestDto>()
    data class adFailed(val error: String) : AdRequestResultState<Nothing>()
}
