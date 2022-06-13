package com.bangkit.pricely.util

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.bangkit.pricely.R
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

val Int.dp
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val localeId = Locale("id", "ID")

private val currencyFormatter = NumberFormat.getCurrencyInstance(localeId)
private val decimalFormatSymbols = DecimalFormatSymbols(localeId).apply {
    groupingSeparator  = ' '
    digit = '.'
}
private val numberFormatter = DecimalFormat("###,###.#", decimalFormatSymbols)

fun Int?.orZero(): Int = (this ?: 0)

fun Int.formatCurrency(numberFormat: NumberFormat = currencyFormatter): String{
    return StringBuilder(numberFormat.format(this))
        .insert(2, ' ')
        .toString()
}

fun Int.formatThousand(numberFormat: NumberFormat = numberFormatter): String{
    return numberFormat.format(this)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ImageView.setImageFromUrl(image: String, size: Int? = null, progressBar: ProgressBar? = null) {
    this.setImage(image, size, size, progressBar)
}

fun ImageView.setImage(image: Any, width: Int?, height: Int?, progressBar: ProgressBar? = null) {
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    this.load(image, imageLoader) {
        if(width != null && height != null) size(width, height)
        listener(
            onSuccess = { _, _ ->
                progressBar?.gone()
            }
        )
        error(R.drawable.ic_baseline_image_24)
    }
}

fun <T> LiveData<Resource<T>>.observe(
    lifecycleOwner: LifecycleOwner,
    onLoading: () -> Unit,
    onSuccess: (items: T) -> Unit,
    onError: (errorMessage: String) -> Unit,
) {
    observe(lifecycleOwner) {
        when (it) {
            is Resource.Loading<T> -> onLoading.invoke()
            is Resource.Success<T> -> it.data?.let { data -> onSuccess.invoke(data) }
            else -> onError.invoke(it.errorMessage.orEmpty())
        }
    }
}

fun AppCompatActivity.setupToolbar(toolbar: Toolbar,title: String, showBack: Boolean) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        this.title = title
        setDisplayHomeAsUpEnabled(showBack)
    }
}

fun <T> CoroutineScope.collectResult(liveData: MutableLiveData<T>, block: suspend () -> Flow<T>) {
    this.launch {
        val result = block.invoke()
        result.collect {
            liveData.postValue(it)
        }
    }
}
fun formatPrice(price: Int): StringBuilder {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return StringBuilder(format.format(price)).insert(2, ' ')
}

fun formatLargeValue(value: Long, digit: Int = 1, base: Array<String>): String {
    val textValue = value.toString()
    val chunkedTextValue = textValue.reversed().chunked(3) {
        it.reversed()
    }.reversed()
    val result = StringBuilder("${chunkedTextValue.first()}.")
    try {
        for (i in 0 until digit) {
            result.append(chunkedTextValue[1][i])
        }
    }catch (e: IndexOutOfBoundsException){ }
    result.append(base[chunkedTextValue.size - 1])
    return if (chunkedTextValue.size > 1)
        result.toString()
    else
        textValue
}

fun getMonths(): List<String> {
    return listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Des"
    )
}

fun <T> AutoCompleteTextView.setAdapter(
    context: Context,
    data: List<T>,
    mapper: (T) -> String,
    onItemClicked: (position: Int, item: T) -> Unit
){
    val adapter = ArrayAdapter(
        context,
        R.layout.item_dropdown,
        R.id.tvItemDropdown,
        data.map { mapper(it) }
    )
    this.apply {
        setOnItemClickListener { _, _, position, _ ->
            onItemClicked.invoke(position, data[position])
        }
        setAdapter(adapter)
    }
}

fun Context.hideSoftInput(view: View){
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
