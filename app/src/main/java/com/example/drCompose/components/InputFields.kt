package com.example.drCompose.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.drCompose.ui.ui.theme.Black
import com.example.drCompose.ui.ui.theme.Blue40
import com.example.drCompose.ui.ui.theme.Grey
import com.example.driqpro.R

@Composable
fun Appbar(
    iconVisibility: Boolean = false,
    title: String = "",
    endImage: Int = 0,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(Grey),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {


            if (iconVisibility) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back Arrow",
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            onClick()
                        },
                )
            }


            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(start = 10.dp, end = 10.dp)
                    .align(Alignment.CenterVertically),
                color = Blue40,
                fontSize = 18.sp
            )


            endImage?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "NHS Logo",
                    modifier = Modifier
                        .padding(end = 8.dp),
                    alignment = Alignment.CenterEnd
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    hint: String,
    isEnabled: Boolean = true,
    modifier: Modifier,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false
) {

    var inputValue by remember { mutableStateOf(TextFieldValue()) }

    OutlinedTextField(
        value = inputValue,
        onValueChange = {
            inputValue = it
            onTextChanged(it.text)
        },
        placeholder = { Text(hint) },
        textStyle = TextStyle(fontSize = 16.sp, color = Black),
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        singleLine = true,
        enabled = isEnabled,
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInputField(
    modifier: Modifier = Modifier,
    hint: String,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false
) {

    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }


    val visualTransformation =
        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
            onTextChanged(it.text)
        },
        textStyle = TextStyle(fontSize = 16.sp, color = Black),
        placeholder = { Text(hint) },
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        singleLine = true,
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordVisible) Icon(
                painter = painterResource(id = R.drawable.ic_password_visible),
                contentDescription = "Password Visible",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(25.dp)
                    .height(25.dp)
                    .clickable {
                        passwordVisible = !passwordVisible
                    }
            ) else Icon(
                painter = painterResource(id = R.drawable.ic_password_hide),
                contentDescription = "Password Hide",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(25.dp)
                    .height(25.dp)
                    .clickable {
                        passwordVisible = !passwordVisible
                    }
            )
        },
    )
}

@Composable
fun HeadingInputField(
    modifier: Modifier = Modifier,
    heading: String,
    hint: String,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)

    ) {

        Text(
            text = heading,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth(),
            fontSize = 16.sp,
            color = Blue40,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
        )

        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            hint = hint,
            errorStatus = errorStatus,
            onTextChanged = { text ->
                onTextChanged(text)
            }
        )

    }

}

@Composable
fun HeadingInputPassword(
    modifier: Modifier = Modifier,
    heading: String,
    hint: String,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)

    ) {

        Text(
            text = heading,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth(),
            fontSize = 16.sp,
            color = Blue40,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
        )

        PasswordInputField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            hint = hint,
            errorStatus = errorStatus,
            onTextChanged = { text ->
                onTextChanged(text)
            }
        )

    }

}


@Composable
fun SelectionInputField(
    heading: String,
    hint: String,
    modifier: Modifier = Modifier,
    callback: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .then(modifier)
            .clickable { callback() }
    ) {

        Text(
            text = heading,
            modifier = Modifier
                .padding(bottom = 2.dp)
                .fillMaxWidth(),
            fontSize = 16.sp,
            color = Blue40,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
        )

        IqButton(
            text = hint,
            align = TextAlign.Start,
            onClick = {
                callback()
            })
    }
}

@Composable
fun ProfilePhoto(title: String, uri: Uri?, callback: () -> Unit = {}) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { callback() }) {

        if (uri?.path?.isNotEmpty() == true) {
            rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = uri)
                    .build()
            )
        } else painterResource(id = R.drawable.ic_user)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
            .build(),
        contentDescription = "icon",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp)
            .clip(CircleShape)
            .border(4.dp, Black, CircleShape)
            .align(Alignment.CenterHorizontally)
        )

        Text(
            text = title,
            fontSize = 16.sp,
            color = Blue40,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )

    }

}