package com.bangkit.pricely.di

import android.content.Context
import android.util.Log
import com.bangkit.pricely.BuildConfig
import com.bangkit.pricely.R
import com.bangkit.pricely.data.category.CategoryDataStore
import com.bangkit.pricely.data.category.CategoryRepository
import com.bangkit.pricely.data.category.remote.CategoryApi
import com.bangkit.pricely.data.category.remote.CategoryApiClient
import com.bangkit.pricely.data.price.PriceDataStore
import com.bangkit.pricely.data.price.PriceRepository
import com.bangkit.pricely.data.price.remote.PriceApi
import com.bangkit.pricely.data.price.remote.PriceApiClient
import com.bangkit.pricely.data.product.ProductDataStore
import com.bangkit.pricely.data.product.ProductRepository
import com.bangkit.pricely.data.product.remote.ProductApi
import com.bangkit.pricely.data.product.remote.ProductApiClient
import com.bangkit.pricely.data.util.HeaderInterceptor
import com.bangkit.pricely.data.util.getSSLConfiguration
import com.bangkit.pricely.data.util.getTrustManager
import com.bangkit.pricely.domain.category.CategoryInteractor
import com.bangkit.pricely.domain.category.CategoryUseCase
import com.bangkit.pricely.domain.price.PriceInteractor
import com.bangkit.pricely.domain.price.PriceUseCase
import com.bangkit.pricely.domain.product.ProductInteractor
import com.bangkit.pricely.domain.product.ProductUseCase
import com.bangkit.pricely.presentation.viewmodel.CategoryViewModel
import com.bangkit.pricely.presentation.viewmodel.PriceViewModel
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.AppConst
import com.bangkit.pricely.util.RemoteConfigKey
import com.bangkit.pricely.util.showToast
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager


val networkModule = module {

    val httpLogging = "http_logging"

    single<Interceptor>(named(httpLogging)) {
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        )
    }

    single {
        val trustManagerFactory = getTrustManager(get())
        val trustManagers = trustManagerFactory.trustManagers

        if(trustManagers.size != 1 || trustManagers.first() !is X509TrustManager){
            throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
        }
        trustManagers.first() as X509TrustManager
    }

    single {
        getSSLConfiguration(get()).socketFactory
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(interceptor = get(named(httpLogging)))
            .connectTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .sslSocketFactory(get(), get())
            .build()
    }

    scope(named("baseUrl")){
        scoped {
            provideBaseUrlHolder()
        }
    }

    single<Retrofit>{
        Retrofit.Builder()
            .baseUrl(AppConst.API_URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<ProductApiClient> {
        get<Retrofit>()
            .create(ProductApiClient::class.java)
    }

    single<PriceApiClient> {
        get<Retrofit>()
            .create(PriceApiClient::class.java)
    }

    single<CategoryApiClient> {
        get<Retrofit>()
            .create(CategoryApiClient::class.java)
    }

    single {
        ProductApi(get())
    }
    single {
        PriceApi(get())
    }
    single {
        CategoryApi(get())
    }
}

val repositoryModule = module {
    single<ProductRepository> { ProductDataStore(get()) }
    single<PriceRepository> { PriceDataStore(get()) }
    single<CategoryRepository> { CategoryDataStore(get()) }
}

val useCaseModule = module {
    single<ProductUseCase> { ProductInteractor(get()) }
    single<PriceUseCase> { PriceInteractor(get()) }
    single<CategoryUseCase> { CategoryInteractor(get()) }
}

val viewModelModule = module {
    viewModel { ProductViewModel(get()) }
    viewModel { PriceViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
}

val firebaseModule = module {
    single {
        val remoteConfig = Firebase.remoteConfig
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.apply {
            setConfigSettingsAsync(settings)
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
        remoteConfig
    }
}

fun provideBaseUrlHolder(): String {
    return AppConst.API_URL_BASE
}