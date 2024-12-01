package com.example.familiada.controller

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.familiada.R
import com.example.familiada.data.Question
import com.example.familiada.utils.loadQuestions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.Normalizer
import kotlin.random.Random

class GameController(
    private val context: Context,
    team1Players: List<String>,
    team2Players: List<String>,
    private val isSoundEnabled: Boolean = true
) {

    private val questions: List<Question> = loadQuestions(context)
    private var currentQuestionIndex by mutableIntStateOf(Random.nextInt(questions.size))  // Losowy indeks pytania
    private var scoreTeam1 = 0
    private var scoreTeam2 = 0
    private var incorrectAnswersTeam1 = 0
    private var incorrectAnswersTeam2 = 0
    private var correctAnswersTeam1 = 0
    private var correctAnswersTeam2 = 0
    private var isTeam1Turn = true
    private var team1Queue: MutableList<String> = team1Players.toMutableList()
    private var team2Queue: MutableList<String> = team2Players.toMutableList()
    private var team1PlayerIdx = 0;
    private var team2PlayerIdx = 0;
    private var roundNumber = 1;
    private var stolenRound = false
    private var gameOver = false

    private var timerJob: Job? = null
    var remainingTime by mutableStateOf(60)
    var timerActive by mutableStateOf(false)

    fun getPlayer(): String {
        return if (isTeam1Turn) {
            team1Queue[team1PlayerIdx]
        } else team2Queue[team2PlayerIdx]
    }

    var answeringTeam: String? = null // Przechowuje nazwę drużyny odpowiadającej

    fun selectTeam(team: String) {
        if (answeringTeam == null) {
            answeringTeam = team
            isTeam1Turn = team == "Drużyna 1"
        }
    }

    fun resetAnsweringTeam() {
        answeringTeam = null
    }


    fun isGameOver(): Boolean {
        return gameOver
    }

    fun getRoundNumber(): Number {
        return roundNumber
    }

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
        val normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD).replace(
            Regex("\\p{InCombiningDiacriticalMarks}"), ""
        ) // Usuwanie znaków diakrytycznych
            .lowercase()
        return normalizedText
    }

    fun submitAnswer(answerText: String): Boolean {

        val question = getCurrentQuestion()
        val normalizedAnswerText = normalizeText(answerText)

        val correctAnswer = question?.answers?.find {
            normalizeText(it.text) == normalizedAnswerText

        }

        if (isTeam1Turn) {
            team1PlayerIdx++;
        } else {
            team2PlayerIdx++;
        }

        //val correctAnswer = question?.answers?.find { it.text.equals(answerText, ignoreCase = true) }

        stopTimer()

        return if (correctAnswer != null) {
            if (stolenRound) {
                // Drużyna przejmuje punkty przeciwnej
                playSound(R.raw.correct_answer)
                if (isTeam1Turn) {
                    scoreTeam1 += correctAnswer.points + scoreTeam2
                } else {
                    scoreTeam2 += correctAnswer.points + scoreTeam1
                }
                nextQuestion()
                resetTeam()
                stolenRound = false;
            } else {
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
                    resetTeam()
                    nextQuestion()
                } else {
                    playSound(R.raw.correct_answer)
                }
            }
            true
        } else {
            // Błędna odpowiedź
            if (!stolenRound) {
                if (isTeam1Turn) {
                    incorrectAnswersTeam1++
                } else {
                    incorrectAnswersTeam2++
                }

                // Sprawdzenie, czy drużyna przekroczyła 3 błędne odpowiedzi
                if (incorrectAnswersTeam1 >= 3 || incorrectAnswersTeam2 >= 3) {
                    playSound(R.raw.three_wrong)
                    handleStolenRound()

                } else {
                    playSound(R.raw.wrong_answer)
                }
            } else {
                playSound(R.raw.wrong_answer)
                resetTeam()
                nextQuestion()
                stolenRound = false;
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

    private fun resetTeamAnswerCounts() {
        correctAnswersTeam1 = 0
        incorrectAnswersTeam2 = 0
        correctAnswersTeam2 = 0
        incorrectAnswersTeam1 = 0
    }

    private fun resetTeamPlayerIndexes() {
        team1PlayerIdx = 0
        team2PlayerIdx = 0
    }

    fun handleStolenRound() {
        stolenRound = true
        resetTeamAnswerCounts()
        resetTeamPlayerIndexes()
        isTeam1Turn = !isTeam1Turn
    }

    private fun resetTeam() {
        answeringTeam = null
        resetTeamAnswerCounts()
        resetTeamPlayerIndexes()

    }

    private fun nextQuestion() {
        currentQuestionIndex++
        roundNumber++
        if (!stolenRound) {
            resetAnsweringTeam()
        }
        if (roundNumber > 10) {
            gameOver = true;
        }
    }

    fun getCurrentTeam(): String {
        return if (answeringTeam !== null) answeringTeam!! else "?"
    }

    fun getWinnerTeam(): String? {
        return when {
            gameOver -> {
                when {
                    scoreTeam1 > scoreTeam2 -> "Drużyna 1 wygrywa!"
                    scoreTeam2 > scoreTeam1 -> "Drużyna 2 wygrywa!"
                    else -> "Remis!"
                }
            }

            else -> null
        }
    }

    fun startTimer() {
        if (timerActive) return

        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (remainingTime > 0) {
                delay(1000)
                remainingTime -= 1
            }
            if (remainingTime == 0) {
                submitAnswer("")
                resetTimer()
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun resetTimer() {
        remainingTime = 60
        startTimer()
    }

}