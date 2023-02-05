package com.test.data.data_source.remote.state



sealed class ResultState<out R> {
    object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T?) : ResultState<T>()
    data class Error(val error: String) : ResultState<Nothing>()
    data class ConnectionFailure(val error: String) : ResultState<Nothing>()
}
