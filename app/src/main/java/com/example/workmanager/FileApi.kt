package com.example.workmanager

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

interface FileApi {

    @GET("/photo/2017/06/17/10/55/hot-air-balloon-2411851_1280.jpg")
    suspend fun downloadImage() : Response<ResponseBody>

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("https://cdn.pixabay.com")
                .build()
                .create(FileApi::class.java)
        }
    }
}