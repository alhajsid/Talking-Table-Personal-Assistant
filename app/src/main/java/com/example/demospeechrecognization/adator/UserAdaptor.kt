package com.example.demospeechrecognization.adator

import com.example.demospeechrecognization.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.usertextlayout_light_theme.view.*


class UserAdaptor(val text: String, val theme:Int) : Item<ViewHolder>() {
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