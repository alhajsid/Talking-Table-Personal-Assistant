package com.example.demospeechrecognization.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demospeechrecognization.models.*
import com.example.demospeechrecognization.repositories.SessionRepositories
import com.example.demospeechrecognization.utils.BaseResponse

class MainActivityViewModel : ViewModel() {

    var brainShopResponse: MutableLiveData<BaseResponse<BrainShopResponse>> = MutableLiveData()

    val mRepo = SessionRepositories

    private var mIsUpdating = MutableLiveData(false)

    fun get(bid:String,uid:String,key:String,msg:String) {
        mIsUpdating.value=(true)
        mRepo.get(bid,uid, key, msg).observeForever {
            brainShopResponse.value=it
            mIsUpdating.value=(false)
        }

    }

    fun getIsLoading(): LiveData<Boolean> {
        return mIsUpdating
    }

}