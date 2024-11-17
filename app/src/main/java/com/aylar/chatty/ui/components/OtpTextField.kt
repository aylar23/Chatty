package com.aylar.chatty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CharView(
    index: Int,
    text: String,
    isTextFieldError: Boolean,
    isFocused: Boolean
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemWidth = (screenWidth - 80.dp) / 6
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    val isCurrentChar = text.length == index
    val borderColor =
        if (isTextFieldError) MaterialTheme.colorScheme.error
        else if (char.isNotEmpty()) MaterialTheme.colorScheme.primary
        else if (isCurrentChar && isFocused) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondaryContainer

    Text(
        modifier = Modifier
            .width(itemWidth)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .border(
                1.dp, borderColor, MaterialTheme.shapes.medium
            )
            .padding(20.dp),
        text = char,
        color = MaterialTheme.colorScheme.onBackground,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        textAlign = TextAlign.Center
    )
}