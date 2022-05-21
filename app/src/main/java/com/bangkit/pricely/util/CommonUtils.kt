package com.bangkit.pricely.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkit.pricely.R
import com.bangkit.pricely.domain.util.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

val localeId = Locale("id", "ID")

private val currencyFormatter = NumberFormat.getCurrencyInstance(localeId)

fun Double.formatCurrency(numberFormat: NumberFormat = currencyFormatter): String{
    return StringBuilder(numberFormat.format(this))
        .insert(2, ' ')
        .toString()
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

@SuppressLint("CheckResult")
fun ImageView.setImageFromUrl(image: String, size: Int? = null) {
    val request = RequestOptions().apply {
        error(R.drawable.ic_baseline_image_24)
        placeholder(R.drawable.ic_baseline_image_24)
    }
    if (size != null) request.override(size)
    Glide.with(this).load(image).apply(request).into(this)
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

fun formatLargeValue(value: Long, digit: Int = 1, base: Array<String>): String {
    val textValue = value.toString()
    val chunkedTextValue = textValue.reversed().chunked(3) {
        it.reversed()
    }.reversed()
    val result = StringBuilder("${chunkedTextValue.first()}.")
    for (i in 0 until digit) {
        result.append(chunkedTextValue[1][i])
    }
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