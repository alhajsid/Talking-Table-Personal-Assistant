package com.example.demospeechrecognization

import android.Manifest
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import java.util.*
import android.speech.tts.Voice
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import com.example.demospeechrecognization.AI.AIlerner
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.content_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.collections.ArrayList
import android.util.Log as Log1

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    companion object {
        var AppTheme=0
        var contacts = ArrayList<String>()
        var thatnum = ""
        var resultcontact = ArrayList<String>()
        val urlforcreatesession = "https://api.intellivoid.info/coffeehouse/v2/createSession"
        var session_id:String=""
        val apikey="2b3ca346ef86ec29053b3b4db2de22635da2c10885a0e97a71599052fa89c31aa011828e"
        var allasonglist=ArrayList<AudioModel>()
    }

    private var adaptor1 = GroupAdapter<ViewHolder>()
    private lateinit var myTTS: TextToSpeech
    private lateinit var mSpeechRecognizer: SpeechRecognizer
    var mode = ""
    var amount = 0
    val urlmessage="https://api.intellivoid.info/coffeehouse/v2/thinkThought"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_setting.setOnClickListener {
            val intent=Intent(this,SettingActivity()::class.java)
            startActivity(intent)
        }


        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED&&checkSelfPermission(Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED&&checkSelfPermission(Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED&&checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
               maininit()
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE),9)
            }
        }else{

            maininit()
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==9){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED&&grantResults[2]==PackageManager.PERMISSION_GRANTED&&grantResults[3]==PackageManager.PERMISSION_GRANTED)
                maininit()
        }
    }


    fun maininit(){
        try {


            val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE)
            val theme=sharedPresent.getInt("theme",0)
            AppTheme=theme
            if(theme==1){
                contact_main.setBackgroundColor(Color.parseColor("#000000"))
            }else{
                contact_main.setBackgroundColor(Color.parseColor("#ffffff"))
            }

            fab.setOnClickListener { view ->
                Log1.e("fab.onlcicklistner", mode + " " + amount.toString())

                // unmute()
                if (NetworkState.connectionAvailable(this)) {
                    startlistening(786)
                } else {
                    speakforerror("Boss network unavailable")
                }
            }

            intializeTextToSpeech()
            intializeSpeechRecognizer()
            val a = HashSet<String>()
            a.add("male")//here you can give male if you want to select male voice.
            val v = Voice("en-us-x-sfg#male_2-local", Locale("en", "US"), 400, 200, true, a)
            myTTS.voice = v
            myTTS.setSpeechRate(0.8f)
            onInit(1)
            rvconver.adapter = adaptor1
            doAsync{
                startService(Intent(this,mService::class.java))
            }
        }catch (e:Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
        }
    }

    fun Context.toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun startlistening(p: Int) {
        Log1.e("startlistening", "111")
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        // intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        imageButtonicon.setImageDrawable(getDrawable(R.drawable.bubble))
        //startActivity(intent) // Sends the detected query to search
        mSpeechRecognizer.startListening(intent)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        super.onKeyDown(keyCode, event)
        val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE)
        val volumeUpWake=sharedPresent.getInt("volume_wake",0)
        if (volumeUpWake==1){
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                Log1.e("LightWriter", "I WORK BRO.")
                if (NetworkState.connectionAvailable(this)) {
                    startlistening(786)
                } else {
                    speakforerror("Boss network unavailable")
                }
                return true
            }
        }

        return true
    }

    fun moility(command: String) {
        if (command.indexOf("off") != -1) {
            if (command.indexOf("bluetooth") != -1) {

            }
            if (command.indexOf("wi-fi") != -1) {

            }
            if (command.indexOf("flashlight") != -1) {
                val cameraManager = getSystemService (Context.CAMERA_SERVICE) as CameraManager
                try {
                    val cameraId = cameraManager . getCameraIdList ()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, false)
                        log("flashlight","oned")
                    };
                } catch (e:CameraAccessException ) {
                    log("flashlight",e.toString())
                }
            }
        }

        if (command.indexOf("on") != -1) {
            if (command.indexOf("bluetooth") != -1) {

            }
            if (command.indexOf("wi-fi") != -1) {

            }
            if (command.indexOf("flashlight") != -1) {
                log("flashlight","on")
                val cameraManager = getSystemService (Context.CAMERA_SERVICE) as CameraManager
                try {
                    val cameraId = cameraManager . getCameraIdList ()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, true)
                        log("flashlight","oned")
                    };
                } catch (e:CameraAccessException ) {
                    log("flashlight",e.toString())
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == 1) {
            val a = HashSet<String>()
            a.add("male")//here you can give male if you want to select male voice.
            //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
            val v = Voice("en-us-x-sfg#male_2-local", Locale("en", "US"), 400, 200, true, a)
            myTTS.voice = v
            val result1 = myTTS.setLanguage(Locale.US);
            val result = myTTS.setVoice(v)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //val installIntent = Intent()
                //installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                // startActivity(installIntent)
            } else {
            }
        } else {
            Log1.e("TTS", "Initilization Failed!")
        }
    }

    private fun intializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            mSpeechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(bundle: Bundle) {
                }

                override fun onBeginningOfSpeech() {
                }

                override fun onRmsChanged(v: Float) {
                }

                override fun onBufferReceived(bytes: ByteArray) {
                }

                override fun onEndOfSpeech() {
                }

                override fun onError(i: Int) {
                    imageButtonicon.setImageDrawable(getDrawable(R.drawable.mic))

                }

                override fun onResults(bundle: Bundle) {
                    Log1.e("onresult", "--")
                    //getting all the matches
                    val results = bundle.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    )
                    val finalresult = results!!.get(0).toLowerCase()
                    choosingresult(finalresult)
                    amount = amount - 1
                }

                override fun onPartialResults(bundle: Bundle) {
                }

                override fun onEvent(i: Int, bundle: Bundle) {
                }
            })
        }
    }

    fun choosingresult(finalresult: String) {
        //unmute()
        ProcessResult(finalresult)
        imageButtonicon.setImageDrawable(getDrawable(R.drawable.mic))
    }

    private fun ProcessResult(command: String) {

        Log1.e("processresult", "11")

        adaptor1.add(usertext(command, AppTheme))

        rvconver.scrollToPosition(adaptor1.itemCount - 1)

        Log1.d("ajtag1", command)

        var finalresult = AIlerner(this, command)

        if(finalresult.indexOf("pause")!=-1){
            if(command.indexOf("song")!=-1){

            }
        }
        if(command.indexOf("next")!=-1){
            if(command.indexOf("song")!=-1){

            }
        }
        if(command.indexOf("stop ")!=-1){
            if(command.indexOf("song")!=-1){

            }
        }

        if(finalresult.indexOf("play ")!=-1){
            PlaySong(finalresult)
            return
        }

        if(finalresult.startsWith("chatbot")){
            finalresult=finalresult.replace("chatbot","")
            sendMessage(finalresult)
            return
        }

        if (finalresult.startsWith("Turning")) {
            moility(finalresult)
        }

        if (finalresult.startsWith("searchitfriday")) {
            finalresult = finalresult.replace("searchitfriday", "")
            adaptor1.add(searchbyfriday(finalresult, this))
            rvconver.scrollToPosition(adaptor1.itemCount - 1)
            return
        }

        if (finalresult.startsWith("makecallonthat")) {
            PlaceCall(finalresult)
            return
        }
        speak(AIlerner(this, command))
        rvconver.scrollToPosition(adaptor1.itemCount - 1)
    }

    fun PlaySong(s:String){
        var finalresult=s.replace("play ","")
        finalresult=finalresult.replace(" song ","")
        finalresult=finalresult.replace("song ","")
        allasonglist.forEach {
            if(it.aName.toLowerCase().indexOf(finalresult)!=-1){
                val aj=Intent()
                aj.putExtra("songname",finalresult)
                aj.setComponent(ComponentName("com.example.alhaj.mediaplayer","com.example.alhaj.mediaplayer.MyService"))
                startService(aj)
                speakforerror("Playing "+finalresult)
                rvconver.scrollToPosition(adaptor1.itemCount-1)
                return
            }
        }
        speak("No such song found")
        rvconver.scrollToPosition(adaptor1.itemCount-1)

    }

    private fun PlaceCall(finalresult: String) {
        if (finalresult.startsWith("makecallonthat3")) {
            speak("Here is many Contact Chooose which one please.")
            adaptor1.add(contactchoose(this, resultcontact))
            rvconver.scrollToPosition(adaptor1.itemCount - 1)
            return
        } else if (finalresult.startsWith("makecallonthat2")) {
            val finalresult = finalresult.replace("makecallonthat2", "")
            speak("No contacts found for " + finalresult)
        } else {
            val finalresult = finalresult.replace("makecallonthat", "")
            speak("Placing call to" + finalresult)
            Makecall(thatnum)
        }
    }

    fun Makecall(s: String) {
        val callIntent = Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + s));
        val chooser = Intent.createChooser(callIntent, "title")
        startActivity(chooser);
    }

    fun greetingmessages() {

        Log1.e("greetingmeesage", "111")
        val random: Int = (System.currentTimeMillis() % 10).toInt()
        when (random) {
            3 -> greetingspeak("Assala walekum Boss.")
            4 -> greetingspeak("Hey Boss.")
            5 -> greetingspeak("Welcome Boss.")
            else -> {
                val time = DateUtils.formatDateTime(
                    this, Date().time,
                    DateUtils.FORMAT_SHOW_TIME
                )
                var konstant = 0
                var currenthour = 0
                Toast.makeText(this,time.toString(),Toast.LENGTH_SHORT).show()

                for (p in time) {
                    if (konstant == 0) {
                        currenthour = p.toString().toInt()
                    }
                    if (konstant == 1 && p != ':') {
                        currenthour = currenthour * 10 + p.toString().toInt()
                    }
                    konstant = konstant + 1
                }
                Log1.e("houraj", currenthour.toString())
                if(time.indexOf("a.m.")!=-1||time.indexOf("p.m.")!=-1){
                    if (time.indexOf("a.m.") != -1 && currenthour >= 5 && currenthour <= 11) {
                        greetingspeak("Good morning Boss.")
                    } else if (time.indexOf("p.m.") != -1 && currenthour == 12) {
                        greetingspeak("Good afternoon Boss.")
                    } else if (time.indexOf("p.m.") != -1 && currenthour >= 1 && currenthour <= 4) {
                        greetingspeak("Good afternoon Boss.")
                    } else if (time.indexOf("p.m.") != -1 && currenthour >= 5 && currenthour <= 9) {
                        greetingspeak("Good evening Boss.")
                    } else {
                        greetingspeak("Good night Boss.")
                    }
                }
                else if(time.indexOf("AM")!=-1||time.indexOf("PM")!=-1){
                    if (time.indexOf("AM") != -1 && currenthour >= 5 && currenthour <= 11) {
                        greetingspeak("Good morning Boss.")
                    } else if (time.indexOf("PM") != -1 && currenthour == 12) {
                        greetingspeak("Good afternoon Boss.")
                    } else if (time.indexOf("PM") != -1 && currenthour >= 1 && currenthour <= 4) {
                        greetingspeak("Good afternoon Boss.")
                    } else if (time.indexOf("PM") != -1 && currenthour >= 5 && currenthour <= 9) {
                        greetingspeak("Good evening Boss.")
                    } else {
                        greetingspeak("Good night Boss.")
                    }
                }
                else if(time.indexOf("am")!=-1||time.indexOf("pm")!=-1){
                    if (time.indexOf("am") != -1 && currenthour >= 5 && currenthour <= 11) {
                        greetingspeak("Good morning Boss.")
                    } else if (time.indexOf("pm") != -1 && currenthour == 12) {
                        greetingspeak("Good afternoon Boss.")
                    } else if (time.indexOf("pm") != -1 && currenthour >= 1 && currenthour <= 4) {
                        greetingspeak("Good afternoon Boss.")
                    } else if (time.indexOf("pm") != -1 && currenthour >= 5 && currenthour <= 9) {
                        greetingspeak("Good evening Boss.")
                    } else {
                        greetingspeak("Good night Boss.")
                    }
                }


            }
            // else->speak("Jai hind Boss.")
        }
        return
    }

    private fun intializeTextToSpeech() {
        myTTS = TextToSpeech(this, TextToSpeech.OnInitListener() {
            if (myTTS.engines.size == 0) {
                Toast.makeText(this, "your device is not supported", Toast.LENGTH_LONG).show()
                finish()
            } else {
                myTTS.language = Locale.US
                greetingmessages()
            }
        }, "com.google.android.tts")
    }

    private fun speak(message: String) {
        Log1.e("speak", message)
        runOnUiThread {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
            adaptor1.add(jarvistext(message,this))
            rvconver.scrollToPosition(adaptor1.itemCount - 1)
            isTTSSpeaking()
        }
    }

    private fun speakforerror(message: String) {
        Log1.e("speak", message)
        runOnUiThread {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
            adaptor1.add(jarvistext(message,this))
            rvconver.scrollToPosition(adaptor1.itemCount - 1)
        }
    }

    lateinit var runnable: Runnable
    private fun greetingspeak(message: String) {
        myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        adaptor1.add(jarvistext(message,this))
        rvconver.scrollToPosition(adaptor1.itemCount - 1)
        val h = Handler();
        runnable = Runnable() {
            if (!myTTS.isSpeaking()) {
                h.removeCallbacks(runnable)
                log("greetingspeak", "runnable")
                onTTSSpeechFinished();
                return@Runnable
            }
            h.postDelayed(runnable, 1000);
        }
        h.postDelayed(runnable, 3000);
    }

    fun log(a: String, b: String) {
        Log1.e(a, b)
    }


    lateinit var mrunnable: Runnable
    fun isTTSSpeaking() {

        val h = Handler();

        mrunnable = Runnable() {

            if (!myTTS.isSpeaking()) {
                h.removeCallbacks(mrunnable)
                onTTSSpeechFinished();
                Log1.e("isttsspeaking", "")
                return@Runnable
            }
            h.postDelayed(mrunnable, 1000);
        }

        h.postDelayed(mrunnable, 3000);
    }



    fun sendMessage(s:String) {
        if(session_id==""){
            startService(Intent(this,mService::class.java))
            return
        }
        doAsync{
            try{
                var reqParam = URLEncoder.encode("api_key", "UTF-8") + "=" + URLEncoder.encode(apikey, "UTF-8")
                reqParam += "&" + URLEncoder.encode("input", "UTF-8") + "=" + URLEncoder.encode(s)
                reqParam += "&" + URLEncoder.encode("session_id", "UTF-8") + "=" + URLEncoder.encode(session_id, "UTF-8")
                val mURL = URL(urlmessage)

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
                        val gson = Gson()

                        val mMineUserEntity = gson.fromJson(response.toString(), demoobjectmess.upperclass::class.java)
                        println("Response : $response")
                        android.util.Log.e("session id is ",mMineUserEntity!!.payload.output)
                        speak(mMineUserEntity!!.payload.output)
                    }

                }
            }catch (e:Exception){
                Toast.makeText(this,e.message.toString(),Toast.LENGTH_LONG).show()
                speakforerror("Boss network unavailable.")
            }
        }
    }

    fun onTTSSpeechFinished() {
        //Toast.makeText(this,"now end by alhaj",Toast.LENGTH_LONG).show()
        startlistening(0)
    }

    override fun onPause() {
        super.onPause()
        try{
            myTTS.shutdown()
        }catch(e:Exception){

        }
    }

    override fun onRestart() {
        super.onRestart()

        val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE)
        val theme=sharedPresent.getInt("theme",0)
        AppTheme=theme
        adaptor1.clear()
        if(theme==1){
            contact_main.setBackgroundColor(Color.parseColor("#000000"))
        }else{
            contact_main.setBackgroundColor(Color.parseColor("#ffffff"))
        }

        try{
            adaptor1.clear()
            intializeTextToSpeech()
        }catch(e:Exception){
        }
    }

}