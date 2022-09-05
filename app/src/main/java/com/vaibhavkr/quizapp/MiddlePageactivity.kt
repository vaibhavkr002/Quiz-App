package com.vaibhavkr.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class MiddlePageactivity : AppCompatActivity() {

    private lateinit var btnsignin : Button
    private lateinit var btnsignup : Button
    private lateinit var back :ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_middle_pageactivity)

        btnsignin = findViewById(R.id.signin)
        btnsignup = findViewById(R.id.signup)
        back = findViewById(R.id.back)

        back.setOnClickListener(){
            var intent = Intent(this,WelcomeActivity::class.java)
            startActivity(intent)
        }

       btnsignup.setOnClickListener(){
            var intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

     btnsignin.setOnClickListener(){
            var intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
    }
}