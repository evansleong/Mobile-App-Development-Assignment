package com.example.travelerapp.ui.theme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text

fun Modifier.textHeadingStyle() =
    this.fillMaxWidth()
        .padding(16.dp)

@Composable
fun HeadingTxt(text: String) {
    Text(
        modifier = Modifier.textHeadingStyle(),
        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        text = text
    )
}
