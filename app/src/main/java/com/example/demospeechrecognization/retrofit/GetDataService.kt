package com.example.demospeechrecognization.retrofit

import com.example.demospeechrecognization.models.BrainShopResponse
import com.example.demospeechrecognization.models.GetSessionModel
import com.example.demospeechrecognization.models.ThinkThoughtModel
import com.example.demospeechrecognization.utils.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GetDataService {

    @GET("v1/lydia/session/get")
    fun getSessions(
        @Query("access_key") api_key: String,
        @Query("session_id") session_id: String
    ): Call<GetSessionModel>

    @GET("v1/lydia/session/create")
    fun createSession(
        @Query("access_key") api_key: String,
        @Query("target_language") target_language: String
    ): Call<GetSessionModel>

    @GET("v1/lydia/session/think")
    fun thinkThought(
        @Query("access_key") api_key: String,
        @Query("session_id") session_id: String,
        @Query("input") input: String
    ): Call<ThinkThoughtModel>

    @GET("/get")
    fun get(
        @Query("bid") bid: String,
        @Query("uid") uid: String,
        @Query("key") key: String,
        @Query("msg") msg: String
    ): Call<BrainShopResponse>
}