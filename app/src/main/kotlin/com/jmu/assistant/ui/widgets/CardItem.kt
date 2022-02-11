package com.jmu.assistant.ui.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.models.CourseInfo

@Composable
fun RowScope.CardItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
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
            content()

        }
    }
}

@Composable
fun RowScope.ImageCardItem(
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
fun ImageCardList(itemList: List<ContentNav>, onClick: (ContentNav) -> Unit) {
    val temp = itemList.size % 2
    val rows = if (temp == 0) itemList.size / 2 else (itemList.size / 2) + 1

    repeat(rows) { row ->
        Row {
            if (row == rows - 1 && temp != 0)
                ImageCardItem(
                    label = itemList[row * 2].stringId,
                    drawableRes = itemList[row * 2].drawableId!!,
                    onClick = { onClick(itemList[row * 2]) })
            else repeat(2) { col ->
                ImageCardItem(
                    label = itemList[row * 2 + col].stringId,
                    drawableRes = itemList[row * 2 + col].drawableId!!,
                    onClick = { onClick(itemList[row * 2 + col]) })
            }
        }
    }
}

@Composable
fun RowScope.GradeItem(
    title: String,
    subTitle: String,
    gpa: String,
    info: String,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .weight(0.5f)
            .padding(6.dp),
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row {
                Text(
                    text = subTitle,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "/$gpa",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Text(text = "学分：$info")
        }
    }
}

@Composable
fun GradeList(
    courseInfos: ArrayList<CourseInfo>,
) {
    val temp = courseInfos.size % 2
    val rows = if (temp == 0) courseInfos.size / 2 else (courseInfos.size / 2) + 1

    repeat(rows) { row ->
        Row {
            if (row == rows - 1 && temp != 0)
                GradeItem(
                    title = courseInfos[row * 2].course.nameZh,
                    subTitle = courseInfos[row * 2].gaGrade,
                    gpa = courseInfos[row * 2].gp.toString(),
                    info = courseInfos[row * 2].course.credits.toString()
                )
            else repeat(2) { col ->
                GradeItem(
                    title = courseInfos[row * 2 + col].course.nameZh,
                    subTitle = courseInfos[row * 2 + col].gaGrade,
                    gpa = courseInfos[row * 2 + col].gp.toString(),
                    info = courseInfos[row * 2 + col].course.credits.toString()
                )
            }
        }
    }
}