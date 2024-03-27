package com.example.travelerapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginScreen(
    navController: NavController
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.clickable {
                navController.navigate(route = Screen.Home.route){
                    popUpTo(Screen.Home.route){
                        inclusive = true
                    }
                }
            },
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Login to your account")

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = "", onValueChange = {}, label = {
                Text(text = "Email address")
            })

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(value = "", onValueChange = {}, label = {
                Text(text = "Password")
            })

            Spacer(modifier = Modifier.height(10.dp))

            Button.CustomButton (
                text = "Login",
                onClick = {
                    navController.navigate(route = Screen.Home.route){
                        popUpTo(Screen.Home.route){
                        inclusive = true
                    }
                }
                }
            )
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview(){
    LoginScreen(
        navController = rememberNavController()
    )
}