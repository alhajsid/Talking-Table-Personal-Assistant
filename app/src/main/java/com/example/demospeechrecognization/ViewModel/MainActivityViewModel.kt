package com.example.demospeechrecognization.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demospeechrecognization.models.*
import com.example.demospeechrecognization.repositories.SessionRepositories
import com.example.demospeechrecognization.utils.Resource

class MainActivityViewModel : ViewModel() {

    var getSessionModel: MutableLiveData<GetSessionModel> = MutableLiveData()
    var createSessionModel: MutableLiveData<GetSessionModel> =  MutableLiveData()
    var thinkThought: MutableLiveData<ThinkThoughtModel> = MutableLiveData()
    var spotifySearch: MutableLiveData<Resource<SpotifySearch>> = MutableLiveData()

    lateinit var mRepo: SessionRepositories
    private var mIsUpdating = MutableLiveData(false)

    fun getSession(api_key: String,sessionId:String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories.getInstance()
        }
        mIsUpdating.value=(true)
        getSessionModel = mRepo.getSession(api_key,sessionId)
        mIsUpdating.value=(false)
    }

    fun createSession(api_key: String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories.getInstance()
        }
        mIsUpdating.value=(true)
        createSessionModel = mRepo.createSession(api_key)
        mIsUpdating.value=(false)

    }

    fun thinkThought(api_key: String,sessionId:String,input:String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories.getInstance()
        }
        mIsUpdating.value=(true)
        mRepo.thinkThought(api_key,sessionId,input).observeForever {
            thinkThought.value=it
            mIsUpdating.value=(false)
        }

    }
    fun searchSpotify(q:String) {
        if (!this::mRepo.isInitialized) {
            mRepo = SessionRepositories.getInstance()
        }
        mIsUpdating.value=(true)
        mRepo.searchSong(q).observeForever {
            spotifySearch.value=it
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

    fun getSpotifySearch(): LiveData<Resource<SpotifySearch>> {
        return spotifySearch
    }

}