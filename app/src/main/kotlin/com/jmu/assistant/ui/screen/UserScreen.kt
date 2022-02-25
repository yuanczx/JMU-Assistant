package com.jmu.assistant.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun MainActivity.UserScreen() {
    var maxLine by remember { mutableStateOf(5) }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(7.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 15.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Image(
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp),
//                    .padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
                painter = rememberImagePainter(request = mainViewModel.getImageRequest()),
                contentDescription = ""
            )
            Text(
                text = mainViewModel.rightData,
                modifier = Modifier.padding(start = 8.dp, top = 5.dp),
                fontWeight = FontWeight.Light,
                lineHeight = 28.sp
            )
        }
        if (mainViewModel.showInfo)
            Column(modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .clickable {
                    maxLine = if (maxLine == 5) 50 else 5
                }
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = stringResource(id = R.string.basic_info),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 5.dp)
                )
                Text(
                    text = mainViewModel.basicInfo,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 15.dp),
                    maxLines = maxLine
                )
            }
    }
    LaunchedEffect(null) {
        if (mainViewModel.basicInfo.isBlank())
            mainViewModel.getStudentInfo()
        else
            mainViewModel.showInfo = true
    }
}