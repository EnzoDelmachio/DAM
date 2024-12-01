package com.example.myapplication.api

import android.content.Context
import com.example.myapplication.model.Informacao
import com.example.myapplication.model.Login
import com.example.myapplication.model.LoginRequest
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService(context: Context) {

    private val api: Routes

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HeaderIntercept(context))
            .build()

        api = Retrofit.Builder().baseUrl("https://trabalho-final-api-production.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(Routes::class.java)
    }

    suspend fun postLogin(username: String): Response<Login> {
        val loginRequest = LoginRequest(username)
        return api.postLogin(loginRequest)
    }

    suspend fun addInformacao(token: String, data: String): Response<Void> {
        val inform = Informacao(data)
        return api.postInformacao("Bearer $token", inform)
    }

    suspend fun getInformacoes(token: String): Response<List<String>> {
        return api.getInformacoes("Bearer $token")
    }

}