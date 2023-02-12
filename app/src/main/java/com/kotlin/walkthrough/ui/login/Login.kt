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
import com.kotlin.walkthrough.Debug
import com.kotlin.walkthrough.R

@Composable
fun Login() {
    var accountInput: String by remember { mutableStateOf("") }
    var passwordInput: String by remember { mutableStateOf("") }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // header
        Text(
            text = stringResource(R.string.login_header),
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
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
                            painter = painterResource(R.drawable.ic_visibility_off),
                            contentDescription = "Show password"
                        )
                    else
                        Icon(
                            painter = painterResource(R.drawable.ic_visibility_on),
                            contentDescription = "Hide password"
                        )
                }
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Debug(
            text = {
                Text(
                    text =
                        """
                            |account: ${accountInput.ifEmpty { "None" }}
                            |password: ${passwordInput.ifEmpty { "None" }}
                        """.trimMargin(),
                    lineHeight = 24.sp
                )
            }
        ) {
            Button(
                onClick = it,
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
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    Login()
}
