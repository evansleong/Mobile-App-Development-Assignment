package com.example.travelerapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.travelerapp.data.AgencyUser
import com.example.travelerapp.viewModel.AgencyViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun AgencyForgotPwScreen(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel,
) {
    val db = Firebase.firestore

    val email = remember{ mutableStateOf(TextFieldValue()) }
    val securityQuestionAnswer = remember { mutableStateOf(TextFieldValue()) }

    val agencyUsers = remember { mutableStateOf(emptyList<AgencyUser>()) }

    // Fetch agency users from Firestore
    LaunchedEffect(Unit) {
        viewModel.readAgencyData(db) { agencyUserList ->
            agencyUsers.value = agencyUserList
        }
    }

    var agencyUser by remember { mutableStateOf<AgencyUser?>(null) }

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
                .height(400.dp)
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
                    text = "Forgot Password",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Email",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = email.value.text,
                    onValueChange = {
                        email.value = email.value.copy(text = it) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Answer to Security Question",
                    modifier = Modifier.align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = securityQuestionAnswer.value.text,
                    onValueChange = {
                        securityQuestionAnswer.value = securityQuestionAnswer.value.copy(text = it)
                                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                ReuseComponents.CustomButton(
                    text = "Verify",
                    onClick = {
                        val sqEmail = email.value.text
                        val sqQuestion = securityQuestionAnswer.value.text
                        val verifySuccessful =
                            viewModel.checkSecureCredentials(sqEmail, sqQuestion, agencyUsers.value)
                        if (verifySuccessful != null) {
                            viewModel.loggedInAgency = verifySuccessful
                            Toast.makeText(context, "verified Successful", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(Screen.AgencyHome.route) {
                                popUpTo(Screen.AgencyLogin.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            Toast.makeText(context, "Verify failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AgencyForgotPwPreview() {
    AgencyForgotPwScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        viewModel = AgencyViewModel()
    )
}
