package com.vaibhavkr.quizapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhavkr.quizapp.databinding.ActivityProfileBinding
import com.vaibhavkr.quizapp.databinding.ActivitySignUpBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()

       binding.back.setOnClickListener() {
            var intent = Intent(this, MiddlePageactivity::class.java)
            startActivity(intent)
        }

       binding.allrdyaccn.setOnClickListener() {
            var intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

//checkbox for password
     binding.signCheckbox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
              binding.passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

            } else {
                binding.passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance())

            }
        }

//Authentication with email/Password in Firebase
    binding.buttonSignup.setOnClickListener {
            if(checking())
            {

                val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                val cal = Calendar.getInstance()
                val date = dateFormat.format(cal.time)

                var email=binding.emailEt.text.toString()
                var password= binding.passwordEt.text.toString()
                var name=binding.nameEt.text.toString()
                val user= hashMapOf(
                    "name" to name,
                    "email" to email
                )
                val Users=db.collection("USERS")
                val query =Users.whereEqualTo("email",email).get()
                    .addOnSuccessListener {
                            tasks->
                        if(tasks.isEmpty)
                        {
                            mAuth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(this){
                                        task->
                                    if(task.isSuccessful)
                                    {
                                        Users.document(email).set(user)
                                        val intent=Intent(this,MainActivity::class.java)
                                        intent.putExtra("email",email)
                                        startActivity(intent)
                                        finish()
                                    }
                                    else
                                    {
                                        Toast.makeText(this,"Authentication Failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else
                        {
                            Toast.makeText(this,"User Already Registered", Toast.LENGTH_LONG).show()
                            val intent= Intent(this,SignInActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the Details", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun checking():Boolean{
        if(binding.nameEt.text.toString().trim{it<=' '}.isNotEmpty()
            &&binding.emailEt.text.toString().trim{it<=' '}.isNotEmpty()
            &&binding.passwordEt.text.toString().trim{it<=' '}.isNotEmpty()
        )
        {
            return true
        }
        return false
    }

}






