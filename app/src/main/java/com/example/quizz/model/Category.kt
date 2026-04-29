package com.example.quizz.model

import java.io.Serializable

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val emoji: String
) : Serializable