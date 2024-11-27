package com.example.familiada.utils

import android.content.Context
import com.example.familiada.R
import com.example.familiada.data.Question
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import android.util.Log

fun loadQuestions(context: Context): List<Question> {
    return try {
        val inputStream = context.resources.openRawResource(R.raw.questions)
        val reader = InputStreamReader(inputStream)
        val jsonText = reader.readText()

        val loadedQuestions = Json.decodeFromString<List<Question>>(jsonText)
        loadedQuestions.shuffled()

    } catch (e: Exception) {
        Log.e("LoadQuestions", "Błąd wczytywania pliku JSON: ${e.message}", e)
        emptyList<Question>()
    }
}