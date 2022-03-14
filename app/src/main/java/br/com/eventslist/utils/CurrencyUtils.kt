package br.com.eventslist.utils

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    @SuppressLint("NewApi")
    fun formatPrice(price: Double): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        numberFormat.maximumFractionDigits = 2;
        return numberFormat.format(price)
    }
}