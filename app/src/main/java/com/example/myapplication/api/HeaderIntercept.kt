package com.example.myapplication.api

import okhttp3.Interceptor
import okhttp3.Response
import android.content.Context

class HeaderIntercept(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        val requestBuilder = chain.request().newBuilder()
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}