package com.example.quizz.model

import java.io.Serializable

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val categoryId: String
) : Serializable