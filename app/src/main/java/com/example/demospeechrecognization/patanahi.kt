package com.example.demospeechrecognization

import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import android.util.Log
import com.example.demospeechrecognization.MainActivity.Companion.apikey
import com.example.demospeechrecognization.MainActivity.Companion.session_id
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import android.widget.Toast

class mService : Service() {
    override fun onBind(p0: Intent?): IBinder? {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        sendPostRequest("", "")
        getContacts()
        getAllAudioFromDevice(this)
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    fun getAllAudioFromDevice(context: Context) {

        try{
            val tempAudioList = ArrayList<AudioModel>()
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.ArtistColumns.ARTIST
            )
            val c = context.contentResolver.query(
                uri,
                projection,
                null, null, null
            )

            if (c != null) {
                while (c.moveToNext()) {
                    val audioModel = AudioModel()
                    val path = c.getString(0)
                    val name = c.getString(1)
                    val album = c.getString(2)
                    val artist = c.getString(3)
                    val aj = MediaPlayer()
                    aj.setDataSource(path)
                    aj.prepare()
                    if (aj.duration >= 60000) {

                    }
                    if (aj.duration >= 60000) {
                        audioModel.setaName(name)
                        audioModel.setaAlbum(album)
                        audioModel.setaArtist(artist)
                        audioModel.setaPath(path)
                        tempAudioList.add(audioModel)
                    }
                }
                c.close()
            }
            if (tempAudioList.size != 0) {
                MainActivity.allasonglist = tempAudioList
            }
        }catch (c:Exception){
            c.printStackTrace()
        }



    }

    fun sendPostRequest(userName: String, password: String) {

        doAsync {
            try {
                var mMineUserEntity: demoobject.upperclass? = null
                var reqParam = URLEncoder.encode("api_key", "UTF-8") + "=" + URLEncoder.encode(apikey, "UTF-8")
                reqParam += "&" + URLEncoder.encode("language", "UTF-8") + "=" + URLEncoder.encode("en", "UTF-8")
                val mURL = URL(MainActivity.urlforcreatesession)

                with(mURL.openConnection() as HttpURLConnection) {
                    // optional default is GET
                    requestMethod = "POST"

                    val wr = OutputStreamWriter(getOutputStream());
                    wr.write(reqParam);
                    wr.flush();

                    println("URL : $url")
                    println("Response Code : $responseCode")

                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        var gson = Gson()
                        mMineUserEntity = gson.fromJson(response.toString(), demoobject.upperclass::class.java)
                        println("Response : $response")
                        session_id = mMineUserEntity!!.payload.session_id
                        Log.e("session id is ", mMineUserEntity!!.payload.session_id)

                    }

                }
                if (userName == "1") {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun getContacts() {

        val contactarray = ArrayList<String>()
        doAsync {


            val builder = StringBuilder()

            val resolver: ContentResolver = contentResolver;
            val cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null,
                null
            )

            if (cursor!!.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    )).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null
                        )

                        if (cursorPhone!!.count > 0) {
                            while (cursorPhone.moveToNext()) {
                                var phoneNumValue = cursorPhone.getString(
                                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )
                                builder.append("Contact: ").append(name).append(", Phone Number: ").append(
                                    phoneNumValue
                                ).append("\n\n")
                                phoneNumValue = phoneNumValue.replace("+91", "")
                                phoneNumValue = phoneNumValue.replace(" ", "")
                                phoneNumValue = phoneNumValue.replace("-", "")

                                contactarray.add(name)
                                contactarray.add(phoneNumValue)
                                //  Log.e("Name ===>",phoneNumValue);
                            }
                        }


                        cursorPhone.close()
                    }
                }
            } else {
                //   toast("No contacts available!")
            }
            cursor.close()
        }

        MainActivity.contacts = contactarray
    }

}

var volumePrev = 0

private val broadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if ("android.media.VOLUME_CHANGED_ACTION" == intent.action) {

            val volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0)

            Log.i(TAG, "volume = $volume")

            if (volumePrev < volume) {
                Log.e(TAG, "You have pressed volume up button")
            } else {
                Log.e(TAG, "You have pressed volume down button")
            }
            volumePrev = volume
        }
    }
}



