package com.example.demospeechrecognization.adator

import com.example.demospeechrecognization.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.layoutrvcontact.view.*


class MultiContactAdaptor(val name: String, val phone: String) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvchoosecontactname.text = name
        viewHolder.itemView.tvchoosecontactnum.text = phone
    }

    override fun getLayout(): Int {
        return R.layout.layoutrvcontact
    }

}