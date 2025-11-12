package com.example.myapplication

import com.google.gson.annotations.SerializedName

/**
 * Цей файл описує, як виглядають дані,
 * які ми отримуємо з Інтернету.
 */

// Описує всю відповідь (об'єкт, що містить список)
data class ExchangeRateResponse(
    @SerializedName("exchangeRate") val exchangeRate: List<CurrencyRate>
)

// Описує ОДИН елемент у списку валют
data class CurrencyRate(
    @SerializedName("currency") val currency: String,
    @SerializedName("saleRate") val saleRate: Double?, // Може бути null
    @SerializedName("purchaseRate") val purchaseRate: Double? // Може бути null
)