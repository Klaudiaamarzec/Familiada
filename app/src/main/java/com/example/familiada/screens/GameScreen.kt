package com.example.familiada.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.familiada.controller.GameController
import com.example.familiada.ui.theme.FamiliadaTheme

@Composable
fun GameScreen(
    team1Players: List<String>,
    team2Players: List<String>,
    modifier: Modifier = Modifier,
    context: Context,
    isSoundEnabled: Boolean,
    isTimeLimitEnabled: Boolean,
    isMicEnabled: Boolean,
    onGameOver: (Int, Int) -> Unit
) {

    val gameController = remember {
        GameController(
            team1Players = team1Players,
            team2Players = team2Players,
            context = context,
            isSoundEnabled = isSoundEnabled,
            isTimeLimitEnabled = isTimeLimitEnabled
        )
    }

    if (gameController.isGameOver()) {
        val scoreTeam1 = gameController.getScoreTeam1()
        val scoreTeam2 = gameController.getScoreTeam2()
        onGameOver(scoreTeam1, scoreTeam2)
    }

    val teamSelectionButtonWidth = 140.dp
    val teamSelectionButtonHeight = 64.dp

    var answerText by remember { mutableStateOf("") }
    val question = gameController.getCurrentQuestion()
    val keyboardController = LocalSoftwareKeyboardController.current // Obsługa klawiatury

    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.primaryContainer
    val iconColor = MaterialTheme.colorScheme.primaryContainer
    val borderColor = MaterialTheme.colorScheme.primaryContainer
    var timeLimitEnabled = isTimeLimitEnabled
    var timeValue = gameController.remainingTime
    var selectedTeam = gameController.answeringTeam
    val micEnabled = isMicEnabled

    LaunchedEffect(question) {
        gameController.resetTimer()
    }

    val context = LocalContext.current

    val handleAnswerSubmit = {
        gameController.submitAnswer(answerText)
        answerText = ""
        keyboardController?.hide()

        gameController.resetTimer()
    }

    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                val spokenText =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        ?.firstOrNull()
                if (spokenText != null) {
                    answerText = spokenText
                    handleAnswerSubmit()
                } else {
                    Toast.makeText(context, "Nie udało się rozpoznać głosu", Toast.LENGTH_SHORT)
                        .show()
                }
            })


    Column(modifier = modifier
        .fillMaxSize()
        .background(backgroundColor)
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                keyboardController?.hide()
            })
        }) {
        // Nagłówek
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Drużyna
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Drużyna",
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = gameController.getCurrentTeamName(), style = TextStyle(
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
            }
            // Runda
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Numer rundy",
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Runda nr ${gameController.getRoundNumber()}", style = TextStyle(
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
            }
            // Punkty
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Punkty",
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${gameController.getScore()} pkt", style = TextStyle(
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
            }


        }

        HorizontalDivider(
            thickness = 1.dp, color = borderColor
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            question?.let {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        text = it.question, style = MaterialTheme.typography.bodyLarge.copy(
                            color = textColor, fontSize = 24.sp
                        ), modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        if (gameController.answeringTeam != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                Text(
                    text = "Teraz odpowiada: ${gameController.getPlayer()}",
                    style = TextStyle(
                        color = textColor, fontWeight = FontWeight.Bold, fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Pytanie
            question?.let {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Ramka z odpowiedziami
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .border(1.dp, borderColor)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(16.dp)
                                .fillMaxWidth(0.85f)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                it.answers.forEach { answer ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (gameController.revealedAnswers[answer.text] == true) answer.text else "..................",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = textColor, fontSize = 20.sp
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = if (gameController.revealedAnswers[answer.text] == true) "${answer.points}" else "",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = textColor, fontSize = 20.sp
                                            ),
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }

                        // X X X
                        Column(
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            repeat(gameController.getIncorrectAnswers()) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = iconColor,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
            } ?: run {
                Text(
                    text = "Ładowanie pytań...",
                    modifier = Modifier.fillMaxSize(),
                    style = MaterialTheme.typography.bodyLarge.copy(color = textColor)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (!micEnabled) {
                    // Jeżeli tryb mikrofonu wyłączony, pokaż pole do wpisania odpowiedzi
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = answerText,
                            onValueChange = { answerText = it },
                            label = { Text(text = "Wpisz odpowiedź", color = textColor) },
                            textStyle = TextStyle(color = textColor, fontSize = 20.sp),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background),
                            enabled = gameController.answeringTeam != null,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                autoCorrect = false
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                disabledContainerColor = MaterialTheme.colorScheme.background,
                                errorContainerColor = MaterialTheme.colorScheme.background
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                handleAnswerSubmit()
                            },
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Wyślij odpowiedź"
                            )
                        }
                    }

                } else {
                    // Odpowiadanie głosowe
                    Button(
                        onClick = {
                            // Sprawdzenie uprawnień
                            if (ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                                intent.putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                )
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pl-PL")
                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Mów teraz...")

                                // Jeżeli przyznano uprawnienia, uruchom rozpoznawanie mowy
                                speechRecognizerLauncher.launch(intent)

                            } else {
                                ActivityCompat.requestPermissions(
                                    context as Activity,
                                    arrayOf(Manifest.permission.RECORD_AUDIO),
                                    100
                                )
                            }
                        }, modifier = Modifier
                            .height(64.dp)
                            .width(172.dp)
                    ) {
                        Text("Mów")
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (gameController.answeringTeam == null) {
                        gameController.selectTeam(Team.FIRST)
                    }
                },
                enabled = gameController.answeringTeam == null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (gameController.answeringTeam == Team.FIRST) Color.Green else Color.LightGray
                ),
                modifier = Modifier
                    .height(teamSelectionButtonHeight)
                    .width(teamSelectionButtonWidth)
            ) {
                Text(
                    "Drużyna 1",
                    color = if (gameController.answeringTeam == Team.FIRST) Color.White else Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    if (gameController.answeringTeam == null) {
                        gameController.selectTeam(Team.SECOND)
                    }
                },
                enabled = gameController.answeringTeam == null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (gameController.answeringTeam == Team.SECOND) Color.Blue else Color.LightGray
                ),
                modifier = Modifier
                    .height(teamSelectionButtonHeight)
                    .width(teamSelectionButtonWidth)
            ) {
                Text(
                    "Drużyna 2",
                    color = if (gameController.answeringTeam == Team.SECOND) Color.White else Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (timeLimitEnabled && selectedTeam !== null) {
                Text(
                    text = "Czas: $timeValue s", style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    val context = LocalContext.current
    FamiliadaTheme {
        GameScreen(modifier = Modifier.fillMaxSize(),
            context = context,
            team1Players = listOf("Nile", "Amazon", "Yangtze"),
            team2Players = listOf("Nile", "Amazon", "Yangtze"),
            isTimeLimitEnabled = true,
            isSoundEnabled = true,
            isMicEnabled = true,
            onGameOver = { scoreTeam1, scoreTeam2 ->
                {}
            })
    }
}