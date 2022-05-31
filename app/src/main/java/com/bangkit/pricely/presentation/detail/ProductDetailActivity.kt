package com.bangkit.pricely.presentation.detail

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityProductDetailBinding
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.presentation.viewmodel.PriceViewModel
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.*
import com.bangkit.pricely.util.chart.LeftAxisValueFormatter
import com.bangkit.pricely.util.chart.XAxisValueFormatter
import com.bangkit.pricely.util.chart.YValueFormatter
import com.bangkit.pricely.util.dialog.MonthYearPickerDialog
import com.bangkit.pricely.util.dialog.getErrorDialog
import com.bangkit.pricely.util.dialog.getLoadingDialog
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.koin.android.ext.android.inject
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
        binding.btnMontAndYearPicker.setOnClickListener {
            monthYearPickerDialog.show(supportFragmentManager)
        }
        monthYearPickerDialog.setOnMontAndYearPicked { month, year ->
            getPriceByMonthAndYear(month.first, year.second.toInt())
        }
    }

    override fun setupProcess() {
        getProductDetail()
        getAvailableYears()
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
                showErrorDialog(it, ::getAvailableYears)
            },
            onSuccess = {
                dismissLoading()
                availableYears.addAll(it)
                val years = availableYears.map { yearItem -> yearItem.year.toString() }
                monthYearPickerDialog.setMonthsAndYears(
                    ArrayList(getMonths()),
                    ArrayList(years)
                )
            }
        )
        priceViewModel.priceByMontAndYear.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it){ getPriceByMonthAndYear(month, year) }
            },
            onSuccess = {
                dismissLoading()
                setPriceByMonthAndYear(it)
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
            imgProduct.setImageFromUrl(product.imageUrl, 200.dp, 100.dp)
            tvProductName.text = product.name
            val weightPerPiece = "${product.weight.formatThousand()} ${product.unit}"
            tvProductWeightPerPiece.text = weightPerPiece
            tvProductPrice.text = product.price.formatCurrency()
            tvProductDescription.text = product.description
            imgIndicator.setImageResource(if(product.isRise) R.drawable.ic_indicator_up else R.drawable.ic_indicator_down)
            tvPriceInfo.text = getString(R.string.label_current_month)
//            setLineChart(getMonthlyPricesData(), getMonths())

            setupDropDown()
        }
    }

    private fun setPriceByMonthAndYear(price: Price){
        with(binding){
            tvProductPrice.text = price.price.formatCurrency()
            val info = "(${price.month.replaceFirstChar { it.uppercase() }} ${price.year})"
            tvPriceInfo.text = info
        }
    }

    private fun setLineChart(data: List<Entry>, xAxisLabels: List<String>) {
        val historyDataSet = LineDataSet(data.subList(0, (data.size+1)/2), getString(R.string.label_history))
        val predictionDataSet = LineDataSet(data.subList(((data.size+1)/2)-1, data.size), getString(R.string.label_prediction))
        historyDataSet.apply {
            val mColor = ContextCompat.getColor(this@ProductDetailActivity, R.color.greenLeaf)
            color = mColor
            setCircleColor(mColor)
            lineWidth = 2f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }
        predictionDataSet.apply {
            val mColor = ContextCompat.getColor(this@ProductDetailActivity, R.color.anotherGrey)
            color = mColor
            enableDashedLine(20f, 10f, 0f)
            setCircleColor(mColor)
            lineWidth = 2f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
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

    private fun getProductDetail(){
        productViewModel.getProductDetail(productId)
    }

    private fun getAvailableYears(){
        priceViewModel.getAvailableYears(productId)
    }

    private fun getPriceByMonthAndYear(month: Int, year: Int){
        priceViewModel.getPriceByMonthAndYear(productId, month, year)
    }

    companion object{
        @JvmStatic
        fun start(context: Context, productId: Int) {
            context.startActivity(Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(BundleKeys.PRODUCT_ID, productId)
            })
        }
    }
}