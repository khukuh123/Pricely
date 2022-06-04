package com.bangkit.pricely.util.reusable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bangkit.pricely.base.BaseFooterAdapter
import com.bangkit.pricely.base.BaseFooterViewHolder
import com.bangkit.pricely.databinding.ItemViewAllBinding

class ViewAllAdapter(
    private val onViewAllButtonClicked: () -> Unit,
) : BaseFooterAdapter<BaseFooterViewHolder>() {

    private var showLoading = true
    private var showFooter = false

    inner class ViewAllViewHolder(_binding: ViewBinding) : BaseFooterViewHolder(_binding) {
        override fun bind() {
            with(binding as ItemViewAllBinding) {
                btnViewAll.setOnClickListener {
                    onViewAllButtonClicked.invoke()
                }
                btnViewAll.isVisible = showFooter
                pbViewAll.isVisible = showLoading
            }
        }

        override fun bind(payload: MutableList<Any>) {
            with(binding as ItemViewAllBinding){
                (payload.firstOrNull() as? Map<String, Boolean>)?.let { map ->
                    map["showFooter"]?.let { showFooter ->
                        btnViewAll.isVisible = showFooter
                        pbViewAll.isVisible = !showFooter
                    }
                    map["showLoading"]?.let { showLoading ->
                        pbViewAll.isVisible = showLoading
                        btnViewAll.isVisible = !showLoading
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseFooterViewHolder =
        ViewAllViewHolder(ItemViewAllBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    fun showFooter(isShow: Boolean) {
        notifyItemRangeChanged(
            0,
            1,
            mapOf(
                "showFooter" to isShow
            )
        )
        showFooter = isShow
    }

    fun showLoading(isShow: Boolean) {
        notifyItemRangeChanged(
            0,
            1,
            mapOf(
                "showLoading" to isShow
            )
        )
        showLoading = isShow
    }
}