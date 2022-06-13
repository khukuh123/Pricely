@file:Suppress("unused")
package com.bangkit.pricely.base

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.bangkit.pricely.di.*
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

class PricelyApplication: Application() {

    private var scope: Scope? = null

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@PricelyApplication)
            modules(
                getModules()
            )
        }
        scope = getKoin().getOrCreateScope("baseUrlId", named("baseUrl"))
    }

    fun refreshScope(){
        scope?.close()
        scope = getKoin().getOrCreateScope("baseUrlId", named("baseUrl"))
        provideBaseUrlHolder()
    }
}

fun getModules() =
    listOf(
        firebaseModule,
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule
    )

fun reloadModules() {
    unloadKoinModules(getModules())
    loadKoinModules(getModules())
}