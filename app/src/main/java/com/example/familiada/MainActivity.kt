package com.example.familiada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.familiada.ui.theme.FamiliadaTheme

class MainActivity : ComponentActivity() {
    private var currentScreen: @Composable () -> Unit = { StartScreen(onStartGame = { startGame() }) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FamiliadaTheme {
                currentScreen()
            }
        }
    }

    private fun startGame() {
        currentScreen = {
            FamiliadaTheme {
                GameScreen()
            }
        }
        setContent {
            FamiliadaTheme {
                currentScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewStartScreen() {
        FamiliadaTheme {
            StartScreen(onStartGame = { startGame() })
        }
    }
}