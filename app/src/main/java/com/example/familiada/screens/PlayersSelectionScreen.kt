package com.example.familiada.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

@Composable
fun PlayersSelectionScreen(onPlayersConfirmed: (List<String>, List<String>) -> Unit) {
    // tylko do testow dla GamePreparationScreen
    val defaultPlayerNames1 = listOf("Jan", "Anna", "Krzysztof", "Maria")
    val defaultPlayerNames2 = listOf("Adam", "Agata", "Maciej", "Mariola")

    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = {
                    onPlayersConfirmed(defaultPlayerNames1, defaultPlayerNames2)
                },
                modifier = Modifier.padding(16.dp),
            ) {
                Text("ZATWIERDŹ GRACZY")
            }

        }
    }
}
