package com.bangkit.pricely.util.chart

import android.content.Context
import com.bangkit.pricely.util.formatLargeValue
import com.bangkit.pricely.util.getMonths
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.lang.Exception
import kotlin.math.roundToLong

class XAxisValueFormatter(private val context: Context, private val data: List<String>): ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return try {
            data[value.toInt()]
        }catch (e: Exception){
            ""
        }
    }
}