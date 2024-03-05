package com.example.tateti_20.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tateti_20.R
import com.example.tateti_20.ui.core.ShimmerLogin
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Orange1
import com.example.tateti_20.ui.theme.Orange2

@Composable
fun Login(
    isLoading: Boolean,
    modifier: Modifier,
    onClickLogin: (String, String) -> Unit,
    onClickChangePassword: (String) -> Unit,
    onGoogleLoginSelected: () -> Unit,
    onClickSingUp: (String, String, String) -> Unit
) {
        var createUser by remember { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var verifyPassword by remember { mutableStateOf("") }

    ShimmerLogin(modifier = modifier, isLoading = isLoading) {
        Column(modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Email(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = email,
                onValueChange = { email = it }
            )
            Password(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = password,
                onValueChange = { password = it }
            )
            VerifyPassword(
                isVisible = createUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = verifyPassword,
                onValueChange = { verifyPassword = it }
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
            ) {
                CreateAcount(value = createUser, onValueChange = { createUser = it})
                Spacer(modifier = modifier.weight(1f))
                ForgotPassword{ onClickChangePassword(email) }
            }

            Spacer(modifier = Modifier.height(22.dp))

            ButtonEmailAccess(
                text = if (createUser) "Create Count" else "Login",
                enabled = (email.isNotEmpty() && password.isNotEmpty() && (verifyPassword.isNotEmpty() || !createUser))
            ) {
                if (createUser) onClickSingUp(email, password, verifyPassword)
                else onClickLogin(email, password)
            }
            ButtonGoogleAccess{ onGoogleLoginSelected() }
        }
    }
}


@Composable
fun Email(modifier: Modifier, value: String, onValueChange: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = "email", color = Orange2) },
        value = value,
        onValueChange = { onValueChange(it) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = Orange1,
            textColor = Accent,
            focusedBorderColor = Orange1,
            unfocusedBorderColor = Orange1
        ),
        maxLines = 1,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}
@Composable
fun Password(modifier: Modifier, value: String, onValueChange: (String) -> Unit) {
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = "password", color = Orange2) },
        value = value,
        onValueChange = { onValueChange(it) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = Orange1,
            textColor = Accent,
            focusedBorderColor = Orange1,
            unfocusedBorderColor = Orange1
        ),
        maxLines = 1,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon =
                if (isPasswordVisible) R.drawable.ic_hide_password else R.drawable.ic_show_password
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
//                            tint = Orange1
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}
@Composable
fun VerifyPassword(
    modifier: Modifier,
    isVisible: Boolean,
    value: String,
    onValueChange: (String) -> Unit
) {
    var verifyPassword by remember { mutableStateOf("") }

    AnimatedContent(targetState = isVisible, label = "") {
        if (it) {
            OutlinedTextField(
                modifier = modifier,
                label = { Text(text = "confirm password", color = Orange2) },
                value = value,
                onValueChange = { onValueChange(it) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Orange1,
                    textColor = Accent,
                    focusedBorderColor = Orange1,
                    unfocusedBorderColor = Orange1
                ),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
    }
}


@Composable
fun CreateAcount(value: Boolean, onValueChange: (Boolean) -> Unit) {
    var createUser by remember { mutableStateOf(false) }
    Row( verticalAlignment = Alignment.CenterVertically ) {
        Text(
            text = "Create Account",
            color = Accent,
            fontSize = 12.sp
        )
        Checkbox(checked = value, onCheckedChange = {
            onValueChange(!value)
        })
    }
}
@Composable
fun ForgotPassword(onClickChangePassword:() -> Unit) {
    Text(
        text = "Forgot Password?",
        color = Accent,
        fontSize = 12.sp,
        modifier = Modifier
            .padding(12.dp)
            .clickable { onClickChangePassword() }
    )
}


@Composable
fun ButtonEmailAccess(text: String, enabled: Boolean, onClickEmailAccess:()->Unit){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Orange1,
            contentColor = Accent
        ),
        onClick = { onClickEmailAccess() },
        enabled = enabled
    ) {
        Text(text = text)
    }
}
@Composable
fun ButtonGoogleAccess(onGoogleLoginSelected:() -> Unit){
    Card(
        Modifier
            .clickable { onGoogleLoginSelected() }
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp), elevation = 12.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(Orange1),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(26.dp)
                    .padding(end = 8.dp),
                painter = painterResource(id = R.drawable.ic_google),
                tint = Color.Black,
                contentDescription = "ic_google"
            )
            Text(
                text = "Login with Google",
                color = Accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}


