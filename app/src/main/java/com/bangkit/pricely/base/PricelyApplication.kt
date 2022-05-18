@file:Suppress("unused")
package com.bangkit.pricely.base

import android.app.Application
import com.bangkit.pricely.di.networkModule
import com.bangkit.pricely.di.repositoryModule
import com.bangkit.pricely.di.useCaseModule
import com.bangkit.pricely.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PricelyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@PricelyApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}