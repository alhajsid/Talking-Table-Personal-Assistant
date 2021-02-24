package com.example.demospeechrecognization

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.nio.ByteOrder.LITTLE_ENDIAN
import android.R.attr.order
import android.R.attr.start
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    lateinit var prog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        prog= ProgressDialog(this)
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContentView(R.layout.activity_login)

        textViewcreateaccounr.setOnClickListener {
            startActivity(Intent(this,CreateAccountActivity::class.java))
            finish()
        }

        buttonlogin.setOnClickListener {
            loginuser()
        }

    }

    fun loginuser(){
        if (editTextemaillogin.text.isEmpty()||editTextloginpassword.text.isEmpty()){
            editTextemaillogin.error="Enter text"
            return
        }
        prog.setTitle("Connecting to Server ;)")
        prog.show()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextemaillogin.text.toString(),editTextloginpassword.text.toString()).addOnCompleteListener(){
            if(!it.isSuccessful){
                prog.dismiss()
                return@addOnCompleteListener
            }
            prog.dismiss()
            prog.setTitle("Loging in....")
            prog.show()
            Toast.makeText(this,"succesfull", Toast.LENGTH_SHORT).show()
            val intent=Intent(this,MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            prog.dismiss()
        }
            .addOnFailureListener(){
                Toast.makeText(this,it.message.toString(), Toast.LENGTH_SHORT).show()
                prog.dismiss()
            }

    }
}
