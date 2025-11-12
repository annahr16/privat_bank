package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Клас для опису стану екрану (завантаження, успіх, помилка)
// Це набагато краще, ніж керувати progressBar.visibility вручну
sealed class RatesState {
    object Loading : RatesState() // Стан "завантаження"
    data class Success(val rates: List<CurrencyRate>) : RatesState() // Стан "успіх"
    data class Error(val message: String) : RatesState() // Стан "помилка"
}

class ExchangeViewModel : ViewModel() {

    // Приватна LiveData зі станом
    private val _ratesState = MutableLiveData<RatesState>()
    // Публічна LiveData, на яку підпишеться Activity
    val ratesState: LiveData<RatesState> = _ratesState

    // Функція, яку буде викликати Activity
    fun fetchRates(date: String) {

        // 1. Одразу показуємо стан "Завантаження"
        _ratesState.value = RatesState.Loading

        // 2. Запускаємо корутину в viewModelScope.
        // Цей потік НЕ буде знищено при перевороті екрану.
        viewModelScope.launch {
            try {
                // 3. Робимо безпечний запит в мережу
                val response = ApiClient.service.getExchangeRates(date)

                // 4. Публікуємо успішний результат
                _ratesState.value = RatesState.Success(response.exchangeRate)

            } catch (e: Exception) {
                // 5. Якщо помилка (немає інтернету і т.д.) - публікуємо помилку
                _ratesState.value = RatesState.Error(e.message ?: "Unknown error")
            }
        }
    }
}