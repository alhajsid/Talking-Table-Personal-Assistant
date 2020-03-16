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

class AudioModel {
    internal var aPath: String=""
    internal var aName: String=""
    internal var aAlbum: String=""
    internal var aArtist: String=""
    internal var time: String=""

    fun getaPath(): String {
        return aPath
    }

    fun setabitmap(bitmap: String) {
        this.time = bitmap
    }

    fun setaPath(aPath: String) {
        this.aPath = aPath
    }

    fun getaName(): String {
        return aName
    }

    fun setaName(aName: String) {
        this.aName = aName
    }

    fun getaAlbum(): String {
        return aAlbum
    }

    fun setaAlbum(aAlbum: String) {
        this.aAlbum = aAlbum
    }

    fun getaArtist(): String {
        return aArtist
    }

    fun setaArtist(aArtist: String) {
        this.aArtist = aArtist
    }
}

class jarvistext(val text: String,val context: Context) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvjarvistext.text=text

    }

    override fun getLayout(): Int {

            return R.layout.layoutjarvusbubble

    }
}

class usertext(val text: String,val theme:Int) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvusertext.text = text

    }

    override fun getLayout(): Int {

        if(theme==0){

            return R.layout.usertextlayout_light_theme
        }else{

            return R.layout.usertextlayout_dark_theme
        }

    }
}

class contactchoose(val contxt: Context, val text: ArrayList<String>) : Item<ViewHolder>() {
    @SuppressLint("MissingPermission")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        var adaptor2 = GroupAdapter<ViewHolder>()
        viewHolder.itemView.rvcontacts1.adapter = adaptor2


        val mapcontact = HashMap<String, String>()
        for (i in 0..text.size - 1) {
            Log.e("ajcontacitem ", text[i])

            if ((i % 2) == 0) {

                mapcontact.put(text[i + 1], text[i])
            }
        }


        val makecalllo = ArrayList<String>()

        mapcontact.forEach {
            adaptor2.add(contactchooseitem(it.value, it.key))
        }

        adaptor2.setOnItemClickListener { item, view ->

            val userItem = item as contactchooseitem
            userItem.phone

            val callIntent = Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + userItem.phone));
            val chooser = Intent.createChooser(callIntent, "title")
            contxt.startActivity(chooser);
        }
    }

    override fun getLayout(): Int {

        return R.layout.layoutchoosewhichcontact
    }
}

class contactchooseitem(val name: String, val phone: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvchoosecontactname.text = name
        viewHolder.itemView.tvchoosecontactnum.text = phone

    }

    override fun getLayout(): Int {

        return R.layout.layoutrvcontact
    }
}

class searchbyfriday(val text: String, val context: Context) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.webveiwsearch.webViewClient = mywebClient2(context)
        viewHolder.itemView.webveiwsearch.loadUrl("https://www.google.com/search?q=" + text)
        viewHolder.itemView.webveiwsearch.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        //viewHolder.itemView.webveiwsearch.scrollTo(0,400)
    }

    override fun getLayout(): Int {

        return R.layout.searchlayout
    }
}

class mywebClient2(context: Context) : WebViewClient() {
    val context = context
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view?.loadUrl(request?.url.toString())
        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url)
        return true
    }


    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view!!.scrollTo(0, 500)
    }
}

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    init {
        execute()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}
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


