package com.example.familiada.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RulesScreen(
    onBackToStart: () -> Unit
) {

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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Zasady gry",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = headlineColor,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 25.dp, bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "1. W grze udział biorą dwie drużyny złożone z maksymalnie 5 graczy.\n" +
                            "2. Celem gry jest udzielanie takich odpowiedzi, na które wskazało najwięcej ankietowanych osób.\n" +
                            "3. W każdej rundzie drużyna, która pierwsza nacisnie przycisk, odpowiada.\n" +
                            "4. Odpowiedzi udziela się poprzez użycie mikrofonu lub wpisanie jej w odpowiednie pole.\n" +
                            "5. Za udzielenie poprawnej odpowiedzi do puli trafia przypisana jej liczba punktów.\n" +
                            "6. Jeżeli drużyna odgadnie wszystkie odpowiedzi zdobywa punkty z puli.\n" +
                            "7. Jeżeli drużyna udzieli trzykortnie błędnej odpowiedzi pytanie przechodzi na drużynę przeciwną.\n" +
                            "8. Przy poprawnej odpowiedzi drużyna zgrania punkty z puli, w przeciwnym wypadku punkty zdobywają przeciwnicy.\n" +
                            "9. Gra kończy się po 10 rundach. Wygrywa drużyna która uzyska większy wynik.\n",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

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