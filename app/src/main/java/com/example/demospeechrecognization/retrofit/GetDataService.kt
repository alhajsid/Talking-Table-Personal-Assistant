package com.example.demospeechrecognization.retrofit

import com.example.demospeechrecognization.models.GetSessionModel
import com.example.demospeechrecognization.models.ThinkThoughtModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GetDataService {

    @POST("/v1/lydia/session/get")
    fun getSessions(@Query("api_key") api_key: String,@Query("session_id") session_id: String): Call<GetSessionModel>

    @POST("/v1/lydia/session/create")
    fun createSession(@Query("api_key") api_key: String,@Query("target_language") target_language: String): Call<GetSessionModel>

    @POST("/v1/lydia/session/think")
    fun thinkThought(@Query("api_key") api_key: String,@Query("session_id") session_id: String,@Query("input") input: String): Call<ThinkThoughtModel>
}