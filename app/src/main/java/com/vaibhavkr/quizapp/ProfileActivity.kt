package com.vaibhavkr.quizapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.vaibhavkr.quizapp.databinding.ActivityProfileBinding


class ProfileActivity : AppCompatActivity() {

    lateinit var binding:ActivityProfileBinding
   lateinit var launcher: ActivityResultLauncher<String>
   lateinit   var database: FirebaseDatabase
   lateinit   var storage: FirebaseStorage
   lateinit  var fAuth: FirebaseAuth
   lateinit var userId: String
   lateinit var fstore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        fAuth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        userId = fAuth.currentUser!!.uid


        val documentReference = fstore.collection("USERS").document(userId)
        documentReference.addSnapshotListener(
            this
        ) { documentSnapshot, error ->
            binding.name.setText(documentSnapshot!!.getString("name"))
            binding.email.setText( documentSnapshot!!.getString("email"))

        }

        //retriving image from firebase
        database.reference.child("image").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val image = snapshot.getValue(String::class.java)
                Picasso.get().load(image).into(binding.im)

            }

            override fun onCancelled(error: DatabaseError) {}
        })

        //Uploading image in firebase storage
        launcher = registerForActivityResult<String, Uri>(
            ActivityResultContracts.GetContent()
        ) { uri ->
            binding.im.setImageURI(uri)
            val reference = storage.reference.child("image")
            reference.putFile(uri!!).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    database.reference.child("image")
                        .setValue(uri.toString()).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "upload",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }

        //selecting image from Device
        binding.camera1.setOnClickListener {
            launcher.launch("image/*") }
    }
}