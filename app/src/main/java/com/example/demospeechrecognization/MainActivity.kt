package com.example.demospeechrecognization

import android.Manifest
import android.content.ComponentName
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
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.demospeechrecognization.farziai.Ailearning
import com.example.demospeechrecognization.ViewModel.MainActivityViewModel
import com.example.demospeechrecognization.adator.ContactAdaptor
import com.example.demospeechrecognization.adator.JarvisAdaptor
import com.example.demospeechrecognization.adator.SearchGoogleAdaptor
import com.example.demospeechrecognization.adator.UserAdaptor
import com.example.demospeechrecognization.models.AudioModel
import com.example.demospeechrecognization.models.GetSessionModel
import com.example.demospeechrecognization.models.ThinkThoughtModel
import com.example.demospeechrecognization.services.MainService
import com.example.demospeechrecognization.utils.SharedPref
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.content_main.*
import kotlin.collections.ArrayList
import android.util.Log as Log1

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    companion object {
        var AppTheme = 0
        var contacts = ArrayList<String>()
        var contactNumber = ""
        var contactList = ArrayList<String>()
        var sessionId: String = ""
        var localSongsList = ArrayList<AudioModel>()
    }

    private val accessKey="4f8c34291fc7d0ca9902584930f9c3e29c35c5c9a48167e15db68f994c4492ea1fdaa9999e69c328dca2d23e6fd4114e336012f4beeed183d0a7bdf35317ddfb"
    lateinit var sharedPref: SharedPref
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var adaptorMain = GroupAdapter<ViewHolder>()
    private lateinit var myTTS: TextToSpeech
    private lateinit var mSpeechRecognizer: SpeechRecognizer
    private var mode = ""
    var amount = 0
//    val urlmessage = "https://api.intellivoid.info/coffeehouse/v2/thinkThought"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity()::class.java)
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mainInit()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    9
                )
            }
        } else {
            mainInit()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 9) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED)
                mainInit()
        }
    }
    
    private fun checkSession(){

        sharedPref= SharedPref(this)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        sessionId=sharedPref.getString(sharedPref.SESSION)
        if (sessionId==""){
            mainActivityViewModel.createSession(accessKey)
        }else{
            mainActivityViewModel.getSession(accessKey,sessionId)
        }
    }

    private fun mainInit() {
        try {
            
            checkSession()

            mainActivityViewModel.getCreateSessionModel().observe(this,
                Observer<GetSessionModel> {
                    if (it.success && it.response_code==200){
                        sessionId=it.results.session_id
                        sharedPref.setString(sharedPref.SESSION,it.results.session_id)
                    }else if(it.response_code!=9876){
                        Toast.makeText(this, "error ${it.error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
//
            mainActivityViewModel.getSessionModel().observe(this,
                Observer<GetSessionModel> {
                    if (it.success && it.response_code==200){
                        sessionId=it.results.session_id
                        sharedPref.setString(sharedPref.SESSION,it.results.session_id)
                    }else if(it.response_code!=9876){
                        mainActivityViewModel.createSession(accessKey)
                        Toast.makeText(this, "error ${it.error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
//
            mainActivityViewModel.getThinkThouModel().observe(this,
                Observer<ThinkThoughtModel> {
                    if (it.success && it.response_code==200){
                        speak(it.results.output)
                    }else if(it.response_code!=9876){
                        Toast.makeText(this, "error ${it.error.message}", Toast.LENGTH_SHORT).show()
                    }
                })

            mainActivityViewModel.getIsLoading().observe(this,
                Observer<Boolean> {
                    if (it){
                        fab.isClickable=false
                        txt_thinking.visibility= View.VISIBLE
                    }
                    else {
                        fab.isClickable=true
                        txt_thinking.visibility= View.GONE
                    }
                })

            val sharedPresent = getSharedPreferences("setting", Context.MODE_PRIVATE)
            val theme = sharedPresent.getInt("theme", 0)
            AppTheme = theme
            if (theme == 1) {
                contact_main.setBackgroundColor(Color.parseColor("#000000"))
            } else {
                contact_main.setBackgroundColor(Color.parseColor("#ffffff"))
            }

            fab.setOnClickListener {
                Log1.e("fab.onlcicklistner", "$mode $amount")

                // unmute()
                if (NetworkState.connectionAvailable(this)) {
                    startListening()
                } else {
                    speakError("Boss network unavailable")
                }
            }

            initializeTextToSpeech()
            initializeSpeechRecognizer()
            val a = HashSet<String>()
            a.add("male")//here you can give male if you want to select male voice.
            val v = Voice("en-us-x-sfg#male_2-local", Locale("en", "US"), 400, 200, true, a)
            myTTS.voice = v
            myTTS.setSpeechRate(0.8f)
            onInit(1)
            rvconver.adapter = adaptorMain
            startService(Intent(this, MainService::class.java))

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startListening() {
        Log1.e("startlistening", "111")
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        // intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        imageButtonicon.setImageDrawable(getDrawable(R.drawable.bubble))
        //startActivity(intent) // Sends the detected query to search
        mSpeechRecognizer.startListening(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        super.onKeyDown(keyCode, event)
        val sharedPresent = getSharedPreferences("setting", Context.MODE_PRIVATE)
        val volumeUpWake = sharedPresent.getInt("volume_wake", 0)
        if (volumeUpWake == 1) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                Log1.e("LightWriter", "I WORK BRO.")
                if (NetworkState.connectionAvailable(this)) {
                    startListening()
                } else {
                    speakError("Boss network unavailable")
                }
                return true
            }
        }
        return true
    }

    private fun moibility(command: String) {
        if (command.indexOf("off") != -1) {
//            if (command.indexOf("bluetooth") != -1) {
//
//            }
//            if (command.indexOf("wi-fi") != -1) {
//
//            }
            if (command.indexOf("flashlight") != -1) {
                val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, false)
                        log("flashlight", "oned")
                    }
                } catch (e: CameraAccessException) {
                    log("flashlight", e.toString())
                }
            }
        }

        if (command.indexOf("on") != -1) {
//            if (command.indexOf("bluetooth") != -1) {
//
//            }
//            if (command.indexOf("wi-fi") != -1) {
//
//            }
            if (command.indexOf("flashlight") != -1) {
                log("flashlight", "on")
                val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, true)
                        log("flashlight", "oned")
                    }
                } catch (e: CameraAccessException) {
                    log("flashlight", e.toString())
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
//            val result1 = myTTS.setLanguage(Locale.US);
//            val result = myTTS.setVoice(v)
//            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //val installIntent = Intent()
                //installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                // startActivity(installIntent)
//            }
        } else {
            Log1.e("TTS", "Initilization Failed!")
        }
    }

    private fun initializeSpeechRecognizer() {
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
                    val finalresult = results!![0].toLowerCase(Locale.getDefault())
                    choosingResult(finalresult)
                    amount -= 1
                }

                override fun onPartialResults(bundle: Bundle) {
                }

                override fun onEvent(i: Int, bundle: Bundle) {
                }
            })
        }
    }

    fun choosingResult(finalResult: String) {
        //unmute()
        processResult(finalResult)
        imageButtonicon.setImageDrawable(getDrawable(R.drawable.mic))
    }

    private fun processResult(command: String) {

        Log1.e("processresult", "11")

        adaptorMain.add(UserAdaptor(command, AppTheme))

        rvconver.scrollToPosition(adaptorMain.itemCount - 1)

        Log1.d("ajtag1", command)

        var finalResult = Ailearning(this, command)

//        if (finalresult.indexOf("pause") != -1) {
//            if (command.indexOf("song") != -1) {
//
//            }
//        }
//        if (command.indexOf("next") != -1) {
//            if (command.indexOf("song") != -1) {
//
//            }
//        }
//        if (command.indexOf("stop ") != -1) {
//            if (command.indexOf("song") != -1) {
//
//            }
//        }

        if (finalResult.indexOf("play ") != -1) {
            playSong(finalResult)
            return
        }

        if (finalResult.startsWith("chatbot")) {
            finalResult = finalResult.replace("chatbot", "")
            sendMessage(finalResult)
            return
        }

        if (finalResult.startsWith("Turning")) {
            moibility(finalResult)
        }

        if (finalResult.startsWith("searchitfriday")) {
            finalResult = finalResult.replace("searchitfriday", "")
            adaptorMain.add(SearchGoogleAdaptor(finalResult, this))
            rvconver.scrollToPosition(adaptorMain.itemCount - 1)
            return
        }

        if (finalResult.startsWith("makecallonthat")) {
            placeCall(finalResult)
            return
        }
        speak(Ailearning(this, command))
        rvconver.scrollToPosition(adaptorMain.itemCount - 1)
    }

    private fun playSong(s: String) {
        var finalresult = s.replace("play ", "")
        finalresult = finalresult.replace(" song ", "")
        finalresult = finalresult.replace("song ", "")
        localSongsList.forEach {
            if (it.aName.toLowerCase(Locale.getDefault()).indexOf(finalresult) != -1) {
                val aj = Intent()
                aj.putExtra("songname", finalresult)
                aj.component = ComponentName(
                    "com.example.alhaj.mediaplayer",
                    "com.example.alhaj.mediaplayer.MyService"
                )
                startService(aj)
                speakError("Playing $finalresult")
                rvconver.scrollToPosition(adaptorMain.itemCount - 1)
                return
            }
        }
        speak("No such song found")
        rvconver.scrollToPosition(adaptorMain.itemCount - 1)
    }

    private fun placeCall(finalresult: String) {
        when {
            finalresult.startsWith("makecallonthat3") -> {
                speak("Here is many Contact Chooose which one please.")
                adaptorMain.add(ContactAdaptor(this, contactList))
                rvconver.scrollToPosition(adaptorMain.itemCount - 1)
                return
            }
            finalresult.startsWith("makecallonthat2") -> {
                val noContactResult = finalresult.replace("makecallonthat2", "")
                speak("No contacts found for $noContactResult")
            }
            else -> {
                val findNumResult = finalresult.replace("makecallonthat", "")
                speak("Placing call to$findNumResult")
                makeCall(contactNumber)
            }
        }
    }

    private fun makeCall(s: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$s")
        val chooser = Intent.createChooser(callIntent, "title")
        startActivity(chooser)
    }

    private fun greetingMessage() {

        Log1.e("greetingmeesage", "111")
        when ((System.currentTimeMillis() % 10).toInt()) {
            3 -> speakGreeting("Assala walekum Boss.")
            4 -> speakGreeting("Hey Boss.")
            5 -> speakGreeting("Welcome Boss.")
            else -> {
                val time = DateUtils.formatDateTime(
                    this, Date().time,
                    DateUtils.FORMAT_SHOW_TIME
                )
                var konstant = 0
                var currenthour = 0
//                Toast.makeText(this, time.toString(), Toast.LENGTH_SHORT).show()

                for (p in time) {
                    if (konstant == 0) {
                        currenthour = p.toString().toInt()
                    }
                    if (konstant == 1 && p != ':') {
                        currenthour = currenthour * 10 + p.toString().toInt()
                    }
                    konstant += 1
                }
                Log1.e("houraj", currenthour.toString())
                if (time.indexOf("a.m.") != -1 || time.indexOf("p.m.") != -1) {
                    if (time.indexOf("a.m.") != -1 && currenthour >= 5 && currenthour <= 11) {
                        speakGreeting("Good morning Boss.")
                    } else if (time.indexOf("p.m.") != -1 && currenthour == 12) {
                        speakGreeting("Good afternoon Boss.")
                    } else if (time.indexOf("p.m.") != -1 && currenthour >= 1 && currenthour <= 4) {
                        speakGreeting("Good afternoon Boss.")
                    } else if (time.indexOf("p.m.") != -1 && currenthour >= 5 && currenthour <= 9) {
                        speakGreeting("Good evening Boss.")
                    } else {
                        speakGreeting("Good night Boss.")
                    }
                } else if (time.indexOf("AM") != -1 || time.indexOf("PM") != -1) {
                    if (time.indexOf("AM") != -1 && currenthour >= 5 && currenthour <= 11) {
                        speakGreeting("Good morning Boss.")
                    } else if (time.indexOf("PM") != -1 && currenthour == 12) {
                        speakGreeting("Good afternoon Boss.")
                    } else if (time.indexOf("PM") != -1 && currenthour >= 1 && currenthour <= 4) {
                        speakGreeting("Good afternoon Boss.")
                    } else if (time.indexOf("PM") != -1 && currenthour >= 5 && currenthour <= 9) {
                        speakGreeting("Good evening Boss.")
                    } else {
                        speakGreeting("Good night Boss.")
                    }
                } else if (time.indexOf("am") != -1 || time.indexOf("pm") != -1) {
                    if (time.indexOf("am") != -1 && currenthour >= 5 && currenthour <= 11) {
                        speakGreeting("Good morning Boss.")
                    } else if (time.indexOf("pm") != -1 && currenthour == 12) {
                        speakGreeting("Good afternoon Boss.")
                    } else if (time.indexOf("pm") != -1 && currenthour >= 1 && currenthour <= 4) {
                        speakGreeting("Good afternoon Boss.")
                    } else if (time.indexOf("pm") != -1 && currenthour >= 5 && currenthour <= 9) {
                        speakGreeting("Good evening Boss.")
                    } else {
                        speakGreeting("Good night Boss.")
                    }
                }
            }
            // else->speak("Jai hind Boss.")
        }
        return
    }

    private fun initializeTextToSpeech() {
        myTTS = TextToSpeech(this, TextToSpeech.OnInitListener {
            if (myTTS.engines.size == 0) {
                Toast.makeText(this, "your device is not supported", Toast.LENGTH_LONG).show()
                finish()
            } else {
                myTTS.language = Locale.US
                greetingMessage()
            }
        }, "com.google.android.tts")
    }

    private fun speak(message: String) {
        Log1.e("speak", message)
        runOnUiThread {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
            adaptorMain.add(JarvisAdaptor(message, this))
            rvconver.scrollToPosition(adaptorMain.itemCount - 1)
            isTTSSpeaking()
        }
    }

    private fun speakError(message: String) {
        Log1.e("speak", message)
        runOnUiThread {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
            adaptorMain.add(JarvisAdaptor(message, this))
            rvconver.scrollToPosition(adaptorMain.itemCount - 1)
        }
    }

    private lateinit var runnable: Runnable
    private fun speakGreeting(message: String) {
        myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        adaptorMain.add(JarvisAdaptor(message, this))
        rvconver.scrollToPosition(adaptorMain.itemCount - 1)
        val h = Handler()
        runnable = Runnable {
            if (!myTTS.isSpeaking) {
                h.removeCallbacks(runnable)
                log("greetingspeak", "runnable")
                onTTSSpeechFinished()
                return@Runnable
            }
            h.postDelayed(runnable, 1000)
        }
        h.postDelayed(runnable, 3000)
    }

    private fun log(a: String, b: String) {
        Log1.e(a, b)
    }

    private lateinit var mrunnable: Runnable

    private fun isTTSSpeaking() {

        val h = Handler()

        mrunnable = Runnable {

            if (!myTTS.isSpeaking) {
                h.removeCallbacks(mrunnable)
                onTTSSpeechFinished()
                Log1.e("isttsspeaking", "")
                return@Runnable
            }
            h.postDelayed(mrunnable, 1000)
        }

        h.postDelayed(mrunnable, 3000)
    }

    private fun sendMessage(s: String) {
        if (sessionId == "") {
            checkSession()
            return
        }
        mainActivityViewModel.thinkThought(accessKey, sessionId,s)
    }

    private fun onTTSSpeechFinished() {
        startListening()
    }

    override fun onPause() {
        super.onPause()
        try {
            myTTS.shutdown()
        } catch (e: Exception) {

        }
    }

    override fun onRestart() {
        super.onRestart()

        val sharedPresent = getSharedPreferences("setting", Context.MODE_PRIVATE)
        val theme = sharedPresent.getInt("theme", 0)
        AppTheme = theme
        adaptorMain.clear()

        if (theme == 1) {
            contact_main.setBackgroundColor(Color.parseColor("#000000"))
        } else {
            contact_main.setBackgroundColor(Color.parseColor("#ffffff"))
        }

        try {
            adaptorMain.clear()
            initializeTextToSpeech()
        } catch (e: Exception) {
        }
    }

}