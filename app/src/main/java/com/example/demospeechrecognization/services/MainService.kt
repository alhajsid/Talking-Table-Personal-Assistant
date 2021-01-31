package com.example.demospeechrecognization.services

import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import com.example.demospeechrecognization.MainActivity
import com.example.demospeechrecognization.models.AudioModel
import com.example.demospeechrecognization.utils.AsyncClass

class MainService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getContacts()
        getAllAudioFromDevice(this)
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getAllAudioFromDevice(context: Context) {
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
                MainActivity.localSongsList = tempAudioList
            }
        }catch (c:Exception){
            c.printStackTrace()
        }
    }

//    fun sendPostRequest() {
//        AsyncClass {
//            try {
//                var mMineUserEntity: demoobject.upperclass?
//                var reqParam = URLEncoder.encode("api_key", "UTF-8") + "=" + URLEncoder.encode(apikey, "UTF-8")
//                reqParam += "&" + URLEncoder.encode("language", "UTF-8") + "=" + URLEncoder.encode("en", "UTF-8")
//                val mURL = URL(MainActivity.urlforcreatesession)
//
//                with(mURL.openConnection() as HttpURLConnection) {
//                    // optional default is GET
//                    requestMethod = "POST"
//
//                    val wr = OutputStreamWriter(getOutputStream());
//                    wr.write(reqParam);
//                    wr.flush();
//
//                    println("URL : $url")
//                    println("Response Code : $responseCode")
//
//                    BufferedReader(InputStreamReader(inputStream)).use {
//                        val response = StringBuffer()
//
//                        var inputLine = it.readLine()
//                        while (inputLine != null) {
//                            response.append(inputLine)
//                            inputLine = it.readLine()
//                        }
//                        it.close()
//                        val gson = Gson()
//                        mMineUserEntity = gson.fromJson(response.toString(), demoobject.upperclass::class.java)
//                        println("Response : $response")
//                        session_id = mMineUserEntity!!.payload.session_id
//                        Log.e("session id is ", mMineUserEntity!!.payload.session_id)
//
//                    }
//
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    private fun getContacts() {

        val contactList = ArrayList<String>()

        AsyncClass {

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

                                contactList.add(name)
                                contactList.add(phoneNumValue)
                            }
                        }
                        cursorPhone.close()
                    }
                }
            }
            cursor.close()
        }
        MainActivity.contacts = contactList
    }

}