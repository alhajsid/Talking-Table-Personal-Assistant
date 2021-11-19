package com.example.demospeechrecognization.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demospeechrecognization.models.*
import com.example.demospeechrecognization.repositories.SessionRepositories
import com.example.demospeechrecognization.utils.BaseResponse

class MainActivityViewModel : ViewModel() {

    var getSessionModel: MutableLiveData<GetSessionModel> = MutableLiveData()

    var createSessionModel: MutableLiveData<GetSessionModel> =  MutableLiveData()

    var thinkThought: MutableLiveData<ThinkThoughtModel> = MutableLiveData()

    var brainShopResponse: MutableLiveData<BaseResponse<BrainShopResponse>> = MutableLiveData()

    val mRepo = SessionRepositories().getInstance()

    private var mIsUpdating = MutableLiveData(false)

    fun getSession(api_key: String,sessionId:String) {
        mIsUpdating.value=(true)
        getSessionModel = mRepo.getSession(api_key,sessionId)
        mIsUpdating.value=(false)
    }

    fun createSession(api_key: String) {
        mIsUpdating.value=(true)
        createSessionModel = mRepo.createSession(api_key)
        mIsUpdating.value=(false)

    }

    fun thinkThought(api_key: String,sessionId:String,input:String) {
        mIsUpdating.value=(true)
        mRepo.thinkThought(api_key,sessionId,input).observeForever {
            thinkThought.value=it
            mIsUpdating.value=(false)
        }
    }

    fun get(bid:String,uid:String,key:String,msg:String) {
        mIsUpdating.value=(true)
        mRepo.get(bid,uid, key, msg).observeForever {
            brainShopResponse.value=it
            mIsUpdating.value=(false)
        }

    }

    fun getSessionModel(): LiveData<GetSessionModel> {
        return getSessionModel
    }

    fun getCreateSessionModel(): LiveData<GetSessionModel> {
        return createSessionModel
    }

    fun getThinkThouModel(): LiveData<ThinkThoughtModel> {
        return thinkThought
    }

    fun getIsLoading(): LiveData<Boolean> {
        return mIsUpdating
    }

}