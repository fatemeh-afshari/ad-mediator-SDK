package com.test.data.data_source.remote.state

import com.test.data.data_source.remote.dto.AdRequestDto


sealed class AdShowState {
    object Loading : AdShowState()
    object Started : AdShowState()
    object Completed : AdShowState()
    object Failed : AdShowState()
}
