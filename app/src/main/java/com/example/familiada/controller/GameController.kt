package com.example.familiada.controller

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.*
import com.example.familiada.data.Question
import com.example.familiada.utils.loadQuestions
import java.text.Normalizer
import kotlin.random.Random
import com.example.familiada.R

class GameController(private val context: Context, private val isSoundEnabled: Boolean = true) {

    private val questions: List<Question> = loadQuestions(context)
    private var currentQuestionIndex by mutableIntStateOf(Random.nextInt(questions.size))  // Losowy indeks pytania
    private var scoreTeam1 = 0
    private var scoreTeam2 = 0
    private var incorrectAnswersTeam1 = 0
    private var incorrectAnswersTeam2 = 0
    private var correctAnswersTeam1 = 0
    private var correctAnswersTeam2 = 0
    private var isTeam1Turn = true

    // Obsługa dźwięków
    private fun playSound(resourceId: Int) {
        if (isSoundEnabled) {
            val mediaPlayer = MediaPlayer.create(context, resourceId)
            mediaPlayer.setOnCompletionListener {
                it.release()
            }
            mediaPlayer.start()
        }
    }

    // Metody do manipulowania stanem gry
    fun getCurrentQuestion(): Question? {
        return questions.getOrNull(currentQuestionIndex)
    }

    private fun normalizeText(text: String): String {
        // Usuwanięcie polskich znaków i zamiana na same małe litery
        val normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}"), "") // Usuwanie znaków diakrytycznych
            .lowercase()
        return normalizedText
    }

    fun submitAnswer(answerText: String): Boolean {

        val question = getCurrentQuestion()
        val normalizedAnswerText = normalizeText(answerText)

        val correctAnswer = question?.answers?.find {
            normalizeText(it.text) == normalizedAnswerText
        }

        //val correctAnswer = question?.answers?.find { it.text.equals(answerText, ignoreCase = true) }

        return if (correctAnswer != null) {
            // Dodanie punktów do drużyny
            if (isTeam1Turn) {
                scoreTeam1 += correctAnswer.points
                correctAnswersTeam1++
            } else {
                scoreTeam2 += correctAnswer.points
                correctAnswersTeam2++
            }

            // Sprawdzenie, czy drużyna zgadła wszystkie odpowiedzi
            if (correctAnswersTeam1 == question.answers.size && isTeam1Turn || correctAnswersTeam2 == question.answers.size && !isTeam1Turn) {
                playSound(R.raw.all_corect)
                switchTeam()
                nextQuestion()
            }
            else {
                playSound(R.raw.correct_answer)
            }

            true
        } else {
            // Błędna odpowiedź
            if (isTeam1Turn) {
                incorrectAnswersTeam1++
            } else {
                incorrectAnswersTeam2++
            }

            // Sprawdzenie, czy drużyna przekroczyła 3 błędne odpowiedzi
            if (incorrectAnswersTeam1 >= 3 || incorrectAnswersTeam2 >= 3) {
                playSound(R.raw.three_wrong)
                switchTeam()
                nextQuestion()
            }
            else {
                playSound(R.raw.wrong_answer)
            }

            false
        }
    }

    fun getScore(): Int {
        return if (isTeam1Turn) scoreTeam1 else scoreTeam2
    }

    fun getIncorrectAnswers(): Int {
        return if (isTeam1Turn) incorrectAnswersTeam1 else incorrectAnswersTeam2
    }

    private fun switchTeam() {
        isTeam1Turn = !isTeam1Turn

        // Reset poprawnych i błędnych odpowiedzi
        if (isTeam1Turn) {
            correctAnswersTeam1 = 0
            incorrectAnswersTeam2 = 0
        } else {
            correctAnswersTeam2 = 0
            incorrectAnswersTeam1 = 0
        }
    }

    private fun nextQuestion() {
        currentQuestionIndex++
    }

    fun getCurrentTeam(): String {
        return if (isTeam1Turn) "Drużyna 1" else "Drużyna 2"
    }

//    fun isGameOver(): Boolean {
//
//    }
}