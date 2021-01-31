package com.example.demospeechrecognization.adator

import android.content.Context
import com.example.demospeechrecognization.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.layoutjarvusbubble.view.*


class JarvisAdaptor(val text: String,val context: Context) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvjarvistext.text=text

    }

    override fun getLayout(): Int {

        return R.layout.layoutjarvusbubble

    }
}