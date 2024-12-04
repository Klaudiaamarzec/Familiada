package com.example.familiada.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun PlayersSelectionScreen(onPlayersConfirmed: (List<String>, List<String>) -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground
    val maxPlayers = 5

    var team1Size by remember { mutableIntStateOf(2) }
    var team2Size by remember { mutableIntStateOf(2) }

    var team1Names by remember { mutableStateOf(List(maxPlayers) { "" }) }
    var team2Names by remember { mutableStateOf(List(maxPlayers) { "" }) }

    var showError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text("Wybierz liczbę graczy dla każdej drużyny:", style = MaterialTheme.typography.bodyLarge.copy(color = textColor))
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Drużyna 1:", style = MaterialTheme.typography.bodyLarge.copy(color = textColor))
                Slider(
                    value = team1Size.toFloat(),
                    onValueChange = { team1Size = it.toInt() },
                    valueRange = 1f..maxPlayers.toFloat(),
                    steps = maxPlayers - 1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text("$team1Size")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Drużyna 2:", style = MaterialTheme.typography.bodyLarge.copy(color = textColor))
                Slider(
                    value = team2Size.toFloat(),
                    onValueChange = { team2Size = it.toInt() },
                    valueRange = 1f..maxPlayers.toFloat(),
                    steps = maxPlayers - 1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text("$team2Size")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Imiona graczy Drużyny 1:", style = MaterialTheme.typography.bodyLarge.copy(color = textColor))
            Spacer(modifier = Modifier.height(8.dp))
            for (i in 0 until team1Size) {
                OutlinedTextField(
                    value = team1Names[i],
                    onValueChange = { name ->
                        team1Names = team1Names.toMutableList().apply { set(i, name) }
                    },
                    label = { Text("Gracz ${i + 1}") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Imiona graczy Drużyny 2:", style = MaterialTheme.typography.bodyLarge.copy(color = textColor))
            Spacer(modifier = Modifier.height(8.dp))
            for (i in 0 until team2Size) {
                OutlinedTextField(
                    value = team2Names[i],
                    onValueChange = { name ->
                        team2Names = team2Names.toMutableList().apply { set(i, name) }
                    },
                    label = { Text("Gracz ${i + 1}") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showError) {
                Text(
                    text = "Wszystkie pola muszą być wypełnione!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (team1Names.take(team1Size).any { it.isBlank() } ||
                        team2Names.take(team2Size).any { it.isBlank() }
                    ) {
                        showError = true
                    } else {
                        showError = false
                        onPlayersConfirmed(
                            team1Names.take(team1Size),
                            team2Names.take(team2Size)
                        )
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("ZATWIERDŹ GRACZY")
            }
        }
    }
}