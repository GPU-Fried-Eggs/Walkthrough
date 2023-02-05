package com.kotlin.walkthrough.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.kotlin.walkthrough.R

@Composable
fun Login() {
    var accountInput: String by remember { mutableStateOf("") }
    var passwordInput: String by remember { mutableStateOf("") }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    var showDebug: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // header
        Text(
            text = stringResource(R.string.login_header),
            fontSize = 32.sp,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        // user account input field
        OutlinedTextField(
            value = accountInput,
            onValueChange = { accountInput = it },
            label = { Text(stringResource(R.string.login_account)) },
            placeholder = { Text(stringResource(R.string.login_account_placeholder)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Person Icon"
                )
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        // password input field
        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text(stringResource(R.string.login_password)) },
            placeholder = { Text(stringResource(R.string.login_password_placeholder)) },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Lock Icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    if (passwordVisibility)
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_visibility_off),
                            contentDescription = "Show password"
                        )
                    else
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_visibility),
                            contentDescription = "Hide password"
                        )
                }
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Button(
            onClick = { showDebug = true },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.login_submit),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
        if (showDebug) {
            AlertDialog(
                onDismissRequest = { showDebug = false },
                title = null,
                text = {
                    Text(
                        """
                        |account: ${accountInput.ifEmpty { "None" }}
                        |password: ${passwordInput.ifEmpty { "None" }}
                        """.trimMargin()
                    )
                },
                buttons = {
                    TextButton(onClick = { showDebug = false }) {
                        Text("Done")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Login")
@Composable
fun PreviewLogin() = Login()

