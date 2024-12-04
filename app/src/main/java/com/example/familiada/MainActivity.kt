package com.example.familiada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.familiada.screens.GameOverScreen
import com.example.familiada.screens.GamePreparationScreen
import com.example.familiada.screens.GameScreen
import com.example.familiada.screens.PlayersSelectionScreen
import com.example.familiada.screens.RulesScreen
import com.example.familiada.screens.SettingsScreen
import com.example.familiada.screens.StartScreen
import com.example.familiada.ui.theme.FamiliadaTheme

class MainActivity : ComponentActivity() {
    private var currentScreen: @Composable () -> Unit = {
        StartScreen(onStartGame = { navigateToPlayersSelection() },
            onRules = { navigateToRules() },
            onSettings = { navigateToSettings() })
    }
    private var isDarkThemeState = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isDarkThemeState.value = sharedPreferences.getBoolean("isDarkTheme", true)

        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    private fun startGame(team1: List<String>, team2: List<String>) {
        val isSoundEnabled =
            getSharedPreferences("AppSettings", MODE_PRIVATE).getBoolean("isSoundEnabled", false)
        val isTimeLimitEnabled = getSharedPreferences("AppSettings", MODE_PRIVATE).getBoolean(
            "isTimeLimitEnabled",
            false
        )
        val isMicEnabled = getSharedPreferences("AppSettings", MODE_PRIVATE).getBoolean(
            "isMicEnabled",
            false
        )

        currentScreen = {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                val context = LocalContext.current
                GameScreen(
                    context = context,
                    team1Players = team1,
                    team2Players = team2,
                    isSoundEnabled = isSoundEnabled,
                    isTimeLimitEnabled = isTimeLimitEnabled,
                    isMicEnabled = isMicEnabled,
                    onGameOver = { scoreTeam1, scoreTeam2 ->
                        navigateToGameOverScreen(scoreTeam1, scoreTeam2, team1, team2)
                    }
                )
            }
        }
        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    private fun navigateToStart() {
        currentScreen = {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                StartScreen(
                    onStartGame = { navigateToPlayersSelection() },
                    onRules = { navigateToRules() },
                    onSettings = { navigateToSettings() })
            }
        }
        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    private fun navigateToRules() {
        currentScreen = {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                RulesScreen(
                    onBackToStart = { navigateToStart() }
                )
            }
        }
        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    private fun navigateToSettings() {
        currentScreen = {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                SettingsScreen(
                    onBackToStart = { navigateToStart() },
                    onThemeChange = { newTheme ->
                        isDarkThemeState.value = newTheme
                        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isDarkTheme", newTheme).apply()
                    },
                    context = this@MainActivity
                )
            }
        }
        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    private fun navigateToPlayersSelection() {
        currentScreen = {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                PlayersSelectionScreen(onPlayersConfirmed = { team1, team2 ->
                    navigateToGamePreparation(
                        team1,
                        team2
                    )
                })
            }
        }
        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    private fun navigateToGamePreparation(team1: List<String>, team2: List<String>) {
        currentScreen = {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                GamePreparationScreen(
                    team1 = team1,
                    team2 = team2,
                    onStartGame = { shuffledTeam1, shuffledTeam2 ->
                        startGame(shuffledTeam1, shuffledTeam2)
                    })
            }
        }
        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    private fun navigateToGameOverScreen(team1Score: Int, team2Score: Int, team1: List<String>, team2: List<String>) {
        currentScreen = {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                GameOverScreen(
                    scoreTeam1 = team1Score,
                    scoreTeam2 = team2Score,
                    onPlayAgain = {startGame(team1,team2)},
                    onBackToMenu = {navigateToStart()}

                )
            }
        }
        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewStartScreen() {
        FamiliadaTheme {
            StartScreen(
                onStartGame = { navigateToPlayersSelection() },
                onRules = { navigateToRules() },
                onSettings = { navigateToSettings() })
        }
    }
}