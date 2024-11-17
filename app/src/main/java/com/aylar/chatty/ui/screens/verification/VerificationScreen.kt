package com.aylar.chatty.ui.screens.verification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aylar.chatty.R
import com.aylar.chatty.ui.components.CharView

@Composable
fun CodeVerificationScreen(
    verifyViewModel: VerificationViewModel,
    phoneNumber: String,
    logIn: () -> Unit,
    register: () -> Unit
) {

    val uiState by verifyViewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(phoneNumber) {
        verifyViewModel.updatePhone(phoneNumber)
    }

    LaunchedEffect(uiState) {
        uiState.errorMessage?.let {
            snackBarHostState.showSnackbar(
                message = it,
            )
            verifyViewModel.updateToDefault()
        }
    }

    val resendAllowed = verifyViewModel.resendAllowed
    val resendTime = verifyViewModel.timeLeftToResend

    var isTextFieldError by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        },
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(id = R.string.verification),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.enter_code),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            BasicTextField(
                modifier = Modifier.onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
                value = uiState.code,
                onValueChange = {
                    if (it.length > 7) return@BasicTextField
                    isTextFieldError = false
                    verifyViewModel.updateCode(it)
                    if (it.length == 6) verifyViewModel.checkAuthCode(
                        phoneNumber,
                        it,
                        onSuccess = { isUserExists ->
                            if (isUserExists)logIn() else register()
                        },
                        onFailure = {
                            isTextFieldError = true
                        }
                    )
                },
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.Center) {
                        repeat(6) { index ->
                            CharView(
                                index = index, text = uiState.code, isTextFieldError, isFocused
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                },

                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }),
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        uiState.updateCode("")
                        verifyViewModel.resendCode(phoneNumber)
                    },
                textAlign = TextAlign.Center,
                text = if (resendAllowed) stringResource(id = R.string.resend) else resendTime,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

        }

        if (uiState.isLoading) {
            Dialog(
                onDismissRequest = { },
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}