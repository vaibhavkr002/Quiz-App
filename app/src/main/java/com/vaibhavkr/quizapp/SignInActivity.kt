package com.vaibhavkr.quizapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vaibhavkr.quizapp.databinding.ActivityMainBinding
import com.vaibhavkr.quizapp.databinding.ActivitySignBinding
import com.vaibhavkr.quizapp.databinding.ActivitySignUpBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    lateinit var binding: ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = findViewById(R.id.pgbaars)
        mAuth = FirebaseAuth.getInstance()


        binding.back.setOnClickListener() {
            var intent = Intent(this, MiddlePageactivity::class.java)
            startActivity(intent)
        }

        binding.dtnaccn.setOnClickListener() {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //checkbox for password
        binding.signInCheckbox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

            } else {
                binding.passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance())

            }
        }

     binding.buttonSignIn.setOnClickListener {
            if(checking()){
                val email=binding.emailEt.text.toString()
                val password=binding.passwordEt.text.toString()
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            var intent =Intent(this,MainActivity::class.java)
                            intent.putExtra("email",email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Wrong Details", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the Details",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checking():Boolean
    {
        if(binding.emailEt.text.toString().trim{it<=' '}.isNotEmpty()
            &&binding.passwordEt.text.toString().trim{it<=' '}.isNotEmpty())
        {
            return true
        }
        return false
    }
    }
