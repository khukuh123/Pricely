package com.bangkit.pricely.data.util

import android.content.Context
import com.bangkit.pricely.R
import com.bangkit.pricely.domain.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.security.cert.CertificateException

fun <T, U> Flow<ApiResult<T>>.mapToDomain(mapper: (T) -> U): Flow<Resource<U>> =
    this.map {
        when (it) {
            is ApiResult.Success -> {
                Resource.Success(mapper.invoke(it.result!!))
            }
            else -> {
                Resource.Error(it.errorCode ?: 999, it.errorMessage ?: "")
            }
        }
    }

fun <T> Response<BaseResponse<T>>.call(): Flow<ApiResult<T>> =
    flow<ApiResult<T>> {
        try {
            val response = this@call
            if (response.isSuccessful) {
                emit(ApiResult.Success(response.body()?.data))
            } else {
                response.errorBody()?.let {
                    val errorResponse = Gson().fromJson(it.string(), BaseResponse::class.java)
                    emit(ApiResult.Error(errorResponse.code, errorResponse.message.orEmpty()))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(999, e.message.orEmpty()))
        }
    }.flowOn(Dispatchers.IO)

@Throws(
    CertificateException::class,
    IOException::class,
    KeyStoreException::class,
    NoSuchAlgorithmException::class,
    KeyManagementException::class
)
fun getSSLConfiguration(context: Context): SSLContext {
    // Creating an SSLSocketFactory that uses our TrustManager
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, getTrustManager(context).trustManagers, null)
    return sslContext
}

private fun getKeystore(context: Context): KeyStore {
    // Creating a KeyStore containing our trusted CAs
    val keyStoreType = KeyStore.getDefaultType()
    val keyStore = KeyStore.getInstance(keyStoreType)
    keyStore.load(null, null)
    keyStore.setCertificateEntry("ca", getCertificate(context))
    return keyStore
}

fun getTrustManager(context: Context): TrustManagerFactory {
    // Creating a TrustManager that trusts the CAs in our KeyStore.
    val trustManagerFactoryAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
    val trustManagerFactory = TrustManagerFactory.getInstance(trustManagerFactoryAlgorithm)
    trustManagerFactory.init(getKeystore(context))
    return trustManagerFactory
}

private fun getCertificate(context: Context): Certificate? {
    // Loading CAs from file
    val certificateFactory: CertificateFactory? = CertificateFactory.getInstance("X.509")
    return context.resources.openRawResource(R.raw.certificate)
        .use { cert -> certificateFactory?.generateCertificate(cert) }
}