package com.example.demospeechrecognization.AI

import android.content.Context
import android.text.format.DateUtils
import com.example.demospeechrecognization.MainActivity
import java.util.*
import kotlin.collections.HashMap

fun AIlerner(context: Context, command: String): String {

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

        val resultscontects=ArrayList<String>()
        val command=command.replace("call ","")
        val contacts=MainActivity.contacts
        for(i in 0..contacts.size-1){
            if(contacts[i].toLowerCase().indexOf(command)!=-1){
                resultscontects.add(contacts[i])
                resultscontects.add(contacts[i+1])
            }
        }
        MainActivity.resultcontact=resultscontects
        val aj=HashMap<String,String>()
        for(i in 0..resultscontects.size-1) {
            if(i%2==0)
            aj.put(resultscontects[i+1],resultscontects[i])
        }
        if(aj.size==1){
            MainActivity.thatnum=resultscontects[1]
            return "makecallonthat"

        }
        else if(aj.size==0){
            return "makecallonthat2"+command
        }
        else{
            return "makecallonthat3"+command
        }

    }


    //search webView
    if(command.indexOf("search ")!=-1){
        val command=command.replace("search ","")
        return "searchitfriday"+command
    }

    // if(command.indexOf("stock")!=-1||command.indexOf("price")!=-1){
    //    return "searchitfriday"+command
    //}

    //what
    if (command.indexOf("what") != -1) {
        if (command.indexOf("your name") != -1) {
            val time = DateUtils.formatDateTime(
                context, Date().time,
                DateUtils.FORMAT_SHOW_TIME
            )
            val random: Int = (System.currentTimeMillis() % 10).toInt()
            when (random) {
                1, 2, 3, 4, 5 -> return ("I am friday.")
                else -> return ("friday, Boss")
            }

        }
        if (command.indexOf("time") != -1) {
            val time = DateUtils.formatDateTime(
                context, Date().time,
                DateUtils.FORMAT_SHOW_TIME
            )
            val random: Int = (System.currentTimeMillis() % 10).toInt()
            when (random) {
                1, 5 -> return ("Boss its " + time)
                2, 6 -> return ("Its " + time)
                3, 7 -> return ("Boss time is very good and also " + time)
                else -> return ("Time is " + time)
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
        if (command.indexOf("evening") != -1) {
            return ("Good evening Boss.")

        }
        if (command.indexOf("afternoon") != -1) {
            return ("Good afternoon Boss.")

        }
        if (command.indexOf("morning") != -1) {
            return ("Good morning Boss.")

        }
        if (command.indexOf("night") != -1) {
            return ("Good night Boss.")

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

        val random: Int = (System.currentTimeMillis() % 10).toInt()
        when (random) {
            1, 5 -> return ("I don't want to talk about this.")
            2, 6 -> return ("I don't like it Boss.")
            3, 7 -> return ("this is too much Boss.")
            4, 8 -> return ("Boss you belong to very respected family")
            else -> return ("I don't know Boss.")
        }

    }
    //greetings
    if (command.indexOf("hello") != -1 || command.indexOf("hi ") != -1 || command.indexOf("hey") != -1) {
        val random: Int = (System.currentTimeMillis() % 10).toInt()
        when (random) {
            1, 5 -> return ("Hello Boss.")
            2, 6 -> return ("Hi Boss.")
            3, 7 -> return ("Assalawalekum Boss.")
            4, 8 -> return ("Hey Boss.")
            else -> return ("Jai hind Boss.")
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
        return "chatbot"+command
    }


}
fun moility(a:String,b:String):String{
    return a+" "+b
}

