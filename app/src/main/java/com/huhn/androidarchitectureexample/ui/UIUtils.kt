package com.huhn.androidarchitectureexample.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp


@Composable
fun UnderlinedText(textString: String) {
    Text(
        text = textString,
        textDecoration = TextDecoration.Underline,
        fontSize = 20.sp,
    )
}