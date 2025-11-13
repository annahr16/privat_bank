package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

// R.layout.item_currency - це XML, який ти створиш в 'res/layout'
// Він описує, як виглядає ОДИН рядок списку
class CurrencyAdapter : ListAdapter<CurrencyRate, CurrencyAdapter.CurrencyViewHolder>(CurrencyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false) // !!! Треба створити цей layout
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
        private val currencySale: TextView = itemView.findViewById(R.id.tv_currency_sale)
        private val currencyPurchase: TextView = itemView.findViewById(R.id.tv_currency_purchase)

        fun bind(rate: CurrencyRate) {
            currencyName.text = rate.currency
            currencySale.text = "Продаж: ${rate.saleRate ?: "N/A"}"
            currencyPurchase.text = "Купівля: ${rate.purchaseRate ?: "N/A"}"
        }
    }
}

// Це допоміжний клас, щоб RecyclerView знав, що змінилося
class CurrencyDiffCallback : DiffUtil.ItemCallback<CurrencyRate>() {
    override fun areItemsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem.currency == newItem.currency
    }

    override fun areContentsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem == newItem
    }
}