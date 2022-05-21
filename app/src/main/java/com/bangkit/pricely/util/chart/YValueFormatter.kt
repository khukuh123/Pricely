package com.bangkit.pricely.util.chart

import android.content.Context
import com.bangkit.pricely.util.localeId
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class YValueFormatter(private val context: Context) : ValueFormatter() {

    override fun getPointLabel(entry: Entry?): String {
        return yValueFormat.format((entry?.y ?: 0f))
    }

    companion object{
        private val numberSymbol = DecimalFormatSymbols(localeId).apply {
            groupingSeparator = ' '
        }
        val yValueFormat = DecimalFormat("###,###", numberSymbol)
    }
}