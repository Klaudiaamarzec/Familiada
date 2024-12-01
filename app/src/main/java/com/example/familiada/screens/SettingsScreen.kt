package com.example.familiada.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SettingsScreen(
    onBackToStart: () -> Unit,
    onThemeChange: (Boolean) -> Unit,
    context: Context
) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    var isDarkTheme by remember { mutableStateOf(sharedPreferences.getBoolean("isDarkTheme", false)) }
    var isSoundEnabled by remember { mutableStateOf(sharedPreferences.getBoolean("isSoundEnabled", false)) }
    var isTimeLimitEnabled by remember { mutableStateOf(sharedPreferences.getBoolean("isTimeLimitEnabled", false)) }
    var isMicEnabled by remember { mutableStateOf(sharedPreferences.getBoolean("isMicEnabled", false)) }

    val backgroundColor = MaterialTheme.colorScheme.background
    val headlineColor = MaterialTheme.colorScheme.primaryContainer
    val textColor = MaterialTheme.colorScheme.onBackground
    val switchThumbColor = MaterialTheme.colorScheme.primary

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
                text = "Ustawienia",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = headlineColor,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Motyw aplikacji (jasny/ciemny)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Motyw aplikacji",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = {
                        isDarkTheme = it
                        onThemeChange(it)
                        editor.putBoolean("isDarkTheme", it).apply()
                    },
                    modifier = Modifier.padding(start = 16.dp),
                    colors = SwitchDefaults.colors(checkedThumbColor = switchThumbColor)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dźwięki (włącz/wyłącz)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Dźwięki",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                Switch(
                    checked = isSoundEnabled,
                    onCheckedChange = {
                        isSoundEnabled = it
                        editor.putBoolean("isSoundEnabled", it).apply()
                    },
                    modifier = Modifier.padding(start = 16.dp),
                    colors = SwitchDefaults.colors(checkedThumbColor = switchThumbColor)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ograniczenia czasowe na odpowiedź (włącz/wyłącz)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ograniczenie czasowe",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                Switch(
                    checked = isTimeLimitEnabled,
                    onCheckedChange = {
                        isTimeLimitEnabled = it
                        editor.putBoolean("isTimeLimitEnabled", it).apply()
                    },
                    modifier = Modifier.padding(start = 16.dp),
                    colors = SwitchDefaults.colors(checkedThumbColor = switchThumbColor)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tryb odpowiedzi przez mikrofon (włącz/wyłącz)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Tryb mikrofonu",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                Switch(
                    checked = isMicEnabled,
                    onCheckedChange = {
                        isMicEnabled = it
                        editor.putBoolean("isMicEnabled", it).apply()
                    },
                    modifier = Modifier.padding(start = 16.dp),
                    colors = SwitchDefaults.colors(checkedThumbColor = switchThumbColor)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Przycisk powrotu do menu
            Button(
                onClick = { onBackToStart() },
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "POWRÓT DO MENU",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
