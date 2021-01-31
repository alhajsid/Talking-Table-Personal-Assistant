package com.example.demospeechrecognization.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demospeechrecognization.models.*
import com.example.demospeechrecognization.repositories.SessionRepositories

class MainActivityViewModel : ViewModel() {

    var getSessionModel: MutableLiveData<GetSessionModel> = MutableLiveData(GetSessionModel(false,400,
        Payload("","en",true,9876),Error(345,"dgf","dfg")))
    var createSessionModel: MutableLiveData<GetSessionModel> =  MutableLiveData(GetSessionModel(false,400,
        Payload("","en",true,9876),Error(345,"df","gf")))
    var thinkThought: MutableLiveData<ThinkThoughtModel> = MutableLiveData(ThinkThoughtModel(false,9876,
        PayloadThink(""),ErrorThink(34,"sdf","sef")
    ))
    lateinit var mRepo: SessionRepositories
    private var mIsUpdating = MutableLiveData(false)

    fun getSession(api_key: String,sessionId:String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories().getInstance()
        }
        mIsUpdating.postValue(true)
        getSessionModel = mRepo.getSession(api_key,sessionId)
        mIsUpdating.postValue(false)
    }

    fun createSession(api_key: String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories().getInstance()
        }
        mIsUpdating.postValue(true)
        createSessionModel = mRepo.createSession(api_key)
        mIsUpdating.postValue(false)

    }

    fun thinkThought(api_key: String,sessionId:String,input:String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories().getInstance()
        }
        mIsUpdating.postValue(true)
        thinkThought = mRepo.thinkThought(api_key,sessionId,input)
        mIsUpdating.postValue(false)

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

    fun getIsLoading(): MutableLiveData<Boolean>? {
        return mIsUpdating
    }

}