package com.example.demospeechrecognization.repositories

import android.util.Log
import retrofit2.Call
import androidx.lifecycle.MutableLiveData
import com.example.demospeechrecognization.models.BrainShopResponse
import com.example.demospeechrecognization.models.GetSessionModel
import com.example.demospeechrecognization.models.ThinkThoughtModel
import com.example.demospeechrecognization.retrofit.RetrofitClientInstance
import com.example.demospeechrecognization.retrofit.GetDataService
import com.example.demospeechrecognization.utils.BaseResponse
import retrofit2.Callback
import retrofit2.Response

object SessionRepositories {
    private var instance: SessionRepositories?=null
    lateinit var session:GetSessionModel
    lateinit var thinkThoughtModel: ThinkThoughtModel
    private var language="en"

    fun createSession(api_key:String): MutableLiveData<GetSessionModel> {
        val data=MutableLiveData<GetSessionModel>()
        val service: GetDataService = RetrofitClientInstance.retrofitInstance()!!.create(
            GetDataService::class.java
        )
        val call= service.createSession(api_key,language)
        call.enqueue(object : Callback<GetSessionModel> {

            override fun onResponse(call: Call<GetSessionModel>, response: Response<GetSessionModel>) {
                if (response.body()!=null) {
                    data.value = response.body()
                    session = response.body()!!
                }
            }

            override fun onFailure(call: Call<GetSessionModel>, t: Throwable) {
                Log.e("alhaj","error")
            }
        })
        return data
    }

    fun getSession(api_key:String,sessionId:String): MutableLiveData<GetSessionModel> {
        val data=MutableLiveData<GetSessionModel>()
        val service: GetDataService = RetrofitClientInstance.retrofitInstance()!!.create(
            GetDataService::class.java
        )
        val call= service.getSessions(api_key,sessionId)
        call.enqueue(object : Callback<GetSessionModel> {

            override fun onResponse(call: Call<GetSessionModel>, response: Response<GetSessionModel>) {
                if (response.body()!=null) {
                    data.value = response.body()
                    session = response.body()!!
                }
            }

            override fun onFailure(call: Call<GetSessionModel>, t: Throwable) {
                Log.e("alhaj","error")
            }
        })
        return data
    }

    fun thinkThought(api_key:String,sessionId:String,input:String): MutableLiveData<ThinkThoughtModel> {
        val data=MutableLiveData<ThinkThoughtModel>()
        val service: GetDataService = RetrofitClientInstance.retrofitInstance()!!.create(
            GetDataService::class.java
        )
        val call= service.thinkThought(api_key,sessionId,input)
        call.enqueue(object : Callback<ThinkThoughtModel> {

            override fun onResponse(call: Call<ThinkThoughtModel>, response: Response<ThinkThoughtModel>) {
                if (response.body()!=null) {
                    data.value = response.body()
                    thinkThoughtModel=response.body()!!
                }
            }

            override fun onFailure(call: Call<ThinkThoughtModel>, t: Throwable) {
                Log.e("alhaj","error")
            }
        })
        return data
    }

    fun get(bid:String,uid:String,key:String,msg:String): MutableLiveData<BaseResponse<BrainShopResponse>> {
        val data=MutableLiveData<BaseResponse<BrainShopResponse>>()
        val service: GetDataService = RetrofitClientInstance.retrofitInstance()!!.create(
            GetDataService::class.java
        )
        val call= service.get(bid, uid, key, msg)
        call.enqueue(object : Callback<BrainShopResponse> {

            override fun onResponse(call: Call<BrainShopResponse>, response: Response<BrainShopResponse>) {
                if (response.isSuccessful && response.body()!=null) {
                    data.value = BaseResponse.success(response.body())
                }else{
                    data.value = BaseResponse.error(response.message() ?: "error",null)
                }
            }

            override fun onFailure(call: Call<BrainShopResponse>, t: Throwable) {
                data.value = BaseResponse.error(t.message ?: "error",null)
            }
        })
        return data
    }

}