package com.example.drCompose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drCompose.ui.ui.theme.Green20
import com.example.drCompose.ui.ui.theme.White
import com.example.drCompose.utils.Constants


@Composable
fun IqButton(
    text: String,
    modifier: Modifier = Modifier,
    isEnable: Boolean? = true,
    align: TextAlign? = TextAlign.Center,
    onClick: () -> Unit
) {

    ElevatedButton(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .height(Constants.BUTTON_HEIGHT.dp),

        onClick = onClick,
        colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = White),
        enabled = isEnable ?: true,
        border = BorderStroke(Constants.VIEWS_STROKE.dp, Color.Black),
        shape = RoundedCornerShape(corner = CornerSize(Constants.VIEWS_RADIUS.dp)),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = align,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .fillMaxWidth()

        )
    }

}


@Composable
fun IqFillButton(text: String, modifier: Modifier, isEnable: Boolean? = true, onClick: () -> Unit) {

    ElevatedButton(
        modifier = Modifier
            .padding(top = 16.dp)
            .then(modifier)
            .fillMaxWidth()
            .height(Constants.BUTTON_HEIGHT.dp),

        onClick = onClick,
        enabled = isEnable ?: true,
        colors = ButtonDefaults.buttonColors(contentColor = White, containerColor = Green20),
        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
    }

}
