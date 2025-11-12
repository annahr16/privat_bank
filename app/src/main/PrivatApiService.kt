package com.example.myapplication
import retrofit2.http.GET
import retrofit2.http.Query

interface PrivatApiService {

    // GET https://api.privatbank.ua/p24api/exchange_rates?json&date=01.12.2014
    @GET("p24api/exchange_rates?json") // ?json - обов'язковий параметр
    suspend fun getExchangeRates(
        @Query("date") date: String // Динамічний параметр
    ): ExchangeRateResponse
}