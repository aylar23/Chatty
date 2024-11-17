package com.aylar.chatty.ui.screens.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogsScreen(
    navigateToChat :() -> Unit
) {

    val chatList = listOf(
        Chat("John Doe", "Hello there!", "12:30 PM"),
        Chat("Jane Smith", "Good morning!", "09:15 AM"),
        Chat("Alex Johnson", "Are you free today?", "06:45 PM"),
        Chat("Emma Williams", "Can you send me the report?", "03:00 PM"),
        Chat("Michael Brown", "Let's meet at 5.", "01:45 PM"),
        Chat("Sophia Davis", "The meeting is scheduled for 2 PM.", "02:10 PM"),
        Chat("Daniel Martinez", "Do you have any updates?", "11:30 AM"),
        Chat("Olivia Garcia", "How about dinner tonight?", "07:00 PM"),
        Chat("Liam Wilson", "Just finished the task you assigned.", "04:45 PM"),
        Chat("Ava Lee", "I need help with the project.", "08:30 AM"),
        Chat("James Taylor", "Can we discuss the new plan?", "05:15 PM"),
        Chat("Mason Anderson", "Meeting rescheduled to 3 PM.", "10:00 AM"),
        Chat("Isabella Thomas", "Great job on the presentation!", "02:30 PM"),
        Chat("Ethan Jackson", "Let's catch up soon.", "05:45 PM"),
        Chat("Charlotte White", "Do you have time to talk?", "12:00 PM"),
        Chat("Amelia Harris", "When is the deadline for this?", "01:00 PM"),
        Chat("Benjamin Clark", "Working on the design.", "09:30 AM"),
        Chat("Harper Lewis", "I'll be back after lunch.", "12:45 PM"),
        Chat("Jack Walker", "Can we move the meeting?", "04:00 PM"),
        Chat("Grace Hall", "Sent you the files.", "03:30 PM")
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chats",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(chatList) { chat ->
                ChatItem(chat = chat){
                    navigateToChat()
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, onClick:() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Gray, CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = chat.name, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = chat.lastMessage, style = MaterialTheme.typography.bodySmall)
        }

        Text(
            modifier = Modifier.padding(top = 8.dp).align(Alignment.Top),
            text = chat.time,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

data class Chat(
    val name: String,
    val lastMessage: String,
    val time: String
)