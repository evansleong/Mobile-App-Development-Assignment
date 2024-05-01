package com.example.travelerapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.travelerapp.Screen
import com.example.travelerapp.data.AgencyUser
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AgencyLoginScreen(
    navController: NavController,
    context: Context
) {
    val db = Firebase.firestore

    val agencyUsername = remember {
        mutableStateOf(TextFieldValue())
    }
    val agencyLoginEmail = remember {
        mutableStateOf(TextFieldValue())
    }
    val agencyLoginPassword = remember {
        mutableStateOf(TextFieldValue())
    }
    val checked = remember {
        mutableStateOf(false)
    }

    val agencyUsers = remember { mutableStateOf(emptyList<AgencyUser>()) }

    readAgencyDataFromFirestore(db) { agencyUserList ->
        agencyUsers.value = agencyUserList
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
                    text = "Agency Log In",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Email",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = agencyLoginEmail.value.text,
                    onValueChange = { agencyLoginEmail.value = agencyLoginEmail.value.copy(text = it) },
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
                    value = agencyLoginPassword.value.text,
                    onValueChange = { agencyLoginPassword.value = agencyLoginPassword.value.copy(text = it) },
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
                            checked = checked.value,
                            onCheckedChange = { isChecked -> checked.value = isChecked },
                            colors = CheckboxDefaults.colors(checkedColor = Color.Green)
                        )
                        Text("I would like to receive your newsletter and other promotional information")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = checked.value,
                            onCheckedChange = { isChecked -> checked.value = isChecked },
                            colors = CheckboxDefaults.colors(checkedColor = Color.Green)
                        )
                        Text("Remember me")
                    }
                }

                ReuseComponents.CustomButton(
                    text = "Login",
                    onClick = {
                        val email = agencyLoginEmail.value.text
                        val password = agencyLoginPassword.value.text
                        val loginSuccessful = checkLoginCredentials(email, password, agencyUsers.value)

                        if (loginSuccessful) {
                            Log.d("Login", "Login successful")
                            Toast.makeText(context, "Login Up Successful", Toast.LENGTH_SHORT).show()
                            navController.navigate(route = Screen.AgencyHome.route)
                        } else {
                            // Login failed
                            Log.d("Login", "Login failed. Please check your credentials.")
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                Text(
                    text = "Don't have an account yet? Sign up now",
                    color = Color.Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            // Navigate to the signup screen
                            navController.navigate(Screen.AgencySignup.route) {
                                popUpTo(Screen.Signup.route) {
                                    inclusive = true
                                }
                            }
                        }
                )
            }
        }
    }
}

fun checkLoginCredentials(email: String, password: String, agencyUsers: List<AgencyUser>): Boolean {

    // Print each email and password from the agencyUsers list
    agencyUsers.forEachIndexed { index, user ->
        println("User $index - Email: ${user.agencyEmail}, Password: ${user.agencyPassword}")
        println("User $index - Email: ${email}, Password: ${password}")
    }

    // Check if there is any user with the provided email and password
    return agencyUsers.any { it.agencyEmail == email && it.agencyPassword == password }
}

@Preview
@Composable
fun AgencyLoginScreenPreview() {
    AgencyLoginScreen(navController = rememberNavController(), context = LocalContext.current)
}
