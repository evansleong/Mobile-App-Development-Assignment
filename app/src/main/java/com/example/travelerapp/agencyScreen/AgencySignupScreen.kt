package com.example.travelerapp.agencyScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.example.travelerapp.R
import com.example.travelerapp.data.AgencyUser
import com.example.travelerapp.screen.Screen
import com.example.travelerapp.viewModel.AgencyViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.UUID

@Composable
fun AgencySignUpScreen(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel
) {
    val db = Firebase.firestore

    val agencyId = UUID.randomUUID().toString().substring(0, 4)

    val showSecurityQuestionDialog = remember { mutableStateOf(false) }

    val agencyUsername = remember {
        mutableStateOf(TextFieldValue())
    }
    val agencyEmail = remember {
        mutableStateOf(TextFieldValue())
    }
    val agencyPassword = remember {
        mutableStateOf(TextFieldValue())
    }
    val agencyConfirmPassword = remember {
        mutableStateOf(TextFieldValue())
    }
    var securityQuestion = remember { mutableStateOf(TextFieldValue()) }

    val agreedToTerms = remember {
        mutableStateOf(false)
    }
    val showToast = remember {
        mutableStateOf(false)
    }
    val checked = remember {
        mutableStateOf(false)
    }

    val isEmailFormatCorrect = remember { mutableStateOf(false) }
    val isPasswordFormatCorrect = remember { mutableStateOf(false) }
    val isPasswordMatched = remember { mutableStateOf(false) }

    // Call validation functions from ViewModel
    val isValidEmail = viewModel.isValidEmail(agencyEmail.value.text)
    val isValidPassword = viewModel.isValidPassword(agencyPassword.value.text)
    val isValidConfirmPassword = viewModel.isConfirmPasswordMatch(agencyPassword.value.text, agencyConfirmPassword.value.text)

    isEmailFormatCorrect.value = isValidEmail
    isPasswordFormatCorrect.value = isValidPassword
    isPasswordMatched.value = isValidConfirmPassword

    val emailGuideMessage = if (isEmailFormatCorrect.value) "Email format is correct" else "Invalid email format"
    val passwordGuideMessage = if (isPasswordFormatCorrect.value) "Password format is correct" else "Password must contain 8 characters with \n at least 1 uppercase, lowercase & numeric character"
    val confirmPasswordGuideMessage = if (isPasswordMatched.value) "Password matches" else "Password is not matches"


    val emailRegexPattern = viewModel.getEmailRegexPattern()
    val passwordRegexPattern = viewModel.getPasswordRegexPattern()

    val agencyUsers = remember { mutableStateOf(emptyList<AgencyUser>()) }

    viewModel.readAgencyData(db) { agencyUserList ->
        agencyUsers.value = agencyUserList
    }

    val isSignUpEnabled = isValidEmail && isValidPassword && agreedToTerms.value

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
                .height(750.dp)
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
                    modifier = Modifier,
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
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(20.dp),
                    isError = !isValidEmail
                )
                // Display guide messages next to email and password fields
                Text(
                    text = emailGuideMessage,
                    color = if (isEmailFormatCorrect.value) Color.Blue else Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Password",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
                TextField(
                    value = agencyPassword.value,
                    onValueChange = { agencyPassword.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,),
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
                    isError = !isValidPassword
                )
                Text(
                    text = passwordGuideMessage,
                    color = if (isPasswordFormatCorrect.value) Color.Blue else Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Confirm Password",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = agencyConfirmPassword.value,
                    onValueChange = { agencyConfirmPassword.value = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible)
                            R.drawable.visibility
                        else R.drawable.visibility_off

                        // Please provide localized description for accessibility services
                        val description = if (confirmPasswordVisible) "Hide password" else "Show password"
                        IconButton (onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = ImageVector.vectorResource(id = image), description)
                        }
                    },
                    isError = !isValidConfirmPassword
                )

                Text(
                    text = confirmPasswordGuideMessage,
                    color = if (isPasswordMatched.value) Color.Blue else Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
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
                            colors = CheckboxDefaults.colors(checkedColor = if (agreedToTerms.value) Color(0xFF5DB075) else Color.Red) // Change Checkbox color based on agreement
                        )
                        Text("I had read and agreed the Term and Privacy")
                    }
                }


                ReuseComponents.CustomButton(
                    text = "Sign Up",
                    onClick = {
                        if (viewModel.areFieldsNotEmpty(
                                agencyUsername.value.text,
                                agencyEmail.value.text,
                                agencyPassword.value.text,
                                agencyConfirmPassword.value.text
                            ) && isSignUpEnabled
                        ) {
                            if (viewModel.isUsernameAvailable(agencyUsername.value.text, agencyUsers.value) &&
                                viewModel.isEmailAvailable(agencyEmail.value.text, agencyUsers.value) &&
                                viewModel.isConfirmPasswordMatch(agencyPassword.value.text, agencyConfirmPassword.value.text)
                            ) {
//                                viewModel.addAgency(
//                                    context = context,
//                                    db = db,
//                                    agencyId = agencyId,
//                                    agencyUsername = agencyUsername.value.text,
//                                    agencyEmail = agencyEmail.value.text,
//                                    agencyPassword = agencyPassword.value.text,
//                                    agencyPicture = null
//                                )
                                showSecurityQuestionDialog.value = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Username or Email already exists, please choose another",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill all fields correctly and agree to the Terms and Privacy",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )

                if (showSecurityQuestionDialog.value) {
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("Security Question : What was your favourite food as child ?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (securityQuestion.value.text.isNotBlank()) {
                                        viewModel.addAgency(
                                            context = context,
                                            db = db,
                                            agencyId = agencyId,
                                            agencyUsername = agencyUsername.value.text,
                                            agencyEmail = agencyEmail.value.text,
                                            agencyPassword = agencyPassword.value.text,
                                            agencyPicture = null,
                                            agencySecureQst = securityQuestion.value.text
                                        )
                                        showToast.value = true
                                        agreedToTerms.value = false
                                        navController.navigate(Screen.AgencyLogin.route) {
                                            popUpTo(Screen.AgencyLogin.route) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Security question cannot be empty",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) {
                                Text("Confirm")
                            }
                        },
                        text = {
                            TextField(
                                value = securityQuestion.value,
                                onValueChange = { securityQuestion.value = it },
                                label = { Text("Enter your security question") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }

                if (showToast.value) {
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    showSecurityQuestionDialog.value = true
                }

                val annotatedString = buildAnnotatedString {
                    append("Already have an account? ")
                    pushStringAnnotation(tag = "LOGIN", annotation = "Login now")
                    withStyle(style = SpanStyle(color = Color.Blue, fontSize = 14.sp, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)) {
                        append("Login now")
                    }
                    pop()
                }

                Text(
                    text = annotatedString,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            annotatedString.getStringAnnotations(tag = "LOGIN", start = 0, end = annotatedString.length).firstOrNull()?.let { _ ->
                                navController.navigate(Screen.AgencyLogin.route) {
                                    popUpTo(Screen.AgencyLogin.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun SecurityQuestionDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSecurityQuestionEntered: (String) -> Unit,
    navController: NavController,
) {
    if (showDialog) {
        var securityQuestion by remember { mutableStateOf(TextFieldValue()) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Set Security Question") },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss()
                        onSecurityQuestionEntered(securityQuestion.text)
                        navController.navigate(Screen.AgencyLogin.route) {
                            popUpTo(Screen.AgencyLogin.route) {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            },
            text = {
                TextField(
                    value = securityQuestion,
                    onValueChange = { securityQuestion = it },
                    label = { Text("Enter your security question") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

@Composable
@Preview
fun AgencySignUpScreenPreview(){
    AgencySignUpScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        viewModel = AgencyViewModel()
    )
}

