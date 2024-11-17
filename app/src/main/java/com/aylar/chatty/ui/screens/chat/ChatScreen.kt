package com.aylar.chatty.ui.screens.chat


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aylar.chatty.ui.theme.DarkMessageReceived
import com.aylar.chatty.ui.theme.DarkMessageSent
import com.aylar.chatty.ui.theme.LightMessageReceived
import com.aylar.chatty.ui.theme.LightMessageSent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onNavigateUp: () -> Unit
) {
    val messageList = remember {
        mutableStateListOf(
            Message("Hello!", "John", "12:30 PM"),
            Message("How are you?", "John", "12:35 PM"),
            Message("I'm doing well, thanks!", "John", "12:40 PM")
        )
    }

    var messageText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "John",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Submit",
                        )
                    }

                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .imePadding()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                reverseLayout = true,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messageList) { message ->
                    MessageItem(message = message)
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surface)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type a message") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.large,
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(onClick = {
                    if (messageText.isNotEmpty()) {
                        messageList.add(0, Message(messageText, "You", "Just Now"))
                        messageText = ""
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val isDarkTheme = isSystemInDarkTheme()

    val backgroundColor = when {
        message.sender == "You" && !isDarkTheme -> LightMessageSent
        message.sender == "You" && isDarkTheme -> DarkMessageSent
        else -> if (isDarkTheme) DarkMessageReceived else LightMessageReceived
    }

    val textColor = if (message.sender != "You" && !isDarkTheme) {
        Color.Black
    } else {
        Color.White
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.sender == "You") {
            Arrangement.End
        } else {
            Arrangement.Start
        },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier
                .padding(4.dp),
            color = backgroundColor,
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp, 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}

data class Message(
    val content: String,
    val sender: String,
    val timestamp: String
)