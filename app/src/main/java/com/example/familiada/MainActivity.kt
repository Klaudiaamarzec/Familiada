package com.example.familiada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.familiada.screens.GameScreen
import com.example.familiada.screens.StartScreen
import com.example.familiada.screens.RulesScreen
import com.example.familiada.screens.SettingsScreen
import com.example.familiada.ui.theme.FamiliadaTheme

class MainActivity : ComponentActivity() {
    private var currentScreen: @Composable () -> Unit = { StartScreen(onStartGame = { startGame() }, onRules = { navigateToRules() }, onSettings = { navigateToSettings() }) }
    private var isDarkThemeState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isDarkThemeState.value = sharedPreferences.getBoolean("isDarkTheme", false)

        setContent {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                currentScreen()
            }
        }
    }

    private fun startGame() {
        currentScreen = {
            FamiliadaTheme(darkTheme = isDarkThemeState.value) {
                val context = LocalContext.current
                GameScreen(context = context)
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
                StartScreen(onStartGame = { startGame() }, onRules = { navigateToRules() }, onSettings = { navigateToSettings() })
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
                RulesScreen()
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

    @Preview(showBackground = true)
    @Composable
    fun PreviewStartScreen() {
        FamiliadaTheme {
            StartScreen(onStartGame = { startGame() }, onRules = { navigateToRules() }, onSettings = { navigateToSettings() })
        }
    }
}