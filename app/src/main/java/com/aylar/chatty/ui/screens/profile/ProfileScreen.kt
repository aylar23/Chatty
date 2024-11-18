package com.aylar.chatty.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aylar.chatty.R
import com.aylar.chatty.domain.model.UserProfileSend


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateUp: () -> Unit
) {
    val uiState by profileViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Edit,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "AccountCircle",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },

        ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.isFailure -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.network_error),
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    profileViewModel.getProfile()
                                },
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.retry),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }


                }

                uiState.isSuccess -> {
                    uiState.profileData?.let { ProfileContent(it) }
                }
            }
        }
    }
}

@Composable
fun ProfileContent(user: UserProfileSend) {


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = user.avatars?.avatar ?: "",
            contentDescription = "User Avatar",
            modifier = Modifier
                .padding(20.dp)
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        )

        Text(text = user.username)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.phone))
                Text(
                    text = "+${user.phone}",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.name))
                Text(
                    text = user.name,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.city))
                Text(
                    text = user.city ?: "-",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.birthday))
                Text(
                    text = user.birthday ?: "-",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.zodiac_sign))
                Text(
                    text = user.birthday?.let { getZodiacSign(it) } ?: "-",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

fun getZodiacSign(birthDate: String): String? {
    val (year, month, day) = birthDate.split("-").map { it.toInt() }
    return when {
        (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Aquarius"
        (month == 2 && day >= 19) || (month == 3 && day <= 20) -> "Pisces"
        (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "Aries"
        (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "Taurus"
        (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "Gemini"
        (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "Cancer"
        (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "Leo"
        (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "Virgo"
        (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "Libra"
        (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Scorpio"
        (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Sagittarius"
        (month == 12 && day >= 22) || (month == 1 && day <= 19) -> "Capricorn"
        else -> null
    }
}