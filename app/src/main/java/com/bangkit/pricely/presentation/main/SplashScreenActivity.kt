package com.bangkit.pricely.presentation.main

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.base.reloadModules
import com.bangkit.pricely.databinding.ActivitySplashScreenBinding
import com.bangkit.pricely.util.AppConst
import com.bangkit.pricely.util.RemoteConfigKey
import com.bangkit.pricely.util.showToast
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    private val remoteConfig: FirebaseRemoteConfig by inject()

    override fun getViewBinding(): ActivitySplashScreenBinding =
        ActivitySplashScreenBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {

    }

    override fun setupAction() {

    }

    override fun setupProcess() {
        lifecycleScope.launch {
            if (!RemoteConfigKey.isRemoteConfigLoadingDone) {
                remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        with(remoteConfig) {
                            AppConst.API_KEY = getString(RemoteConfigKey.API_KEY)
                            AppConst.API_URL_BASE = getString(RemoteConfigKey.BASE_URL)
                        }
                        RemoteConfigKey.isRemoteConfigLoadingDone = true
                        reloadModules()
                        MainActivity.restart(this@SplashScreenActivity)
                    } else {
                        showToast("Failed to fetch remote config\n${task.exception?.message.toString()}")
                        RemoteConfigKey.isRemoteConfigLoadingDone = false
                    }
                }
            }
        }
    }

    override fun setupObserver() {

    }
}