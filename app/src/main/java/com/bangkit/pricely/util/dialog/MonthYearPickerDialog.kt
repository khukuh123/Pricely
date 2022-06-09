package com.bangkit.pricely.util.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bangkit.pricely.databinding.FragmentMonthYearPickerBinding
import com.bangkit.pricely.util.setAdapter

typealias DateSet = Pair<Int, String>

class MonthYearPickerDialog : DialogFragment() {

    private val months: ArrayList<String> = arrayListOf()
    private val years: ArrayList<String> = arrayListOf()
    private var selectedMonth: DateSet? = null
    private var selectedYear: DateSet? = null
    private var isSelected = false
    private var _binding: FragmentMonthYearPickerBinding? = null
    private var onMonthAndYearPicked: ((month: DateSet, year: DateSet) -> Unit)? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMonthYearPickerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIntent()
        setupUI()
        setupAction()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupIntent() {

    }

    private fun setupUI() {
        isCancelable = false
        setupDropDown()
    }

    private fun setupAction() {
        with(binding) {
            btnNeutral.setOnClickListener {
                dismiss()
                if (!isSelected) {
                    selectedMonth = null
                    selectedYear = null
                }
            }
            btnPositive.setOnClickListener {
                if (validatePicker()) {
                    onMonthAndYearPicked?.invoke(selectedMonth!!, selectedYear!!)
                }
                if (!isSelected) isSelected = true
                dismiss()
            }
        }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(TAG) == null && !isAdded) super.show(manager, TAG)
    }

    private fun setupDropDown() {
        with(binding) {
            spnMonth.setAdapter(
                requireContext(),
                months,
                mapper = { it },
                onItemClicked = { position, item ->
                    selectedMonth = DateSet(position + 1, item)
                    validatePicker()
                })
            selectedMonth?.let {
                spnMonth.setText(it.second, false)
            }
            spnYear.setAdapter(
                requireContext(),
                years,
                mapper = { it },
                onItemClicked = { position, item ->
                    selectedYear = DateSet(position, item)
                    validatePicker()
                })
            selectedYear?.let {
                spnYear.setText(it.second, false)
            }
        }
        validatePicker()
    }

    private fun validatePicker(): Boolean {
        return (selectedMonth != null && selectedYear != null).also {
            binding.btnPositive.isEnabled = it
        }
    }

    fun setMonthsAndYears(months: ArrayList<String>, years: ArrayList<String>) {
        this.months.addAll(months)
        this.years.addAll(years)
    }

    fun setOnMontAndYearPicked(onMonthAndYearPicked: (month: DateSet, year: DateSet) -> Unit) {
        this.onMonthAndYearPicked = onMonthAndYearPicked
    }

    companion object {
        private val TAG = MonthYearPickerDialog::class.java.simpleName

        fun newInstance(): MonthYearPickerDialog =
            MonthYearPickerDialog().apply {
                arguments = Bundle().apply {

                }
            }
    }
}