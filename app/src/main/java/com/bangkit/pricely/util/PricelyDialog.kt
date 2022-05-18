package com.bangkit.pricely.util

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.bangkit.pricely.R
import com.bangkit.pricely.databinding.LayoutLoadingBinding

fun getLoadingDialog(context: Context) =
    AlertDialog.Builder(context, R.style.PricelyLoadingDialog).apply {
        val binding = LayoutLoadingBinding.inflate(LayoutInflater.from(context))
        setView(binding.root)
        setCancelable(false)
    }.create()

fun getErrorDialog(context: Context) =
    AlertDialog.Builder(context).apply {
        setCancelable(true)
    }.create()