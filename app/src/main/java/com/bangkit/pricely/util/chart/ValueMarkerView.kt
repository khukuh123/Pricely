package com.bangkit.pricely.util.chart

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.bangkit.pricely.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class ValueMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {
    private var textView: TextView = findViewById(R.id.tvItemDropdown)
    private var mOffset: MPPointF? = null

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        textView.text = YValueFormatter.yValueFormat.format(e?.y ?: 0f)
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return mOffset ?: MPPointF(-(width.toFloat()/2), -height.toFloat())
    }
}