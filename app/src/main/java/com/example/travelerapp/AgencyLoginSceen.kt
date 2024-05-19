package com.example.travelerapp

import ReuseComponents
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.data.AgencyUser
import com.example.travelerapp.viewModel.AgencyViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AgencyLoginScreen(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel,
) {
    val db = Firebase.firestore

    val agencyLoginEmail = remember {
        mutableStateOf(TextFieldValue())
    }
    val agencyLoginPassword = remember {
        mutableStateOf(TextFieldValue())
    }
    var rememberMeChecked by rememberSaveable { mutableStateOf(viewModel.getLoginDetails(context) != null) }

    fun clearSavedLoginDetails() {
        viewModel.clearSavedLoginDetails(context)
    }

    val agencyUsers = remember { mutableStateOf(emptyList<AgencyUser>()) }

    // Check if the user is already logged in
    LaunchedEffect(Unit) {
        val loginDetails = viewModel.getLoginDetails(context)
        if (loginDetails != null) {
            val (email, password) = loginDetails
            agencyLoginEmail.value = TextFieldValue(email)
            agencyLoginPassword.value = TextFieldValue(password)
            val loginSuccessful =
                viewModel.checkLoginCredentials(email, password, agencyUsers.value)
            if (loginSuccessful != null) {
                viewModel.loggedInAgency = loginSuccessful
                navController.navigate(Screen.AgencyHome.route) {
                    popUpTo(Screen.AgencyLogin.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    viewModel.readAgencyData(db) { agencyUserList ->
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
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
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
                    onValueChange = {
                        agencyLoginEmail.value = agencyLoginEmail.value.copy(text = it)
                    },
                    singleLine = true,
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

                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                TextField(
                    value = agencyLoginPassword.value.text,
                    onValueChange = {
                        agencyLoginPassword.value = agencyLoginPassword.value.copy(text = it)
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            R.drawable.visibility
                        else R.drawable.visibility_off

                        // Please provide localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = ImageVector.vectorResource(id = image), description)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = rememberMeChecked,
                            onCheckedChange = { isChecked ->
                                rememberMeChecked = isChecked
                                if (!isChecked) {
                                    clearSavedLoginDetails()
                                }
                            },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF5DB075))
                        )
                        Text("Remember me")
                    }
                }

                ReuseComponents.CustomButton(
                    text = "Login",
                    onClick = {
                        val email = agencyLoginEmail.value.text
                        val password = agencyLoginPassword.value.text
                        val loginSuccessful =
                            viewModel.checkLoginCredentials(email, password, agencyUsers.value)

                        if (loginSuccessful != null) {
                            viewModel.loggedInAgency = loginSuccessful
                            if (rememberMeChecked) {
                                viewModel.saveLoginDetails(context, email, password)
                            }
                            Toast.makeText(context, "Login Up Successful", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(Screen.AgencyHome.route) {
                                popUpTo(Screen.AgencyLogin.route) {
                                    inclusive = true
                                }
                                popUpTo(Screen.UserOrAdmin.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                val annotatedString = buildAnnotatedString {
                    append("Don't have an account yet? ")
                    pushStringAnnotation(tag = "SIGNUP", annotation = "Sign up now")
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Sign up now")
                    }
                    pop()
                }

                Text(
                    text = annotatedString,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            annotatedString
                                .getStringAnnotations(
                                    tag = "SIGNUP",
                                    start = 0,
                                    end = annotatedString.length
                                )
                                .firstOrNull()
                                ?.let { _ ->
                                    navController.navigate(Screen.AgencySignup.route) {
                                        popUpTo(Screen.AgencySignup.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                        }
                )
                Text(
                    text = "Forgot Password?",
                    color = Color.Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            navController.navigate(Screen.AgencyForgotPw.route) {
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

@Preview
@Composable
fun AgencyLoginScreenPreview() {
    AgencyLoginScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        viewModel = AgencyViewModel()
    )
}
