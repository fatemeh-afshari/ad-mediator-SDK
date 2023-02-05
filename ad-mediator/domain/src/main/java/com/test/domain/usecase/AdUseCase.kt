package com.test.domain.usecase

interface AdUseCase {

    suspend fun initialize()
    suspend fun requestAd()
    suspend fun showAd()
}