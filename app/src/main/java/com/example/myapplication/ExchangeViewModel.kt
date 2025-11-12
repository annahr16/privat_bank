package com.example.myapplication // <-- Правильний пакет

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Ми покладемо цей 'sealed class' прямо тут
sealed class RatesState {
    object Loading : RatesState()
    data class Success(val rates: List<CurrencyRate>) : RatesState()
    data class Error(val message: String) : RatesState()
}

class ExchangeViewModel : ViewModel() {

    private val _ratesState = MutableLiveData<RatesState>()
    val ratesState: LiveData<RatesState> = _ratesState

    fun fetchRates(date: String) {
        _ratesState.value = RatesState.Loading
        viewModelScope.launch {
            try {
                val response = ApiClient.service.getExchangeRates(date)
                _ratesState.value = RatesState.Success(response.exchangeRate)
            } catch (e: Exception) {
                _ratesState.value = RatesState.Error(e.message ?: "Unknown error")
            }
        }
    }
}