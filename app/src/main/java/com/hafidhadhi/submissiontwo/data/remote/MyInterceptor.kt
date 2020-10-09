package com.hafidhadhi.submissiontwo.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val modifiedReq = request.newBuilder()
            .addHeader("Accept", "application/vnd.github.v3+json")
            .addHeader("Authorization", "token 9cd7a685216af93a60d753a90bb5bfd508cdb329")
            .build()

        return chain.proceed(modifiedReq)
    }
}