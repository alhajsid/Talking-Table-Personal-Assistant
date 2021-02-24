package com.example.demospeechrecognization.repositories

import android.util.Log
import retrofit2.Call
import androidx.lifecycle.MutableLiveData
import com.example.demospeechrecognization.models.GetSessionModel
import com.example.demospeechrecognization.models.ThinkThoughtModel
import com.example.demospeechrecognization.retrofit.RetrofitClientInstance
import com.example.demospeechrecognization.retrofit.GetDataService
import retrofit2.Callback
import retrofit2.Response

class SessionRepositories {
    private var instance: SessionRepositories?=null
    lateinit var session:GetSessionModel
    lateinit var thinkThoughtModel: ThinkThoughtModel
    var language="en"

    fun getInstance(): SessionRepositories {
        if (instance==null){
            instance= SessionRepositories()
        }
        return instance!!
    }

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

}