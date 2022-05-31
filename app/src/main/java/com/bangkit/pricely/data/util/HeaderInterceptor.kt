package com.bangkit.pricely.data.util

import com.bangkit.pricely.util.AppConst
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val url = chain.request().url.newBuilder()
        url.addQueryParameter("api_key", AppConst.API_KEY)
        request.url(url.build())
        return try {
            chain.proceed(request.build())
        } catch (e: Throwable) {
            Response.Builder()
                .message(e.message.orEmpty())
                .code(999)
                .protocol(Protocol.HTTP_2)
                .body(
                    Gson().toJson(
                        BaseResponse<Any>(
                            999,
                            null,
                            e.message,
                            false,
                            ""
                        )
                    ).toResponseBody()
                )
                .request(chain.request())
                .build()
        }
    }
}