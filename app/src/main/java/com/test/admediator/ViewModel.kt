package com.test.admediator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.data.data_source.remote.dto.AdRequestDto
import com.test.data.data_source.remote.state.AdRequestResultState
import com.test.data.data_source.remote.state.AdShowState
import com.test.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var isLoading: Boolean by mutableStateOf(false)

    fun requestAd() {
        viewModelScope.launch {
            repository.requestAd().collect { result ->
                when (result) {
                    is AdRequestResultState.Loading -> {
                        isLoading = true
                    }
                    is AdRequestResultState.adIsReady -> {
                        result.data?.let { showAd(it) }
                    }
                }

            }
        }
    }

    private fun showAd(ad: AdRequestDto) {
        viewModelScope.launch {
            repository.showAd(ad).collect { result ->
                when (result) {
                    AdShowState.Completed -> {}
                    AdShowState.Failed -> {}
                    AdShowState.Loading -> isLoading = true
                    AdShowState.Started -> isLoading = false
                }
            }
        }
    }
}