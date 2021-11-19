package com.example.demospeechrecognization.utils

data class BaseResponse<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): BaseResponse<T> {
            return BaseResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): BaseResponse<T> {
            return BaseResponse(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): BaseResponse<T> {
            return BaseResponse(Status.LOADING, data, null)
        }

        fun <T> failed(msg: String, data: T?): BaseResponse<T> {
            return BaseResponse(Status.FAILED, data, msg)
        }
    }
}


enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    FAILED
}