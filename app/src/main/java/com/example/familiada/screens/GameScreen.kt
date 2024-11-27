package com.example.familiada.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.familiada.ui.theme.FamiliadaTheme
import com.example.familiada.controller.GameController

@SuppressLint("MutableCollectionMutableState")
@Composable
fun GameScreen(modifier: Modifier = Modifier, context: Context) {

    val gameController = remember { GameController(context) }
    var answerText by remember { mutableStateOf("") }
    val question = gameController.getCurrentQuestion()
    var revealedAnswers by remember { mutableStateOf(mutableMapOf<String, Boolean>()) } // Mapa odpowiedzi - true jeśli odkryta, false jeśli ukryta
    val keyboardController = LocalSoftwareKeyboardController.current // Obsługa klawiatury

    Column(modifier = modifier
        .fillMaxSize()
        .background(Color.Black)
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                keyboardController?.hide()
            })
        }
    ) {

        // Nagłówek
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            // Drużyna
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Drużyna",
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = gameController.getCurrentTeam(),
                    style = TextStyle(
                        color = Color.Yellow,
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
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${gameController.getScore()} pkt",
                    style = TextStyle(
                        color = Color.Yellow,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Yellow
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pytanie
        question?.let {
            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = it.question,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Yellow, fontSize = 24.sp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Ramka z odpowiedziami
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .border(1.dp, Color.Yellow)
                            .clip(RoundedCornerShape(16.dp))
                            .padding(16.dp)
                            .fillMaxWidth(0.85f),
                        color = Color.Black
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            it.answers.forEach { answer ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (revealedAnswers[answer.text] == true) answer.text else "..................",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Yellow, fontSize = 20.sp),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = if (revealedAnswers[answer.text] == true) "${answer.points}" else "",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Yellow, fontSize = 20.sp),
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    // X X X
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        repeat(gameController.getIncorrectAnswers()) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = Color.Yellow,
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
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Yellow)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = answerText,
                onValueChange = { answerText = it },
                label = { Text(text = "Wpisz odpowiedź", color = Color.Yellow) },
                textStyle = TextStyle(color = Color.Yellow, fontSize = 20.sp),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.Yellow, RoundedCornerShape(8.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {

                    val result = gameController.submitAnswer(answerText)

                    if(result) {
                        revealedAnswers[answerText] = true
                    }

                    answerText = ""
                    keyboardController?.hide()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Wyślij odpowiedź",
                    tint = Color.Black
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
        GameScreen(modifier = Modifier.fillMaxSize(), context = context)
    }
}