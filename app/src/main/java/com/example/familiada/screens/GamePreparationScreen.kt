package com.example.familiada.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import kotlin.random.Random

@Composable
fun GamePreparationScreen(
    team1: List<String>,
    team2: List<String>,
    onStartGame: (List<String>, List<String>) -> Unit
){
    val shuffledTeam1 = remember { team1.shuffled(Random(System.currentTimeMillis())) }
    val shuffledTeam2 = remember { team2.shuffled(Random(System.currentTimeMillis())) }

    val backgroundColor = MaterialTheme.colorScheme.background
    val headlineColor = MaterialTheme.colorScheme.primaryContainer
    val textColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kolejność graczy",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = headlineColor,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Drużyna 1",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            )
            shuffledTeam1.forEachIndexed { index, player ->
                Text(
                    text = "${index + 1}. $player" ,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    ))
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Drużyna 2",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            )
            shuffledTeam2.forEachIndexed { index, player ->
                Text(
                    text = "${index + 1}. $player" ,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    ))
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onStartGame(shuffledTeam1, shuffledTeam2) },
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "ROZPOCZNIJ GRĘ",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}