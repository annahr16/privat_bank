package com.example.myapplication
import com.example.myapplication.ExchangeViewModel
import com.example.myapplication.CurrencyAdapter
import com.example.myapplication.RatesState

import com.example.myapplication.R

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // Отримуємо ViewModel. Вона переживе переворот екрану
    private val viewModel: ExchangeViewModel by viewModels()

    // Адаптер для списку
    private lateinit var currencyAdapter: CurrencyAdapter

    // Елементи UI
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var loadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ініціалізуємо UI елементи (їх ID мають бути в activity_main.xml)
        recyclerView = findViewById(R.id.recycler_view_rates)
        progressBar = findViewById(R.id.progress_bar)
        loadButton = findViewById(R.id.button_load)

        // Налаштовуємо RecyclerView
        setupRecyclerView()

        // 1. Встановлюємо слухач на кнопку
        loadButton.setOnClickListener {
            // TODO: Тут ти маєш брати дату з DatePicker або EditText
            val selectedDate = "01.12.2014" // Поки що "захардкодимо" для тесту
            viewModel.fetchRates(selectedDate)
        }

        // 2. ПІДПИСУЄМОСЬ на зміни стану у ViewModel
        // Це серце MVVM. Цей код автоматично реагує на зміни даних.
        viewModel.ratesState.observe(this) { state ->
            when (state) {
                is RatesState.Loading -> {
                    // Йде завантаження
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    loadButton.isEnabled = false // Блокуємо кнопку
                }
                is RatesState.Success -> {
                    // Успіх!
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    loadButton.isEnabled = true
                    // Оновлюємо дані в адаптері
                    currencyAdapter.submitList(state.rates)
                }
                is RatesState.Error -> {
                    // Помилка
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    loadButton.isEnabled = true
                    // Показуємо помилку
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        currencyAdapter = CurrencyAdapter()
        recyclerView.adapter = currencyAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}