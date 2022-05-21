package com.bangkit.pricely.presentation.detail

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityProductDetailBinding
import com.bangkit.pricely.domain.product.Product
import com.bangkit.pricely.util.*
import com.bangkit.pricely.util.chart.LeftAxisValueFormatter
import com.bangkit.pricely.util.chart.XAxisValueFormatter
import com.bangkit.pricely.util.chart.YValueFormatter
import com.bangkit.pricely.util.dialog.MonthYearPickerDialog
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>() {

    private val monthYearPickerDialog by lazy {
        MonthYearPickerDialog.newInstance(
            ArrayList(getMonths()),
            arrayListOf("2022", "2023")
        )
    }

    override fun getViewBinding(): ActivityProductDetailBinding =
        ActivityProductDetailBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupToolbar(
            binding.toolbar.toolbar,
            getString(R.string.title_product_detail),
            true
        )

        setupChart()
    }

    override fun setupAction() {
        binding.btnMontAndYearPicker.setOnClickListener {
            monthYearPickerDialog.show(supportFragmentManager)
        }
        monthYearPickerDialog.setOnMontAndYearPicked { month, year ->
            showToast("${month.first} and ${year.second}")
        }
    }

    override fun setupProcess() {
        setProductDetail(DummyData.product)
    }

    override fun setupObserver() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun setupDropDown() {
        with(binding){
            spnChartType.apply {
                setAdapter(
                    this@ProductDetailActivity,
                    getChartTypes(),
                    mapper = { it },
                    onItemClicked = { position, _ ->
                        if(position == 0){
                            setLineChart(getMonthlyPricesData(), getMonths())
                        }else{
                            val annualPricesData = getAnnualPricesData()
                            setLineChart(annualPricesData, annualPricesData.map { "%.0f".format(it.x + 2015) })
                        }
                    }
                )
                setText(getChartTypes().first(), false)
            }
        }
    }

    private fun setProductDetail(product: Product){
        with(binding){
//            imgProduct.setImageFromUrl()
            tvProductName.text = product.name
            val weightPerPiece = "${product.weight} ${product.unit}"
            tvProductWeightPerPiece.text = weightPerPiece
            tvProductPrice.text = product.price.formatCurrency()
            tvProductDescription.text = product.description
            imgIndicator.setImageResource(if(product.isRise) R.drawable.ic_indicator_up else R.drawable.ic_indicator_down)
            tvPriceInfo.text = getString(R.string.label_current_month)
            setLineChart(getMonthlyPricesData(), getMonths())

            setupDropDown()
        }
    }

    private fun setLineChart(data: List<Entry>, xAxisLabels: List<String>) {
        val historyDataSet = LineDataSet(data.subList(0, (data.size+1)/2), getString(R.string.label_history))
        val predictionDataSet = LineDataSet(data.subList((data.size+1)/2-1, data.size), getString(R.string.label_prediction))
        historyDataSet.apply {
            val mColor = ContextCompat.getColor(this@ProductDetailActivity, R.color.greenLeaf)
            color = mColor
            setCircleColor(mColor)
            lineWidth = 2f
        }
        predictionDataSet.apply {
            val mColor = ContextCompat.getColor(this@ProductDetailActivity, R.color.anotherGrey)
            color = mColor
            enableDashedLine(20f, 10f, 0f)
            setCircleColor(mColor)
            lineWidth = 2f
        }
        binding.crtPrices.apply {
            val lineData = LineData(historyDataSet, predictionDataSet).apply {
                setValueFormatter(YValueFormatter(this@ProductDetailActivity))
            }
            xAxis.valueFormatter = XAxisValueFormatter(this@ProductDetailActivity, xAxisLabels)
            setData(lineData)
            invalidate()
        }
    }

    private fun getMonthlyPricesData(): List<Entry>{
        val rand = Random(100)
        return Array(12){
            val price = rand.nextDouble(100.00, 235.00)
            val priceFloat = "${price.toInt()}00.0".toFloat()
            Entry(it.toFloat(), priceFloat)
        }.toList()
    }

    private fun getAnnualPricesData(): List<Entry>{
        val rand = Random(132)
        return Array(5){
            val price = rand.nextDouble(100.00, 235.00)
            val priceFloat = "${price.toInt()}00.0".toFloat()
            Entry(it.toFloat(), priceFloat)
        }.toList()
    }

    private fun getChartTypes(): List<String> {
        return listOf(
            "Month",
            "Year"
        )
    }

    private fun setupChart() {
        binding.crtPrices.apply {
            isHighlightPerDragEnabled = false
            isHighlightPerTapEnabled = false
            axisRight.setDrawLabels(false)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                spaceMin = 0.5f
                spaceMax = 0.5f
            }
            description.isEnabled = false
            axisLeft.valueFormatter = LeftAxisValueFormatter(this@ProductDetailActivity)
            isScaleYEnabled = false
            setExtraOffsets(0f, 0f, 0f,5f)
        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, ProductDetailActivity::class.java).apply {

            })
        }
    }
}