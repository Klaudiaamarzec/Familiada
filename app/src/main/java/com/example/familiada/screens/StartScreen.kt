package com.example.familiada.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun StartScreen(
    onStartGame: () -> Unit,
    onRules: () -> Unit,
    onSettings: () -> Unit
    ) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val headlineColor = MaterialTheme.colorScheme.primaryContainer
    val buttonBackgroundColor = MaterialTheme.colorScheme.primary
    val buttonTextColor = MaterialTheme.colorScheme.onPrimary

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
                text = "Familiada",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = headlineColor,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onStartGame() },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackgroundColor,
                    contentColor = buttonTextColor
                )
            ) {
                Text(
                    text = "NOWA GRA",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = { onRules() },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackgroundColor,
                    contentColor = buttonTextColor
                )
            ) {
                Text(
                    text = "ZASADY GRY",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = { onSettings() },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackgroundColor,
                    contentColor = buttonTextColor
                )
            ) {
                Text(
                    text = "USTAWIENIA",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }
}