package com.bangkit.pricely.di

import com.bangkit.pricely.BuildConfig
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
import com.bangkit.pricely.domain.price.PriceInteractor
import com.bangkit.pricely.domain.price.PriceUseCase
import com.bangkit.pricely.domain.product.ProductInteractor
import com.bangkit.pricely.domain.product.ProductUseCase
import com.bangkit.pricely.presentation.viewmodel.PriceViewModel
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.AppConst
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
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

    single<ProductApiClient> {
        Retrofit.Builder()
            .baseUrl(AppConst.API_URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ProductApiClient::class.java)
    }

    single<PriceApiClient> {
        Retrofit.Builder()
            .baseUrl(AppConst.API_URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(PriceApiClient::class.java)
    }

    single {
        ProductApi(get())
    }
    single {
        PriceApi(get())
    }
}

val repositoryModule = module {
    single<ProductRepository> { ProductDataStore(get()) }
    single<PriceRepository> { PriceDataStore(get()) }
}

val useCaseModule = module {
    single<ProductUseCase> { ProductInteractor(get()) }
    single<PriceUseCase> { PriceInteractor(get()) }
}

val viewModelModule = module {
    viewModel { ProductViewModel(get()) }
    viewModel { PriceViewModel(get()) }
}