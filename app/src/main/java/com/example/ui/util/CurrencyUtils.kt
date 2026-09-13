package com.example.ui.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyUtils {
    private val indonesianSymbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
        groupingSeparator = '.'
        monetaryDecimalSeparator = ','
    }

    private val formatter = DecimalFormat("#,###", indonesianSymbols)

    fun formatRupiah(amount: Long): String {
        return "Rp " + formatter.format(amount)
    }

    fun formatNumber(amount: Long): String {
        return formatter.format(amount)
    }
}
