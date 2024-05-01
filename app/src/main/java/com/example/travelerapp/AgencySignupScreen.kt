package com.example.travelerapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AgencySignUpScreen(
    navController: NavController,
    context: Context
) {
    val db = Firebase.firestore

    val agencyUsername = remember {
        mutableStateOf(TextFieldValue())
    }
    val agencyEmail = remember {
        mutableStateOf(TextFieldValue())
    }
    val agencyPassword = remember {
        mutableStateOf(TextFieldValue())
    }
    val agreedToTerms = remember {
        mutableStateOf(false)
    }
    val showToast = remember {
        mutableStateOf(false)
    }
    val checked = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(350.dp)
                .height(600.dp)
                .background(Color.LightGray, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(route = Screen.Home.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    text = "Agency Sign Up",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Username",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = agencyUsername.value,
                    onValueChange = { agencyUsername.value = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Email",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = agencyEmail.value,
                    onValueChange = { agencyEmail.value = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Password",
                    modifier = Modifier.align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = agencyPassword.value,
                    onValueChange = { agencyPassword.value = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = agreedToTerms.value,
                            onCheckedChange = { isChecked -> agreedToTerms.value = isChecked },
                            colors = CheckboxDefaults.colors(checkedColor = if (agreedToTerms.value) Color.Green else Color.Red) // Change Checkbox color based on agreement
                        )
                        Text("I had read and agreed the Term and Privacy")
                    }
                }

                ReuseComponents.CustomButton(
                    text = "Sign Up",
                    onClick = {
                        if (agreedToTerms.value) {
                            addDataToFirestore(
                                context = context,
                                db = db,
                                agencyUsername = agencyUsername.value.text,
                                agencyEmail = agencyEmail.value.text,
                                agencyPassword = agencyPassword.value.text
                            )
                            showToast.value = true // Set the state to show toast after successful signup
                            agreedToTerms.value = false // Reset the checkbox state
                        } else {
                            Toast.makeText(context, "Please agree to the Terms and Privacy", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                // Show toast message after successful signup
                if (showToast.value) {
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }

                Text(
                    text = "Already have an account? Login now",
                    color = Color.Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            // Navigate to the signup screen
                            navController.navigate(Screen.AgencyLogin.route) {
                                popUpTo(Screen.AgencyLogin.route) {
                                    inclusive = true
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
@Preview
fun AgencySignUpScreenPreview(){
    AgencySignUpScreen(
        navController = rememberNavController(),
        context = LocalContext.current
    )
}
