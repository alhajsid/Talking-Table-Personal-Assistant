package com.example.demospeechrecognization

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.layoutchoosewhichcontact.view.*
import kotlinx.android.synthetic.main.layoutjarvusbubble.view.*
import kotlinx.android.synthetic.main.layoutrvcontact.view.*
import kotlinx.android.synthetic.main.searchlayout.view.*
import kotlinx.android.synthetic.main.usertextlayout_light_theme.view.*


class demoobject{
    data class upperclass(
        val status:Boolean,
        val code:Int,
        val payload:lowerclass,
        val ref_code:String
    )
    data class lowerclass(
        val session_id:String,
        val  language:String,
        val available:Boolean,
        val expires:Int
    )
}

class demoobjectmess{
    data class upperclass(
        val status:Boolean,
        val code:Int,
        val payload:lowerclass,
        val ref_code:String
    )
    data class lowerclass(
        val output:String
    )
}


