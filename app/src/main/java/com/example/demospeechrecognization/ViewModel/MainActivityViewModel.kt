package com.example.demospeechrecognization.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demospeechrecognization.models.*
import com.example.demospeechrecognization.repositories.SessionRepositories

class MainActivityViewModel : ViewModel() {

    var getSessionModel: MutableLiveData<GetSessionModel> = MutableLiveData(GetSessionModel(false,9876,
        Payload("","en",true,9876),Error(345,"dgf","dfg")))
    var createSessionModel: MutableLiveData<GetSessionModel> =  MutableLiveData(GetSessionModel(false,9876,
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
        mIsUpdating.value=(true)
        getSessionModel = mRepo.getSession(api_key,sessionId)
        mIsUpdating.value=(false)
    }

    fun createSession(api_key: String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories().getInstance()
        }
        mIsUpdating.value=(true)
        createSessionModel = mRepo.createSession(api_key)
        mIsUpdating.value=(false)

    }

    fun thinkThought(api_key: String,sessionId:String,input:String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories().getInstance()
        }
        mIsUpdating.value=(true)
        mRepo.thinkThought(api_key,sessionId,input).observeForever {
            thinkThought.value=it
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