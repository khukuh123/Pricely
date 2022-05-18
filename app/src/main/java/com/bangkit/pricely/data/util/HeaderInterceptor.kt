package com.bangkit.pricely.data.util

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: Throwable) {
            Response.Builder()
                .message(e.message ?: "")
                .code(999)
                .protocol(Protocol.HTTP_2)
                .body("""
                            {
                                "message": "${("""\$\\"'""".toRegex().replace(e.message ?: "", """\"""")).replace("\"", """\"""")}"
                            }
                        """.trimIndent().toResponseBody()
                )
                .request(chain.request())
                .build()
        }
    }
}