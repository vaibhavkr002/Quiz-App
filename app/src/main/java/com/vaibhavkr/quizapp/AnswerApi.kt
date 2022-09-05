package com.vaibhavkr.quizapp


import com.vaibhavkr.quizapp.Data.AnswerSet
import retrofit2.Response
import retrofit2.http.GET

interface AnswerApi {
  @GET("?quiz=true")
    suspend fun getans(): Response<AnswerSet>
}