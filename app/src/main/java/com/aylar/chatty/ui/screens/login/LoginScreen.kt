package com.aylar.chatty.ui.screens.login

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.aylar.chatty.R
import com.aylar.chatty.ui.components.CustomTextField
import com.aylar.chatty.ui.components.countryPhoneCodes
import com.aylar.chatty.ui.screens.login.components.CountrySelectionDialog


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onContinue: (String) -> Unit
) {

    val uiState by loginViewModel.uiState.collectAsState()

    val code = uiState.code
    val phoneNumber = uiState.phone
    val selectedCountry = uiState.country

    var showCountrySelectionDialog by remember { mutableStateOf(false) }

    val focusRequesterCountryCode = FocusRequester()
    val focusRequesterPhone = FocusRequester()

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = stringResource(R.string.your_phone_number),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.your_phone_number_desc),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(0.7f),
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.shapes.small
                    )
                    .clip(MaterialTheme.shapes.small)
                    .clickable { showCountrySelectionDialog = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCountry == null) {
                    Text(
                        text = "Select Country",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .weight(1f)
                    )

                } else {
                    Text(
                        text = selectedCountry?.flagEmoji ?: "",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = selectedCountry?.countryName ?: "Unsupported Country",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "ArrowForward",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.shapes.small
                    )
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = code,
                    onValueChange = {
                        if (it.length < 5) loginViewModel.updateCode(it)
                        val country = countryPhoneCodes.find { country -> country.code == it }
                        if (country != null) {
                            loginViewModel.updateCountry(country)
                            focusRequesterPhone.requestFocus()
                        }
                    },
                    prefix = {
                        Text(text = "+")
                    },
                    maxLines = 1,
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier
                        .width(5 * 10.dp)
                        .focusRequester(focusRequesterCountryCode)
                )


                VerticalDivider(
                    Modifier
                        .height(20.dp)
                        .padding(horizontal = 10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                CustomTextField(
                    value = phoneNumber,
                    onValueChange = { input ->
                        if (code.isEmpty()) {
                            focusRequesterCountryCode.requestFocus()
                            loginViewModel.updateCode(input)
                        } else {
                            loginViewModel.updatePhone(input)
                        }
                        if (input.isEmpty()) focusRequesterCountryCode.requestFocus()


                    },
                    maxLines = 1,
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.focusRequester(focusRequesterPhone)
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.errorMessage != null){
                Text(
                    text = stringResource(id = uiState.errorMessage!!),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            FloatingActionButton(
                onClick = {
                    loginViewModel.login("+$code$phoneNumber") {
                        onContinue("+$code$phoneNumber")
                        Log.e("TAG", "LoginScreen: onContinue", )
                    }
                },
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.End)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(26.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Submit",
                    )
                }
            }
        }

        if (showCountrySelectionDialog) {
            Dialog(
                onDismissRequest = { showCountrySelectionDialog = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                CountrySelectionDialog(
                    countries = countryPhoneCodes.reversed(),
                    onCountrySelected = { country ->
                        showCountrySelectionDialog = false
                        loginViewModel.updateCode(country.code)
                        loginViewModel.updateCountry(country)
                        focusRequesterPhone.requestFocus()

                    }
                )
            }
        }
    }
}
