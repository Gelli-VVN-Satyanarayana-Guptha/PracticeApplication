package com.example.myandroidapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myandroidapp.navigation.Pages


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(context: Context, navController: NavController){
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 20.dp),
            value = username,
            singleLine = true,
            onValueChange = { username = it },
            label = { Text("Username") },
            trailingIcon = {
                Icon(Icons.Filled.Person,contentDescription = null)
            }
        )

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 20.dp),
            value = email,
            singleLine = true,
            onValueChange = { email = it },
            label = { Text("Email") },
            trailingIcon = {
                Icon(Icons.Filled.Email,contentDescription = null)
            }
        )

        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 20.dp),
            value = password,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Password") },
            trailingIcon = {
                var icon = if(passwordVisible){
                    Icons.Filled.Visibility
                }else{
                    Icons.Filled.VisibilityOff
                }

                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(icon, contentDescription = null)
                }
            }
        )

        Column{
            var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

            OutlinedTextField(
                value = confirmPassword,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                onValueChange = { confirmPassword = it },
                singleLine = true,
                label = { Text("Confirm Password") },
                trailingIcon = {
                    var icon = if(confirmPasswordVisible){
                        Icons.Filled.Visibility
                    }else{
                        Icons.Filled.VisibilityOff
                    }

                    IconButton(
                        onClick = {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }
                    ) {
                        Icon(icon, contentDescription = null)
                    }
                }
            )

            if(password != confirmPassword){
                Text(
                    text = "Passwords doesn't match!",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Button(
            modifier = Modifier
                .width(130.dp),
            onClick = {

            },
            shape = RoundedCornerShape(10),
        ) {
            Text(
                text = "Sign Up"
            )
        }

        Row {
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Sign In",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable{
                       navController.navigate(Pages.Login.route)
                    }
            )
        }

    }
}

/*
@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    MyAndroidAppTheme {
        val navController = rememberNavController()
        SignUp(navController)
    }
}
*/