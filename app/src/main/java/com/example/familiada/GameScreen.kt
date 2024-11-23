package com.example.familiada

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    val teamName = "Drużyna1" // Przykładowa drużyna
    val score = 0 // Przykładowa liczba punktów
    val keyboardController = LocalSoftwareKeyboardController.current // Do obsługi klawiatury

    // Ładowanie pytań i ustawienie losowego pytania
    LaunchedEffect(key1 = context) {
        val questions = loadQuestions(context)
        question = questions.random()  // Losowanie pytania
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(Color.Black)) {

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
                Spacer(modifier = Modifier.width(8.dp)) // Odstęp między ikoną a tekstem
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
                Spacer(modifier = Modifier.width(8.dp)) // Odstęp między ikoną a tekstem
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

        Spacer(modifier = Modifier.height(16.dp)) // Odstęp między nagłówkiem a pytaniem

        // Wyświetlanie pytania
        question?.let {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Pytanie wyświetlane przed ramką
                Text(
                    text = it.question,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Yellow, fontSize = 24.sp), // Kolor pytania na żółty
                    modifier = Modifier.padding(horizontal = 16.dp) // Padding od lewej i prawej
                )

                // Ramka z odpowiedziami
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.Center) // Wyśrodkowanie kontenera
                            .border(1.dp, Color.Yellow) // Ramka wokół kontenera
                            .clip(RoundedCornerShape(16.dp)) // Zaokrąglenie rogów
                            .padding(16.dp),
                        color = Color.Black
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            it.answers.forEach { answer ->
                                Text(
                                    text = "${answer.text} - ${answer.points} punktów",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Yellow, fontSize = 20.sp) // Kolor odpowiedzi na żółty
                                )
                                Spacer(modifier = Modifier.height(8.dp)) // Odstęp między odpowiedziami
                            }
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

        // Ukrywanie klawiatury, gdy klikniesz poza polem tekstowym
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            keyboardController?.hide() // Ukrycie klawiatury po kliknięciu
                        }
                    )
                }
        ) {
            Surface(
                modifier = Modifier
                    .border(1.dp, Color.Yellow) // Żółta ramka wokół pola tekstowego
                    .clip(RoundedCornerShape(16.dp)) // Zaokrąglenie rogów
                    .padding(8.dp),
                color = Color.Black // Tło czarne
            ) {
                TextField(
                    value = answerText,
                    onValueChange = { answerText = it },
                    label = { Text(text = "Wpisz odpowiedź") },
                    textStyle = TextStyle(color = Color.Yellow, fontSize = 20.sp),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide() // Ukrycie klawiatury po zakończeniu edytowania
                        }
                    )
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