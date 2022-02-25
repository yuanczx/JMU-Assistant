package com.jmu.assistant.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.MainActivity.Companion.COOKIE_KEY
import com.jmu.assistant.MainActivity.Companion.cookie
import com.jmu.assistant.R
import com.jmu.assistant.dataStore
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.widgets.TopBar
import com.jmu.assistant.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable

fun MainActivity.LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = null, block = {
        viewModel.userName = dataStore.data.first()[viewModel.userNameKey] ?: ""
        viewModel.passWord = dataStore.data.first()[viewModel.passWordKey] ?: ""
        if (viewModel.userName.isNotBlank()&& viewModel.passWord.isNotBlank()){
            viewModel.rememberPwd = true
        }
    })


    val textFieldColor = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar()
        Spacer(modifier = Modifier.height(80.dp))
        OutlinedTextField(value = viewModel.userName,
            onValueChange = { viewModel.userName = it },
            modifier = Modifier.fillMaxWidth(0.8f),
            label = { Text(stringResource(id = R.string.user)) },
            placeholder = { Text(text = stringResource(R.string.user_place_holder)) },
            colors = textFieldColor,
            leadingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.secondary,
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = stringResource(id = R.string.user)
                )
            },
            trailingIcon = {
                IconButton(onClick = viewModel::clearUser) {
                    Icon(
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = Icons.Default.Clear,
                        contentDescription = "clear"
                    )
                }
            })

        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(value = viewModel.passWord,
            onValueChange = { viewModel.passWord = it },
            modifier = Modifier.fillMaxWidth(0.8f),
            label = { Text(text = stringResource(R.string.password)) },
            placeholder = { Text(text = stringResource(R.string.please_password)) },
            colors = textFieldColor,
            visualTransformation = if (viewModel.passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(
                    tint = MaterialTheme.colorScheme.secondary,
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = stringResource(R.string.user)
                )
            },
            trailingIcon = {
                IconButton(onClick = viewModel::changePasswordVisual) {
                    val visibilityIcon =
                        if (viewModel.passwordHidden) painterResource(id = R.drawable.ic_baseline_visibility_off_24)
                        else painterResource(id = R.drawable.ic_baseline_visibility_24)
                    val description =
                        if (viewModel.passwordHidden) stringResource(R.string.show_password) else
                            stringResource(R.string.hide_password)
                    Icon(
                        tint = MaterialTheme.colorScheme.secondary,
                        painter = visibilityIcon,
                        contentDescription = description
                    )
                }
            })

        Row(
            modifier = Modifier.fillMaxWidth(0.87f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = viewModel.rememberPwd,
                onCheckedChange = { viewModel.rememberPwd = it })
            Text(text = stringResource(R.string.remember_password))
        }
        Spacer(modifier = Modifier.height(10.dp))
        ElevatedButton(modifier = Modifier.fillMaxWidth(0.8f), onClick = {
            scope.launch {
                if (viewModel.login()) {
                    cookie = viewModel.mCookie
                    dataStore.edit {
                        it[COOKIE_KEY] = cookie
                        if (viewModel.rememberPwd) {
                            it[viewModel.userNameKey] = viewModel.userName
                            it[viewModel.passWordKey] = viewModel.passWord
                        }else{
                            it[viewModel.passWordKey] = ""
                            it[viewModel.userNameKey] = ""
                        }
                    }
                    mainViewModel.getStudentId()
                    navController.navigate(ContentNav.Menu.route) {
                        popUpTo(ContentNav.Login.route){
                            inclusive = true
                        }
                    }
                }
            }
        }) {
            Text(text = stringResource(id = R.string.Login))
        }
    }
}