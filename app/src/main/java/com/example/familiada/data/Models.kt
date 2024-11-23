package com.example.familiada.data

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val question: String,
    val answers: List<Answer>
)

@Serializable
data class Answer(
    val text: String,
    val points: Int
)