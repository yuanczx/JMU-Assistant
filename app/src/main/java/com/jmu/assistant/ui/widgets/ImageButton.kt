package com.jmu.assistant.ui.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmu.assistant.entity.ContentNav

@Composable
fun RowScope.CardItem(
    @StringRes label: Int,
    @DrawableRes drawableRes: Int,
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier
            .weight(0.5f)
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
}

@Composable
fun ImageButtonList(itemList: List<ContentNav>, onClick: (ContentNav) -> Unit) {
    val temp = itemList.size % 2
    val rows = if (temp == 0) itemList.size / 2 else (itemList.size / 2) + 1

    repeat(rows) { row ->
        Row {
            if (row == rows - 1&&temp!=0)
                CardItem(
                    label = itemList[row].stringId,
                    drawableRes = itemList[row].drawableId!!,
                    onClick = { onClick(itemList[row]) })
            else repeat(2) { col ->
                CardItem(
                    label = itemList[row * 2 + col].stringId,
                    drawableRes = itemList[row * 2 + col].drawableId!!,
                    onClick = { onClick(itemList[row * 2 + col]) })
            }
        }
    }
}