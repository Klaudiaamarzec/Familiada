package com.example.familiada.controller

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.familiada.R
import com.example.familiada.data.Answer
import com.example.familiada.data.Question
import com.example.familiada.utils.loadQuestions
import com.example.familiada.utils.normalizeText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameController(
    private val context: Context,
    team1Players: List<String>,
    team2Players: List<String>,
    private val isSoundEnabled: Boolean = true,
    private val isTimeLimitEnabled: Boolean = true,
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
    private var team1PlayerIdx = 0
    private var team2PlayerIdx = 0
    private var roundNumber = 1
    private var stolenRound = false
    private var gameOver = false

    private var timerJob: Job? = null
    var remainingTime by mutableStateOf(60)
    private var timerActive by mutableStateOf(false)
    var revealedAnswers: MutableMap<String, Boolean> = mutableMapOf()

    init {
        // Zainicjowanie mapy odpowiedz - zakryte/odkryte [bool] po stworzeniu obiektu
        initializeRevealedAnswers()
    }

    private fun initializeRevealedAnswers() {
        getCurrentQuestion()?.answers?.forEach { key ->
            revealedAnswers.put(key.text, false)
        }
    }

    fun getPlayer(): String {
        return if (isTeam1Turn) {
            team1Queue[team1PlayerIdx]
        } else team2Queue[team2PlayerIdx]
    }

    fun getScoreTeam1(): Int {
        return scoreTeam1
    }

    fun getScoreTeam2(): Int {
        return scoreTeam2
    }

    var answeringTeam: Team? = null // Przechowuje nazwę drużyny odpowiadającej

    fun selectTeam(team: Team) {
        if (answeringTeam === null) {
            answeringTeam = team
            isTeam1Turn = team == Team.FIRST
        }

    }

    private fun resetAnsweringTeam() {
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

    fun submitAnswer(answerText: String): Boolean {

        val question = getCurrentQuestion()
        val normalizedAnswerText = normalizeText(answerText)

        val correctAnswer = question?.answers?.find {
            normalizeText(it.text) == normalizedAnswerText

        }
        progressTeamQueue()
        stopTimer()

        return if ((correctAnswer != null) && (revealedAnswers[correctAnswer.text] == false)) {
            revealedAnswers.keys.forEach { key ->
                val normalizedKey = normalizeText(key)
                if (normalizedKey == normalizeText(correctAnswer.text)) {
                    revealedAnswers[key] = true
                }
            }

            if (stolenRound) {
                // Drużyna przejmuje punkty przeciwnej
                if (isTeam1Turn) {
                    scoreTeam1 += correctAnswer.points + scoreTeam2
                } else {
                    scoreTeam2 += correctAnswer.points + scoreTeam1
                }
                nextQuestion()
                resetTeam()
                stolenRound = false
                playSound(R.raw.all_corect)
            } else {
                // Dodanie punktów do drużyny
                addPointsToTeam(correctAnswer)
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
                addIncorrectQuestionsForTeam()
                // Sprawdzenie, czy drużyna przekroczyła 3 błędne odpowiedzi
                if (incorrectAnswersTeam1 >= 3 || incorrectAnswersTeam2 >= 3) {
                    playSound(R.raw.three_wrong)
                    handleStolenRound()

                } else {
                    playSound(R.raw.wrong_answer)
                }
            } else {
                playSound(R.raw.three_wrong)
                nextQuestion()
                resetTeam()
                stolenRound = false
            }
            false
        }

    }

    private fun progressTeamQueue() {
        if (isTeam1Turn) {
            team1PlayerIdx = (team1PlayerIdx + 1) % team1Queue.size
        } else {
            team2PlayerIdx = (team2PlayerIdx + 1) % team2Queue.size
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

    private fun addPointsToTeam(correctAnswer: Answer) {
        if (isTeam1Turn) {
            scoreTeam1 += correctAnswer.points
            correctAnswersTeam1++

        } else {
            scoreTeam2 += correctAnswer.points
            correctAnswersTeam2++
        }
    }

    private fun addIncorrectQuestionsForTeam() {
        if (isTeam1Turn) {
            incorrectAnswersTeam1++
        } else {
            incorrectAnswersTeam2++
        }
    }

    private fun resetTeamPlayerIndexes() {
        team1PlayerIdx = (roundNumber - 1) % team1Queue.size
        team2PlayerIdx = (roundNumber - 1) % team2Queue.size
    }

    private fun handleStolenRound() {
        stolenRound = true
        resetTeamAnswerCounts()
        resetTeamPlayerIndexes()
        isTeam1Turn = !isTeam1Turn
        answeringTeam = if (answeringTeam == Team.FIRST) {
            Team.SECOND
        } else {
            Team.FIRST
        }
    }

    private fun resetTeam() {
        answeringTeam = null
        resetTeamInfo()
    }

    private fun resetTeamInfo() {
        resetTeamAnswerCounts()
        resetTeamPlayerIndexes()
    }

    private fun nextQuestion() {
        currentQuestionIndex++
        initializeRevealedAnswers()
        roundNumber++
        if (!stolenRound) {
            resetAnsweringTeam()
        }
        if (roundNumber > 10) {
            gameOver = true
        }
    }

    fun getCurrentTeamName(): String {
        return if (answeringTeam !== null) answeringTeam.toString() else "?"
    }

    private fun startTimer() {
        if (timerActive) return
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (remainingTime > 0) {
                delay(1000)
                remainingTime -= 1
            }
            if (remainingTime == 0 && isTimeLimitEnabled && answeringTeam !== null) {
                submitAnswer("")
                resetTimer()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun resetTimer() {
        remainingTime = 60
        startTimer()
    }

}