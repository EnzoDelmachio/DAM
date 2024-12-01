package com.example.myapplication.api

import com.example.myapplication.model.Informacao
import com.example.myapplication.model.Login
import com.example.myapplication.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Routes {

    @POST("login")
    suspend fun postLogin(@Body loginRequest: LoginRequest): Response<Login>

    @POST("saveData")
    suspend fun postInformacao (@Header ("Authorization") token: String, @Body data: Informacao): Response<Void>

    @GET("getData")
    suspend fun getInformacoes(@Header("Authorization") token: String): Response<List<String>>

}