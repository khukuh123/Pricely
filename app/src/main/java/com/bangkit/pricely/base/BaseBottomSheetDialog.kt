package com.bangkit.pricely.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog<VB: ViewBinding>: BottomSheetDialogFragment() {
    private var _binding: VB? = null

    abstract val TAG: String

    protected val binding: VB
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getViewBinding(container)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initIntent()
        setupUI()
        setupAction()
    }

    abstract fun initIntent()

    abstract fun setupUI()

    abstract fun setupAction()

    abstract fun getViewBinding(container: ViewGroup?): VB

    fun showDialog(manager: FragmentManager) {
        super.show(manager, TAG)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}