package com.bangkit.pricely.util.chart

import android.content.Context
import com.bangkit.pricely.util.formatLargeValue
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToLong

class LeftAxisValueFormatter(private val context: Context): ValueFormatter() {
    private val largeValueBase = arrayOf("", "k", "m", "b", "t")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return formatLargeValue(value.roundToLong(), base = largeValueBase)
    }
}