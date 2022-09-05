package com.vaibhavkr.quizapp

import com.vaibhavkr.quizapp.Constant.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val  retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnswerApi::class.java)

    }

    }



