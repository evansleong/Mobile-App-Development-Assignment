package com.example.travelerapp

import ReuseComponents
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.viewModel.AgencyViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun AgencyChangePwScreen(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel,
) {
    val db = Firebase.firestore

    val user = viewModel.loggedInAgency
    val password = remember{ mutableStateOf(TextFieldValue()) }
    val confPassword = remember { mutableStateOf(TextFieldValue()) }

    val isPasswordFormatCorrect = remember { mutableStateOf(false) }
    val isPasswordMatched = remember { mutableStateOf(false) }

    // Call validation functions from ViewModel
    val isValidPassword = viewModel.isValidPassword(password.value.text)
    val isValidConfirmPassword = viewModel.isConfirmPasswordMatch(password.value.text, confPassword.value.text)

    isPasswordFormatCorrect.value = isValidPassword
    isPasswordMatched.value = isValidConfirmPassword

    val passwordGuideMessage = if (isPasswordFormatCorrect.value) "Password format is correct" else "Password must contain 8 characters with \n at least 1 uppercase, lowercase & numeric character"
    val confirmPasswordGuideMessage = if (isPasswordMatched.value) "Password matches" else "Password is not matches"

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
                .height(500.dp)
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
                    text = "Change Password",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Password",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                    },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Insert New Password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidPassword
                )

                Text(
                    text = passwordGuideMessage,
                    color = if (isPasswordFormatCorrect.value) Color.Blue else Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Confirm Password",
                    modifier = Modifier.align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = confPassword.value,
                    onValueChange = {
                        confPassword.value = it
                    },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Insert Again New Password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidConfirmPassword
                )

                Text(
                    text = confirmPasswordGuideMessage,
                    color = if (isPasswordMatched.value) Color.Blue else Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                ReuseComponents.CustomButton(
                    text = "Change Password",
                    onClick = {
                        if (viewModel.isConfirmPasswordMatch(password.value.text, confPassword.value.text)) {
                            viewModel.updateAgencyData(context, db, user?.agencyId.toString(), user?.agencyUsername.toString(), password.value.text)
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Password is not matches", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AgencyChangePwPreview() {
    AgencyChangePwScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        viewModel = AgencyViewModel()
    )
}
