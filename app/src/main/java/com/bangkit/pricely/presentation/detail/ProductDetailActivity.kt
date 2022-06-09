package com.bangkit.pricely.presentation.detail

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityProductDetailBinding
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.price.model.PriceEntry
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.presentation.viewmodel.PriceViewModel
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.*
import com.bangkit.pricely.util.chart.LeftAxisValueFormatter
import com.bangkit.pricely.util.chart.XAxisValueFormatter
import com.bangkit.pricely.util.chart.YValueFormatter
import com.bangkit.pricely.util.dialog.DateSet
import com.bangkit.pricely.util.dialog.MonthYearPickerDialog
import com.bangkit.pricely.util.dialog.getErrorDialog
import com.bangkit.pricely.util.dialog.getLoadingDialog
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.random.Random

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>() {

    private val monthYearPickerDialog: MonthYearPickerDialog by lazy {
        MonthYearPickerDialog.newInstance()
    }
    private val productViewModel: ProductViewModel by inject()
    private val priceViewModel: PriceViewModel by inject()

    private var productId: Int = 0
    private val availableYears: MutableList<Price> = mutableListOf()
    private var month: Int = 1
    private var year: Int = 2022
    private var isMonthly = true
    private lateinit var currentMonth: DateSet
    private lateinit var currentYear: DateSet

    override fun getViewBinding(): ActivityProductDetailBinding =
        ActivityProductDetailBinding.inflate(layoutInflater)

    override fun setupIntent() {
        productId = intent?.getIntExtra(BundleKeys.PRODUCT_ID, 0) as Int
    }

    override fun setupUI() {
        setupToolbar(
            binding.toolbar.toolbar,
            getString(R.string.title_product_detail),
            true
        )
        setLoadingDialog(getLoadingDialog(this))
        setErrorDialog(getErrorDialog(this))

        setupChart()
    }

    override fun setupAction() {
        binding.apply {
            btnMontAndYearPicker.setOnClickListener {
                monthYearPickerDialog.show(supportFragmentManager)
            }
            btnToday.setOnClickListener {
                getProductPriceByMonthAndYear(currentMonth.first, currentYear.second.toInt())
            }
        }
        monthYearPickerDialog.setOnMontAndYearPicked { month, year ->
            this.month = month.first + 1
            this.year = year.second.toInt()
            getProductPriceByMonthAndYear(month.first, year.second.toInt())
        }
    }

    override fun setupProcess() {
        getProductDetail()
        getProductAvailableYears()
        getProductPrices()
    }

    override fun setupObserver() {
        productViewModel.productDetail.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it, ::getProductDetail)
            },
            onSuccess = {
                dismissLoading()
                setProductDetail(it)
            }
        )
        priceViewModel.availableYears.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it, ::getProductAvailableYears)
            },
            onSuccess = {
                lifecycleScope.launch {
                    dismissLoading()
                    availableYears.addAll(it)
                    val years = availableYears.map { yearItem -> yearItem.year.toString() }
                    monthYearPickerDialog.apply {
                        setMonthsAndYears(
                            ArrayList(getMonths()),
                            ArrayList(years)
                        )
                    }
                    val month = Calendar.getInstance(localeId).get(Calendar.MONTH) + 1
                    currentMonth = DateSet(month, getMonths()[month - 1])
                    val year = Calendar.getInstance(localeId).get(Calendar.YEAR) - 1 // TODO: Change it later
                    currentYear = DateSet(years.indexOf(year.toString()), year.toString())
                }
            }
        )
        priceViewModel.priceByMonthAndYear.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it){ getProductPriceByMonthAndYear(month, year) }
            },
            onSuccess = {
                dismissLoading()
                setPriceByMonthAndYear(it)
            }
        )
        priceViewModel.productPrices.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it) { getProductPrices() }
            },
            onSuccess = {
                dismissLoading()
                setLineChart(it,  xAxisLabels = it.labels)
            }
        )
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
                            isMonthly = true
                            getProductPrices()
                        }else{
                            isMonthly = false
                            getProductPrices()
                        }
                    }
                )
                setText(getChartTypes().first(), false)
            }
        }
    }

    private fun setProductDetail(product: Product){
        with(binding){
            imgProduct.setImage(product.imageUrl, 200.dp, 100.dp, pbProduct)
            tvProductName.text = product.name
            val weightPerPiece = "${product.weight.formatThousand()} ${product.unit}"
            tvProductWeightPerPiece.text = weightPerPiece
            tvProductPrice.text = product.price.formatCurrency()
            tvProductDescription.text = product.description
            setIndicator(product.isRising)
            tvPriceInfo.text = getString(R.string.label_current_month)

            setupDropDown()
        }
    }

    private fun setIndicator(isRising: Boolean){
        binding.imgIndicator.setImageResource(if(isRising) R.drawable.ic_indicator_up else R.drawable.ic_indicator_down)
    }

    private fun setPriceByMonthAndYear(price: Product){
        with(binding){
            tvProductPrice.text = price.price.formatCurrency()
            val info = "(${price.month.replaceFirstChar { it.uppercase() }} ${price.year})"
            tvPriceInfo.text = info
            setIndicator(price.isRising)
        }
    }

    private fun setLineChart(historicalData: PriceEntry, predictionData: PriceEntry = PriceEntry(), xAxisLabels: List<String>) {
        val actualDataSet = LineDataSet(historicalData.prices, getString(R.string.label_actual))
        val predictionDataSet = LineDataSet(predictionData.prices, getString(R.string.label_prediction))
        actualDataSet.apply {
            val mColor = ContextCompat.getColor(this@ProductDetailActivity, R.color.greenLeaf)
            color = mColor
            setDrawValues(false)
            circleColors = listOf(mColor)
            circleHoleRadius = 1.5f
            circleRadius = 3f
            lineWidth = 2f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }
        predictionDataSet.apply {
            val mColor = ContextCompat.getColor(this@ProductDetailActivity, R.color.anotherGrey)
            color = mColor
            setDrawValues(false)
            circleColors = listOf(mColor)
            circleHoleRadius = 1.5f
            circleRadius = 3f
            enableDashedLine(20f, 10f, 0f)
            lineWidth = 2f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }
        binding.crtPrices.apply {
            val lineData = LineData(actualDataSet, predictionDataSet).apply {
                setValueFormatter(YValueFormatter(this@ProductDetailActivity))
            }
            xAxis.valueFormatter = XAxisValueFormatter(this@ProductDetailActivity, xAxisLabels)
            data = lineData
            invalidate()
        }
    }

    private fun getChartTypes(): List<String> {
        return listOf(
            getString(R.string.label_month),
            getString(R.string.label_year)
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

    private fun getProductDetail(){
        productViewModel.getProductDetail(productId)
    }

    private fun getProductAvailableYears(){
        priceViewModel.getProductAvailableYears(productId)
    }

    private fun getProductPriceByMonthAndYear(month: Int, year: Int){
        priceViewModel.getProductPriceByMonthAndYear(productId, month, year)
    }

    private fun getProductPrices(){
        priceViewModel.getProductPrices(productId, isMonthly)
    }

    private fun getEntryList(items: List<Price>): List<Entry> =
        items.mapIndexed{ index, price -> Entry(index.toFloat(), price.price.toFloat()) }

    companion object{
        @JvmStatic
        fun start(context: Context, productId: Int) {
            context.startActivity(Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(BundleKeys.PRODUCT_ID, productId)
            })
        }
    }
}