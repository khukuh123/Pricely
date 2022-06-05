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
                    map[IS_DONE_LOADING]?.let { isDoneLoading ->
                        btnViewAll.isVisible = isDoneLoading
                        pbViewAll.isVisible = !isDoneLoading
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseFooterViewHolder =
        ViewAllViewHolder(ItemViewAllBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    fun showFooter(isDoneLoading: Boolean) {
        notifyItemRangeChanged(
            0,
            1,
            mapOf(
                IS_DONE_LOADING to isDoneLoading
            )
        )
        showFooter = isDoneLoading
        showLoading = !isDoneLoading
    }

    companion object{
        private const val IS_DONE_LOADING = "is_done_loading"
    }
}