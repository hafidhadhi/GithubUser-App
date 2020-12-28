package com.hafidhadhi.submissiontwo.data.remote.utils

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val modifiedReq = request.newBuilder()
            .addHeader("Accept", "application/vnd.github.v3+json")
//            .addHeader("Authorization", "token 8038e84ff73d107f1ea157d9971e22e377e14d2a")
            .build()

        return chain.proceed(modifiedReq)
    }
}