package com.example.demospeechrecognization

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    lateinit var prog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prog= ProgressDialog(this)
        if(FirebaseAuth.getInstance().currentUser!=null){
            val intent=Intent(this,MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        setContentView(R.layout.activity_create_account)
        textViewalreadyhavea.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        buttonsignup.setOnClickListener {
            registerUser()
        }
    }

    fun registerUser(){
        if (editTextemailsignup.text.isEmpty()||editTextsignuppass.text.isEmpty()||editTextusernamesignup.text.isEmpty()){
            editTextsignuppass.error="Enter text"
            return
        }
        prog.setMessage("")
        prog.show()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextemailsignup.text.toString(),editTextsignuppass.text.toString()).addOnCompleteListener(){
            if(!it.isSuccessful){
                prog.dismiss()
                return@addOnCompleteListener
            }
            savefireuse()
            Toast.makeText(this,"succesfull",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener(){
            prog.dismiss()
            Toast.makeText(this,it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    fun savefireuse(){

        val key =FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$key")
        val objuser=user(editTextusernamesignup.text.toString(),editTextsignuppass.text.toString(),key)
        ref.setValue(objuser)
            .addOnSuccessListener {
                Toast.makeText(this,"we saved user to database",Toast.LENGTH_SHORT).show()
                val intent=Intent(this,MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this,it.message.toString(), Toast.LENGTH_SHORT).show()
            }


        prog.dismiss()
    }
}
class user(val username:String,val pswd:String,val uid:String){
    constructor():this("","","")
}