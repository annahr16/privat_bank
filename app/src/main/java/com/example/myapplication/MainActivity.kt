package com.example.myapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import android.widget.LinearLayout


class MainActivity : AppCompatActivity() {

    private val viewModel: ExchangeViewModel by viewModels()

    private lateinit var currencyAdapter: CurrencyAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var loadButton: Button
    private lateinit var pickDateButton: Button
    private lateinit var selectedDateText: TextView

    private var selectedDateString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view_rates)
        progressBar = findViewById(R.id.progress_bar)
        loadButton = findViewById(R.id.button_load)
        pickDateButton = findViewById(R.id.button_pick_date)
        selectedDateText = findViewById(R.id.tv_selected_date)

        setupRecyclerView()

        // приховуємо рядок заголовків спочатку
        findViewById<LinearLayout>(R.id.header_row).visibility = View.GONE

        pickDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        loadButton.setOnClickListener {
            if (selectedDateString.isEmpty()) {
                Toast.makeText(this, "Будь ласка, оберіть дату", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.fetchRates(selectedDateString)
            }
        }

        viewModel.ratesState.observe(this) { state ->
            when (state) {
                is RatesState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    loadButton.isEnabled = false
                    pickDateButton.isEnabled = false
                }
                is RatesState.Success -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    loadButton.isEnabled = true
                    pickDateButton.isEnabled = true

                    if (state.rates.isEmpty()) {
                        Toast.makeText(this, "Даних за цю дату немає", Toast.LENGTH_LONG).show()
                        // При відсутності даних рядок заголовків не показуємо
                        findViewById<LinearLayout>(R.id.header_row).visibility = View.GONE
                    }

                    currencyAdapter.submitList(state.rates)
                    // Показуємо заголовки тільки якщо дані успішно завантажено
                    findViewById<LinearLayout>(R.id.header_row).visibility = View.VISIBLE
                }
                is RatesState.Error -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    loadButton.isEnabled = true
                    pickDateButton.isEnabled = true
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                val dayStr = selectedDayOfMonth.toString().padStart(2, '0')
                val monthStr = (selectedMonth + 1).toString().padStart(2, '0')

                selectedDateString = "$dayStr.$monthStr.$selectedYear"

                selectedDateText.text = selectedDateString

            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}