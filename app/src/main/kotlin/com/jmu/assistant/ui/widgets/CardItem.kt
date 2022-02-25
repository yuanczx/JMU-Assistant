package com.jmu.assistant.ui.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier
            .height(150.dp)
            .padding(10.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp,
            pressedElevation = 5.dp
        ),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()

        }
    }
}

@Composable
fun ImageCardItem(
    @StringRes label: Int,
    @DrawableRes drawableRes: Int,
    onClick: () -> Unit
) {
    CardItem(onClick = onClick) {
        Image(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = drawableRes),
            contentDescription = "Transcript",
        )
        Text(
            text = stringResource(id = label),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

}



@Composable
fun GradeItem(
    title: String,
    subTitle: String,
    gpa: String,
    info: String,
) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .padding(6.dp),
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f))
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row {
                Text(
                    text = subTitle,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "/$gpa",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Text(text = "学分：$info", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}