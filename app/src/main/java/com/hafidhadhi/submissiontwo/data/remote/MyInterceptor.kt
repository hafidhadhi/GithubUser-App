package com.hafidhadhi.submissiontwo.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().apply {
            header("Accept:application/vnd.github.v3+json")
        }
        return chain.proceed(request)
    }
}