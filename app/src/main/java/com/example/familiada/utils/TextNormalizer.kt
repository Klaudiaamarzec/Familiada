package com.example.familiada.utils

import java.text.Normalizer

fun normalizeText(text: String): String {
    return Normalizer.normalize(text, Normalizer.Form.NFD).replace(
        Regex("\\p{InCombiningDiacriticalMarks}"), ""
    ).lowercase()
}