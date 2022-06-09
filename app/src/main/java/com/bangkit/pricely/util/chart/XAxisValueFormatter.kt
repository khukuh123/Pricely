package com.bangkit.pricely.util.chart

import android.content.Context
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class XAxisValueFormatter(private val context: Context, private val data: List<String>) : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return try {
            data[value.toInt()]
        } catch (e: Exception) {
            ""
        }
    }
}