package com.vaibhavkr.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class WelcomeActivity : AppCompatActivity() {

   private lateinit var btnstart :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started)

        btnstart = findViewById(R.id.btnstart)

        btnstart.setOnClickListener(){
            var intent = Intent(this,MiddlePageactivity::class.java)
            startActivity(intent)
        }

    }

}