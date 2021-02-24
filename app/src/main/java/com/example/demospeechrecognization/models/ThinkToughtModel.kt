package com.example.demospeechrecognization.models

data class ThinkThoughtModel(val success:Boolean,val response_code:Int,val results:PayloadThink,val error:ErrorThink)

class PayloadThink(val output:String)

class ErrorThink(val error_code:Int,val type:String,val message:String)



//{
//    "success": true,
//    "response_code": 200,
//    "payload": {
//    "output": "Hey how are you?"
//}
//}

//{
//    "success": false,
//    "response_code": 404,
//    "error": {
//    "error_code": 4,
//    "type": "CLIENT",
//    "message": "The session was not found"
//}
//}