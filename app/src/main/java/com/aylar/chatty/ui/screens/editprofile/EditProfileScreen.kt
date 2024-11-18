package com.aylar.chatty.ui.screens.editprofile

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.aylar.chatty.R
import com.aylar.chatty.di.AppModule.Companion.BASE_URL
import com.aylar.chatty.domain.model.UploadImage
import com.aylar.chatty.ui.components.CustomDatePickerDialog
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel,
    onNavigateUp: () -> Unit
) {
    val uiState by editProfileViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    var isNameFieldError by remember {
        mutableStateOf(false)
    }
    var isBirthdayFieldError by remember {
        mutableStateOf(false)
    }
    var savedFile by remember {
        mutableStateOf<File?>(null)
    }
    val isNameValid = uiState.name.isNotEmpty()
    val isBirthdayValid = uiState.birthday.isNotEmpty()

    var showDatePickerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {

                val destinationFile = File(context.cacheDir, "selected_image.jpg")
                savedFile = saveFileFromUri(context, uri, destinationFile.absolutePath)
                savedFile?.let { file ->
                    editProfileViewModel.updateAvatar(file.absolutePath)
                }
            }
        }
    )
    LaunchedEffect(uiState) {
        uiState.errorMessage?.let {
            snackBarHostState.showSnackbar(
                message = it,
            )
            editProfileViewModel.updateToDefault()
        }
    }

    LaunchedEffect(uiState) {
        uiState.errorMessage?.let {
            snackBarHostState.showSnackbar(
                message = it,
            )
            editProfileViewModel.updateToDefault()
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.edit_profile)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (!isNameValid) {
                            isNameFieldError = true
                        } else if (!isBirthdayValid) {
                            isBirthdayFieldError = true
                        } else {
                            editProfileViewModel.updateUser(
                                uiState.username,
                                uiState.name,
                                uiState.birthday,
                                uiState.city,
                                savedFile?.let { createUploadImageFromFile(it) }
                            ) {
                                onNavigateUp()
                            }
                        }

                    }) {
                        Icon(
                            Icons.Default.Check,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
    ) { paddding ->
        Column(
            modifier = Modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(paddding)
                .padding(horizontal = 16.dp),
        ) {

            if (savedFile != null){
                AsyncImage(
                    model = savedFile?.absolutePath?.let { filePath ->
                        File(filePath).takeIf { it.exists() }?.let { Uri.fromFile(it) }
                    } ?: "$BASE_URL/${uiState.avatar}",
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { launcher.launch("image/*") }
                )
            }else{
                AsyncImage(
                    model = "$BASE_URL/${uiState.avatar}",
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { launcher.launch("image/*") }
                )
            }


            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = uiState.username
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "+" + uiState.phone,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.name),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = uiState.name,
                onValueChange = { editProfileViewModel.updateName(it) },
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium,
                placeholder = { Text(stringResource(R.string.name), color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )

            if (isNameFieldError) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.this_field_must_not_be_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.birthday),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = uiState.birthday,
                onValueChange = { editProfileViewModel.updateBirthday(it) },
                enabled = false,
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium,
                placeholder = { Text(stringResource(R.string.birthday), color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePickerDialog = true },
                readOnly = true
            )

            if (isBirthdayFieldError) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.this_field_must_not_be_empty),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.city),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = uiState.city,
                onValueChange = { editProfileViewModel.updateCity(it) },
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium,
                placeholder = { Text(stringResource(R.string.city), color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))


        }

        if (showDatePickerDialog) {
            CustomDatePickerDialog(
                label = stringResource(id = R.string.birthday),
                onDismissRequest = {
                    showDatePickerDialog = false
                },
                onSelect = { birthday ->
                    editProfileViewModel.updateBirthday(birthday)
                }
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

fun saveFileFromUri(context: Context, uri: Uri, destinationPath: String): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(destinationPath)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun createUploadImageFromFile(file: File): UploadImage {
    val fileBytes = file.readBytes()
    val base64String = Base64.encodeToString(fileBytes, Base64.NO_WRAP)
    return UploadImage(
        filename = file.name,
        base64 = base64String
    )
}