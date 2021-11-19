package com.example.demospeechrecognization.adator

import android.content.Context
import android.os.Build
import android.text.Html
import com.example.demospeechrecognization.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.layoutjarvusbubble.view.*

class JarvisAdaptor(val text: String,val context: Context) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewHolder.itemView.tvjarvistext.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            viewHolder.itemView.tvjarvistext.text = Html.fromHtml(text);
        }
    }

    override fun getLayout(): Int {
        return R.layout.layoutjarvusbubble
    }

}