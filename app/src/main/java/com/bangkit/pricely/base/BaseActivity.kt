package com.bangkit.pricely.base

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.bangkit.pricely.R

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    private var mBinding: T? = null
    protected val binding
        get() = mBinding!!
    private var loadingDialog: AlertDialog? = null
    private var errorDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = getViewBinding()

        setContentView(mBinding?.root)
        setupIntent()
        setupUI()
        setupAction()
        setupProcess()
        setupObserver()
    }

    abstract fun getViewBinding(): T

    abstract fun setupIntent()

    abstract fun setupUI()

    abstract fun setupAction()

    abstract fun setupProcess()

    abstract fun setupObserver()

    protected fun showLoading() {
        loadingDialog?.show()
    }

    protected fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    protected fun showErrorDialog(message: String, onRetry: () -> Unit) {
        errorDialog?.apply {
            this.setTitle(getString(R.string.label_error))
            setMessage(message)
            setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.action_retry)) { dialog, _ ->
                onRetry.invoke()
                dialog.dismiss()
            }
            show()
        }
    }

    protected fun setLoadingDialog(alertDialog: AlertDialog) {
        loadingDialog = alertDialog
    }

    protected fun setErrorDialog(alertDialog: AlertDialog) {
        errorDialog = alertDialog
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}