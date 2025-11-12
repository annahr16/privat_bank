package com.example.myapplication // <-- Правильний пакет

import retrofit2.http.GET
import retrofit2.http.Query

interface PrivatApiService {
    @GET("p24api/exchange_rates?json")
    suspend fun getExchangeRates(
        @Query("date") date: String
    ): ExchangeRateResponse
}