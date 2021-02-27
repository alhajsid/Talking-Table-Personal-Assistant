package com.example.demospeechrecognization.repositories

import android.util.Log
import retrofit2.Call
import androidx.lifecycle.MutableLiveData
import com.example.demospeechrecognization.models.*
import com.example.demospeechrecognization.retrofit.RetrofitClientInstance
import com.example.demospeechrecognization.retrofit.GetDataService
import com.example.demospeechrecognization.retrofit.RetrofitClientInstanceSpotify
import retrofit2.Callback
import retrofit2.Response

class SessionRepositories {
    lateinit var session:GetSessionModel
    lateinit var thinkThoughtModel: ThinkThoughtModel
    private var language="en"
    var token ="BQD9z7ola75zrLT69tpZxbm5kPjuvz3UJ2xiYsxfhkC5D1bvBEzAak24SuL-LAAGFtl11eiSSHovoU-cVuqmQDkbzZ784imDRjDvsM0D77Kfe4vn2aTRWloxlKLtG018kgtiaxI1kVFENyDrqcIUaHWAWUKwkk6D-tEUXgActJ1Ojp7w9Kjn8wU"

    companion object{
        private var instance: SessionRepositories?=null
        fun getInstance(): SessionRepositories {
            if (instance==null){
                instance= SessionRepositories()
            }
            return instance!!
        }
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
                }else{
                    data.value = ThinkThoughtModel(false,9347, PayloadThink(""), ErrorThink(9347,"busy","My dumb server not respond me"))
                }
            }

            override fun onFailure(call: Call<ThinkThoughtModel>, t: Throwable) {
                data.value = ThinkThoughtModel(false,401, PayloadThink(""), ErrorThink(401,"busy","Server is busy making tea"))
            }
        })
        return data
    }
    fun searchSong(q:String): MutableLiveData<SpotifySearch> {
        val data=MutableLiveData<SpotifySearch>()
        val service: GetDataService = RetrofitClientInstanceSpotify.retrofitInstance()!!.create(
            GetDataService::class.java
        )
        val call  = service.searchSong("Bearer $token",q,"track")
        call.enqueue(object : Callback<SpotifySearch> {

            override fun onResponse(call: Call<SpotifySearch>, response: Response<SpotifySearch>) {
                if (response.body()!=null) {
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<SpotifySearch>, t: Throwable) {
                Log.e("alhaj","error")
            }
        })
        return data
    }

}