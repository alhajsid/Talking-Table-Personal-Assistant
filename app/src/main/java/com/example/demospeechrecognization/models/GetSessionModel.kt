package com.example.demospeechrecognization.models

data class GetSessionModel(val success:Boolean, val response_code:Int, val results:Payload, val error:Error)

data class Payload(val session_id:String,val language:String,val available:Boolean,val expires:Int)

data class Error(val error_code:Int,val type:String,val message:String)

//{
//    "success": true,
//    "response_code": 200,
//    "payload": {
//    "session_id": "46db504f80a9eadd0fbec43ff4f75690962242b5c57fb23f6e4f2a23423bcab8",
//    "language": "en",
//    "available": true,
//    "expires": 1581899765
//}
//}
//
//{
//    "success": false,
//    "response_code": 404,
//    "error": {
//    "error_code": 4,
//    "type": "CLIENT",
//    "message": "The session was not found"
//}
//}