package com.example.familiada

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.familiada.data.Question
import com.example.familiada.utils.loadQuestions
import androidx.compose.ui.platform.LocalContext
import com.example.familiada.ui.theme.FamiliadaTheme
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
    var question by remember { mutableStateOf<Question?>(null) }
    val context = LocalContext.current
    var answerText by remember { mutableStateOf("") }
    var revealedAnswers by remember { mutableStateOf(mutableMapOf<String, Boolean>()) } // Mapa odpowiedzi - true jeśli odkryta, false jeśli ukryta
    val teamName = "Drużyna1"
    var score by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }
    val keyboardController = LocalSoftwareKeyboardController.current // Obsługa klawiatury

    // Ładowanie pytań i ustawienie losowego pytania
    LaunchedEffect(key1 = context) {
        val questions = loadQuestions(context)
        question = questions.random()
        revealedAnswers = question?.answers?.associate { it.text to false }?.toMutableMap() ?: mutableMapOf()
    }

    if (incorrectAnswers >= 3) {
        return
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(Color.Black)
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                keyboardController?.hide() // Ukryj klawiaturę po kliknięciu ekranu
            })
        }
    ) {

        // Nagłówek
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            // Ikona sylwetki drużyny
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Drużyna",
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = teamName,
                    style = TextStyle(
                        color = Color.Yellow,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
            }
            // Ikona pucharu i punkty
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Punkty",
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$score pkt",
                    style = TextStyle(
                        color = Color.Yellow,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
            }
        }

        Divider(
            color = Color.Yellow,
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wyświetlanie pytania
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
                        // Ikony X
                        repeat(incorrectAnswers) {
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
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clickable { // Ukrycie klawiatury po dotknięciu
                    keyboardController?.hide()
                },
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
                    question?.answers?.find { it.text.equals(answerText, ignoreCase = true) }?.let { correctAnswer ->
                        revealedAnswers[correctAnswer.text] = true
                        score += correctAnswer.points
                    } ?: run {
                        incorrectAnswers += 1
                    }

                    if (incorrectAnswers >= 3) {
                        println("Koniec gry! Zbyt wiele błędnych odpowiedzi.")
                    }

                    answerText = ""
                    keyboardController?.hide()
                } ,
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
    FamiliadaTheme {
        GameScreen(modifier = Modifier.fillMaxSize())
    }
}