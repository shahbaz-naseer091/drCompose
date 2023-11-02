package com.example.drCompose.utils

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.drCompose.koin.StringsUtls
import com.example.drCompose.screens.uri
import com.example.drCompose.ui.ui.theme.Blue40
import com.example.drCompose.utils.Constants.ACTION
import com.example.drCompose.utils.Constants.CAMERA
import com.example.drCompose.utils.Constants.DISMISS
import com.example.drCompose.utils.Constants.GALLERY
import com.example.driqpro.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.Calendar


object Extensions {

    @Composable
    fun DateSelectionDialog(onDateSelected: (String) -> Unit) {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            LocalContext.current,
            { _, year, month, dayOfMonth ->

                onDateSelected("${dayOfMonth}/${month + 1}/${year}")

            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    @Composable
    fun GenderSelectionDialog(source: StringsUtls, onDismiss: (String) -> Unit) {

        val items = stringArrayResource(id = R.array.genders).toList()

        Dialog(onDismissRequest = { }) {

            Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.background) {

                Column {

                    Text(
                        text = source.selectGender,
                        fontSize = 20.sp,
                        color = Blue40,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )


                    items.forEach { item ->

                        Text(
                            text = item,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable {
                                    onDismiss(item)
                                }
                        )

                    }
                }

            }


        }

    }

    @Composable
    fun AttachmentDialog(source: StringsUtls, onSelect: (Int) -> Unit) {

        val openDialog = remember { mutableStateOf(CAMERA) }

        AlertDialog(

            onDismissRequest = {
                openDialog.value = 0
                onSelect(openDialog.value)
            },
            title = {
                Text(text = source.addProfilePhoto)
            },
            text = {
                Text(source.selectImageFrom)
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = CAMERA
                        onSelect(openDialog.value)
                    }) {
                    Text("Camera")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = GALLERY
                        onSelect(openDialog.value)
                    }) {
                    Text("Gallery")
                }
            }
        )

    }


    fun TextFieldValue.isEmpty(): Boolean {
        return this.text.isEmpty()
    }

    fun Context.createImageFile(): Uri? {

        uri = createFileInAppDirectory("IMG_" + System.currentTimeMillis() + ".jpg")?.let {
            return FileProvider.getUriForFile(
                this, "$packageName.provider", it
            )
        }

        return uri
    }

    @Composable
    fun PermissionRequester(
        onPermissionResult: (Map<String, Boolean>) -> Unit
    ) {
        val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                val grantedPermissions = mutableMapOf<String, Boolean>()

                for ((permission, isGranted) in result) {
                    grantedPermissions[permission] = isGranted
                }

                onPermissionResult(grantedPermissions)
            }

        // Define the list of permissions you want to request
        val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            // Add more permissions as needed
        )

        // Request permissions when the composable is initially composed
        DisposableEffect(Unit) {
            requestPermissionLauncher.launch(permissions)
            onDispose { /* Cleanup if needed */ }
        }
    }


    @Composable
    fun SnackbarHostState.ShowMessage(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short, callback: (String) -> Unit = {}
    ) {


        LaunchedEffect(this) {
            val result = showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    // The action button of the snackbar was clicked
                    callback(ACTION)
                }

                SnackbarResult.Dismissed -> {
                    // The snackbar was dismissed by some other action
                    callback(DISMISS)
                }
            }
        }

    }

    fun Context.createAppDirectory(): File? {
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val appDirectory = File(downloadsDirectory, "drCompose")

        if (!appDirectory.exists()) {
            val directoryCreated = appDirectory.mkdir()
            if (!directoryCreated) {
                return null
            }
        }

        return appDirectory
    }

    fun Context.createFileInAppDirectory(fileName: String): File? {
        val appDirectory = createAppDirectory()
        if (appDirectory != null) {
            val file = File(appDirectory, fileName)
            try {
                if (!file.exists()) {
                    val fileCreated = file.createNewFile()
                    if (!fileCreated) {
                        return null
                    }
                }
                return file
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun Bitmap.base64(): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun Uri.bitmapToBase64(contentResolver: ContentResolver): Bitmap? {

        var bitmap: Bitmap? = null
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, this)
            } else {
                val source = ImageDecoder.createSource(contentResolver, this)
                ImageDecoder.decodeBitmap(source)
            }
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun Uri.uriToBitmap(contentResolver: ContentResolver): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(this, "r")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor (fileDescriptor)
            return image
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }


}

