package com.vaibhavkr.quizapp.Data

data class Question(
    val lable: String,
    val options: List<Option>,
    val correct_answers: List<Int>
)