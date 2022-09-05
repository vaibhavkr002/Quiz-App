package com.vaibhavkr.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.vaibhavkr.quizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    lateinit var binding : ActivityMainBinding
    lateinit   var database: FirebaseDatabase
    lateinit   var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = this?.getPreferences(MODE_PRIVATE) ?: return
        val isLogin = sharedPref.getString("email", "1")

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        database.reference.child("image").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val image = snapshot.getValue(String::class.java)
                Picasso.get().load(image).into(binding.profile)

            }

            override fun onCancelled(error: DatabaseError) {}
        })

        binding.start.setOnClickListener(){
            var intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }

        binding.home.setOnClickListener(){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            binding.profile.setOnClickListener(){
                var intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
        binding.powerBtn.setOnClickListener(){
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.arrowLeft.setOnClickListener {
            sharedPref.edit().remove("email").apply()
            var intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        if(isLogin=="1")
        {
            var email=intent.getStringExtra("email")
            if(email!=null)
            {
                setText(email)
                with(sharedPref.edit())
                {
                    putString("email",email)
                    apply()
                }
            }
            else{
                var intent = Intent(this,WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        else
        {
            setText(isLogin)
        }

    }

    private fun setText(email:String?)
    {
        db= FirebaseFirestore.getInstance()
        if (email != null) {
            db.collection("USERS").document(email).get()
                .addOnSuccessListener {
                        tasks->
                  binding.name.text=tasks.get("name").toString()

                }
        }

    }

    }
