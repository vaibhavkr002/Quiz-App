package com.vaibhavkr.quizapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.vaibhavkr.quizapp.databinding.ActivityResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    lateinit var auth: FirebaseAuth
    lateinit   var database: FirebaseDatabase
    lateinit   var storage: FirebaseStorage

    private val reference = FirebaseFirestore.getInstance().collection("USERS")
    var currScore = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityResultBinding.inflate(layoutInflater)
            setContentView(binding.root)

            auth = FirebaseAuth.getInstance()
            currScore = intent.getIntExtra("totalScoreEarned", 0)
            binding.scoreText.text = currScore.toString()

            database = FirebaseDatabase.getInstance()
            storage = FirebaseStorage.getInstance()

            database.reference.child("image").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val image = snapshot.getValue(String::class.java)
                    Picasso.get().load(image).into(binding.profile)

                }

                override fun onCancelled(error: DatabaseError) {}
            })


            binding.back.setOnClickListener(){
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
    suspend fun saveUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.currentUser?.email?.let { reference.document(it).set(user) }
            } catch (e: Exception) {
            }
        }
    }

}