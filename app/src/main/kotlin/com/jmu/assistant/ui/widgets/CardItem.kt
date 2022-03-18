@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.jmu.assistant.ui.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(
    onClick: () -> Unit,
    corner: Dp = 20.dp,
    padding:Dp = 7.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    /**
     * @Author yuanczx
     * @Description 卡片Item 显示Menu界面
     * @Date 2022/3/10 19:28
     * @Params [onClick, content]
     * @Return
     **/
    Card(
//        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outline),
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(corner),
        modifier = Modifier
            .height(150.dp)
            .padding(padding)
            .clip(RoundedCornerShape(corner))
            .clickable(onClick = onClick),
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}

@Composable
fun ImageCardItem(
    @StringRes label: Int,
    @DrawableRes drawableRes: Int,
    onClick: () -> Unit
) {
    /**
     * @Author yuanczx
     * @Description 卡片样式图片Item
     * @Date 2022/3/10 19:29
     * @Params [label, drawableRes, onClick]
     * @Return
     **/
    CardItem(onClick = onClick) {
        Icon(
            modifier = Modifier.size(45.dp),
            painter = painterResource(id = drawableRes),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            contentDescription = stringResource(id = label),
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = stringResource(id = label),
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            color = MaterialTheme.colorScheme.onPrimaryContainer
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
    /**
     * @Author yuanczx
     * @Description 成绩单显示Item
     * @Date 2022/3/10 19:29
     * @Params [title, subTitle, gpa, info]
     * @Return
     **/
    CardItem(onClick = { /*TODO*/ }, corner = 10.dp, padding = 5.dp) {
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