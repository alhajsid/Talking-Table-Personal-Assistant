package com.example.demospeechrecognization.adator

import android.content.Context
import android.webkit.WebSettings
import com.example.demospeechrecognization.R
import com.example.demospeechrecognization.utils.MyWebViewClient
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.searchlayout.view.*


class SearchGoogleAdaptor(val text: String, val context: Context) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.webveiwsearch.webViewClient = MyWebViewClient(context)
        viewHolder.itemView.webveiwsearch.loadUrl("https://www.google.com/search?q=" + text)
        viewHolder.itemView.webveiwsearch.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        //viewHolder.itemView.webveiwsearch.scrollTo(0,400)
    }

    override fun getLayout(): Int {
        return R.layout.searchlayout
    }

}