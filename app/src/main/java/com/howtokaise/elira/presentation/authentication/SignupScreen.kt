package com.howtokaise.elira.presentation.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.howtokaise.elira.AppUtil
import com.howtokaise.elira.R
import com.howtokaise.elira.presentation.viewmodel.AuthViewmodel

@Composable
fun SignupScreen(modifier: Modifier = Modifier, authViewmodel: AuthViewmodel = viewModel()) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello there!",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 30.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Create an account",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.dummy_avatar),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text("Email address")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text("Full name")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                authViewmodel.signup(email,name,password){success, errorMessage->
                    if (success){

                    }else{
                        AppUtil.showToast(context,errorMessage?: "Something went wrong")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Signup",fontSize = 20.sp)
        }
    }
}