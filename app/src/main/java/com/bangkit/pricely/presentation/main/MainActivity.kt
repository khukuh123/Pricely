package com.bangkit.pricely.presentation.main

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        installSplashScreen()
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupIntent() {}

    override fun setupUI() {}

    override fun setupAction() {}

    override fun setupProcess() {}

    override fun setupObserver() {}
}