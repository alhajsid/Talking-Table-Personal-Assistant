package com.example.demospeechrecognization.farziai

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import com.example.demospeechrecognization.MainActivity
import java.util.*
import kotlin.collections.HashMap

fun Ailearning(context: Context, command: String): String {

    if(command.indexOf("play ")!=-1){
        return command
    }
    if(command.indexOf("pause")!=-1){
        if(command.indexOf("song")!=-1){
            return command
        }
    }
    if(command.indexOf("next")!=-1){
        if(command.indexOf("song")!=-1){
            return command
        }
    }
    if(command.indexOf("stop ")!=-1){
        if(command.indexOf("song")!=-1){
            return command
        }
    }
    if(command.indexOf("off")!=-1){
        if(command.indexOf("bluetooth")!=-1){
            moility("off","bluetooth")
            return "Turning off bluetooth."
        }
        if(command.indexOf("wi-fi")!=-1){
            moility("off","wi-fi")
            return "Turning off wi-fi."
        }
        if(command.indexOf("flashlight")!=-1){
            moility("off","flashlight")
            return "Turning off flashlight."
        }
    }

    if(command.indexOf("on")!=-1){
        if(command.indexOf("bluetooth")!=-1){
            moility("on","bluetooth")
            return "Turning on bluetooth."
        }
        if(command.indexOf("wi-fi")!=-1){
            moility("on","wi-fi")
            return "Turning on wi-fi."
        }
        if(command.indexOf("flashlight")!=-1){
            moility("on","flashlight")
            return "Turning on flashlight."
        }
    }

    //call contacts
    if(command.indexOf("call ")!=-1){
        val resultContects=ArrayList<String>()
        val replacedCommand=command.replace("call ","")
        val contacts=MainActivity.contacts
        for(i in 0 until contacts.size){
            if(contacts[i].toLowerCase(Locale.getDefault()).indexOf(replacedCommand)!=-1){
                resultContects.add(contacts[i])
                resultContects.add(contacts[i+1])
            }
        }
        MainActivity.contactList=resultContects
        val aj=HashMap<String,String>()
        for(i in 0 until resultContects.size) {
            if(i%2==0)
                aj[resultContects[i+1]] = resultContects[i]
        }
        return when (aj.size) {
            1 -> {
                MainActivity.contactNumber=resultContects[1]
                "makecallonthat"
            }
            0 -> {
                "makecallonthat2$replacedCommand"
            }
            else -> {
                "makecallonthat3$replacedCommand"
            }
        }
    }

    //search webView
    if(command.indexOf("search ")!=-1){
        val replacedCommand=command.replace("search ","")
        return "searchitfriday$replacedCommand"
    }

    // if(command.indexOf("stock")!=-1||command.indexOf("price")!=-1){
    //    return "searchitfriday"+command
    //}

    //what
    if (command.indexOf("what") != -1) {
        if (command.indexOf("your name") != -1) {
            return when ((System.currentTimeMillis() % 10).toInt()) {
                1, 2, 3, 4, 5 -> ("I am friday.")
                else -> ("friday, Boss")
            }

        }
        if (command.indexOf("time") != -1) {
            val time = DateUtils.formatDateTime(
                context, Date().time,
                DateUtils.FORMAT_SHOW_TIME
            )
            return when ((System.currentTimeMillis() % 10).toInt()) {
                1, 5 -> ("Boss its $time")
                2, 6 -> ("Its $time")
                3, 7 -> ("Boss time is very good and also $time")
                else -> ("Time is $time")
            }

        }

        if (command.indexOf("my name") != -1) {
            return ("Your are Alhaj ")
        }
    }
    //who
    if (command.indexOf("who") != -1) {
        if (command.indexOf("are you") != -1) {
            return ("I am your Assistant Boss")
        }
        if (command.indexOf("creator") != -1) {
            return ("the great Alhaj, that person is very intelligent and smart in every feild, did you know he developed me in one fucking night")

        }
    }
    //set
    if (command.indexOf("set") != -1) {
        if (command.indexOf("reminder") != -1) {
            return ("Ok Boss your reminder is set hihihihi")

        }
    }
    //how
    if (command.indexOf("how") != -1) {
        if (command.indexOf("are you") != -1) {
            return ("I am very good Boss... tell me about you")
        }
    }
    //good
    if (command.indexOf("good") != -1) {
        if (command.indexOf("evening") != -1||command.indexOf("afternoon") != -1||command.indexOf("morning") != -1||command.indexOf("night") != -1) { val time = DateUtils.formatDateTime(
            context, Date().time,
            DateUtils.FORMAT_SHOW_TIME
        )
            var greetingType=""
            var konstant = 0
            var currenthour = 0

            for (p in time) {
                if (konstant == 0) {
                    currenthour = p.toString().toInt()
                }
                if (konstant == 1 && p != ':') {
                    currenthour = currenthour * 10 + p.toString().toInt()
                }
                konstant += 1
            }
            Log.e("houraj", currenthour.toString())
            if(time.indexOf("a.m.")!=-1||time.indexOf("p.m.")!=-1){
                greetingType = if (time.indexOf("a.m.") != -1 && currenthour >= 5 && currenthour <= 11) {
                    ("Good morning Boss.")
                } else if (time.indexOf("p.m.") != -1 && currenthour == 12) {
                    ("Good afternoon Boss.")
                } else if (time.indexOf("p.m.") != -1 && currenthour >= 1 && currenthour <= 4) {
                    ("Good afternoon Boss.")
                } else if (time.indexOf("p.m.") != -1 && currenthour >= 5 && currenthour <= 9) {
                    ("Good evening Boss.")
                } else {
                    ("Good night Boss.")
                }
            }
            else if(time.indexOf("AM")!=-1||time.indexOf("PM")!=-1){
                greetingType = if (time.indexOf("AM") != -1 && currenthour >= 5 && currenthour <= 11) {
                    ("Good morning Boss.")
                } else if (time.indexOf("PM") != -1 && currenthour == 12) {
                    ("Good afternoon Boss.")
                } else if (time.indexOf("PM") != -1 && currenthour >= 1 && currenthour <= 4) {
                    ("Good afternoon Boss.")
                } else if (time.indexOf("PM") != -1 && currenthour >= 5 && currenthour <= 9) {
                    ("Good evening Boss.")
                } else {
                    ("Good night Boss.")
                }
            }
            else if(time.indexOf("am")!=-1||time.indexOf("pm")!=-1){
                greetingType = if (time.indexOf("am") != -1 && currenthour >= 5 && currenthour <= 11) {
                    ("Good morning Boss.")
                } else if (time.indexOf("pm") != -1 && currenthour == 12) {
                    ("Good afternoon Boss.")
                } else if (time.indexOf("pm") != -1 && currenthour >= 1 && currenthour <= 4) {
                    ("Good afternoon Boss.")
                } else if (time.indexOf("pm") != -1 && currenthour >= 5 && currenthour <= 9) {
                    ("Good evening Boss.")
                } else {
                    ("Good night Boss.")
                }
            }

            if(greetingType.indexOf("morning")!=-1){
                if(command.indexOf("morning") == -1){
                    return ("sir please don't use weed nest time, weed don't suit you, its $greetingType")
                }
            }
            if(greetingType.indexOf("afternoon")!=-1){
                if(command.indexOf("afternoon") == -1){
                    return ("sir please don't use weed nest time, weed don't suit you, its $greetingType")
                }
            }
            if(greetingType.indexOf("evening")!=-1){
                if(command.indexOf("evening") == -1){
                    return ("sir please don't use weed nest time, weed don't suit you, its $greetingType")
                }
            }
            if(greetingType.indexOf("night")!=-1){
                if(command.indexOf("night") == -1){
                    return ("sir please don't use weed nest time, weed don't suit you, its $greetingType")
                }
            }
            return greetingType

        }

        if (command.indexOf("weather") != -1) {
            return ("You should go out Boss.")
        }
        if (command.indexOf("mood") != -1) {
            return ("this is very good Boss")
        }

    }
    //bye
    if (command.indexOf("bye") != -1 || command.indexOf("see you later") != -1) {
        return ("nikal lav de , pehli fursat me nikal")

    }
    //abusive words
    if (command.indexOf("f***") != -1 || command.indexOf("sex") != -1 || command.indexOf("dick") != -1 || command.indexOf(
            "porn"
        ) != -1 || command.indexOf("ass") != -1 || command.indexOf("blow job") != -1 || command.indexOf("t********") != -1
    ) {

        return when ((System.currentTimeMillis() % 10).toInt()) {
            1, 5 -> ("I don't want to talk about this.")
            2, 6 -> ("I don't like it Boss.")
            3, 7 -> ("this is too much Boss.")
            4, 8 -> ("Boss you belong to very respected family")
            else -> ("I don't know Boss.")
        }

    }
    //greetings
    if (command.indexOf("hello") != -1 || command.indexOf("hi ") != -1 || command.indexOf("hey") != -1) {
        return when ((System.currentTimeMillis() % 10).toInt()) {
            1, 5 -> ("Hello Boss.")
            2, 6 -> ("Hi Boss.")
            3, 7 -> ("Assalawalekum Boss.")
            4, 8 -> ("Hey Boss.")
            else -> ("Jai hind Boss.")
        }
    }
     //   else if(command.indexOf("what ") != -1 ||command.indexOf("why ") != -1 ||command.indexOf("who ") != -1 ||command.indexOf("when ") != -1 ||command.indexOf("whom ") != -1 ||command.indexOf("how ") != -1 ||command.indexOf("where") != -1 ||command.indexOf("which ") != -1 ||command.indexOf("whose ") != -1 ){
       // return "searchitfriday"+command
   // }

    //exceptions
    else {/*
        val random: Int = (System.currentTimeMillis() % 10).toInt()
        when (random) {
            1, 5 -> return ("I can't understand Boss?")
            2, 6 -> return ("What u talking about?")
            3, 7 -> return ("What Boss?")
            4, 8 -> return ("What is that?")
            else -> return ("I don't know Boss?")
        }
*/
        return "chatbot$command"
    }


}
fun moility(a:String,b:String):String{
    return "$a $b"
}

