package com.example.demospeechrecognization.adator

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.demospeechrecognization.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.layoutchoosewhichcontact.view.*


class ContactAdaptor(val contxt: Context, val text: ArrayList<String>) : Item<ViewHolder>() {

    @SuppressLint("MissingPermission")
    override fun bind(viewHolder: ViewHolder, position: Int) {

        val adaptor2 = GroupAdapter<ViewHolder>()
        viewHolder.itemView.rvcontacts1.adapter = adaptor2

        val mapcontact = HashMap<String, String>()
        for (i in 0 until text.size) {
            Log.e("ajcontacitem ", text[i])
            if ((i % 2) == 0) {
                mapcontact.put(text[i + 1], text[i])
            }
        }

//        val makecalllo = ArrayList<String>()

        mapcontact.forEach {
            adaptor2.add(MultiContactAdaptor(it.value, it.key))
        }

        adaptor2.setOnItemClickListener { item, view ->
            val userItem = item as MultiContactAdaptor
            userItem.phone
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + userItem.phone)
            val chooser = Intent.createChooser(callIntent, "title")
            contxt.startActivity(chooser)
        }

    }

    override fun getLayout(): Int {
        return R.layout.layoutchoosewhichcontact
    }

}