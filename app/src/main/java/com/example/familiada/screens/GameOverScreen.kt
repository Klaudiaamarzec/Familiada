package com.example.familiada.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GameOverScreen(
    scoreTeam1: Int,
    scoreTeam2: Int,
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val headlineColor = MaterialTheme.colorScheme.primaryContainer
    val textColor = MaterialTheme.colorScheme.onBackground

    val winningText: String = when {
        scoreTeam1 > scoreTeam2 -> "Wygrywa Drużyna 1!"
        scoreTeam2 > scoreTeam1 -> "Wygrywa Drużyna 2!"
        else -> "Remis!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Koniec gry!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = headlineColor,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Display scores
            Text(
                text = "Wynik Drużyny 1: $scoreTeam1",
                style = MaterialTheme.typography.bodyLarge.copy(color = textColor)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Wynik Drużyny 2: $scoreTeam2",
                style = MaterialTheme.typography.bodyLarge.copy(color = textColor)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Display winner
            Text(
                text = winningText,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Button(
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("ZAGRAJ PONOWNIE")
            }
            Button(
                onClick = onBackToMenu,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("POWRÓT DO MENU")
            }
        }
    }
}