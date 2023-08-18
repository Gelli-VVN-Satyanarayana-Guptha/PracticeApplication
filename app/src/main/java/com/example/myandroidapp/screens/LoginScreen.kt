package com.example.myandroidapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myandroidapp.R
import com.example.myandroidapp.navigation.Pages
import com.example.myandroidapp.ui.theme.MyAndroidAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myTextField(field: String, icon: Unit) : String? {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.padding(bottom = 20.dp),
        value = text ?: "",
        onValueChange = { text = it },
        label = { Text(field) },
        trailingIcon = {icon}
    )
    return text
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier
                .height(400.dp)
                .padding(top = 100.dp, bottom = 40.dp),
            painter = painterResource(id = R.drawable.android_img),
            contentDescription = null
        )

        Text(
            text = "Sign In",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 20.dp),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            trailingIcon = {
                Icon(Icons.Filled.Email,contentDescription = null)
            }
        )

        var isVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 40.dp),
            value = password,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Password") },
            trailingIcon = {
                var icon = if(isVisible){
                    Icons.Filled.Visibility
                }else{
                    Icons.Filled.VisibilityOff
                }

                IconButton(
                    onClick = {
                        isVisible = !isVisible
                    }
                ) {
                    Icon(icon, contentDescription = null)
                }
            }
        )

        Button(
            modifier = Modifier
                .width(130.dp),
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(10),
        ) {
            Text(
                text = "Login"
            )
        }

        Row {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable{
                        navController.navigate(Pages.SignUp.route)
                    }
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MyAndroidAppTheme  {
        val navController = rememberNavController()
        Login(navController)
    }
}
