package com.jmu.assistant.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jmu.assistant.R

@Composable
fun AlertDialog(
    modifier: Modifier=Modifier,
    title: String,
    text: String?=null,
    dismissText: String = stringResource(R.string.dismiss),
    confirmText: String = stringResource(R.string.confirm),
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    content: @Composable ()->Unit={}
) {
    AlertDialog(modifier = modifier,onDismissRequest = {
        onDismiss()
    }, confirmButton = {
        Button(onClick = { onConfirm() }) {
            Text(text = confirmText)
        }
    }, dismissButton = {
        Button( onClick = {
            onDismiss()
        }) {
            Text(text = dismissText)
        }
    }, title = {
        Text(text = title) }, text = {
        content()
        text?.let {  Text(text = it)}
        })
}


@Composable
fun ProgressDialog(text: String? = null) {
    Dialog(onDismissRequest = {}) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(45.dp))
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.4f)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            text?.let { Text(text = it, modifier = Modifier.padding(10.dp), fontSize = 18.sp) }
        }
    }
}

