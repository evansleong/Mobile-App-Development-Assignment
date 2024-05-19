package com.example.travelerapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.DBHandler
import com.example.travelerapp.Screen
import com.example.travelerapp.data.User
import com.example.travelerapp.viewModel.UserViewModel
import com.example.travelerapp.viewModel.WalletViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SignUpScreen(
    navController: NavController,
    context: Context,
    dbHandler: DBHandler,
    viewModel: UserViewModel,
    walletViewModel: WalletViewModel
) {
    val db = Firebase.firestore
    val username = remember { mutableStateOf(TextFieldValue()) }
    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val confPassword = remember { mutableStateOf(TextFieldValue()) }
    val secureQst = remember { mutableStateOf("") }
    val checked = remember { mutableStateOf(false) }
    val showToast = remember { mutableStateOf(false) }

    val vEmail = viewModel.isUEmailV(email.value.text)
    val vPw = viewModel.isUPwV(password.value.text)

    val vSignUp = vEmail && vPw && checked.value


    val userSU = remember {
        mutableStateOf(emptyList<User>())
    }

    val pwTyped = remember {
        mutableStateOf(false)
    }


    viewModel.readUData(db){ userList -> userSU.value = userList }

    val isEmailFormatCorrect = remember { mutableStateOf(false) }
    val isPasswordFormatCorrect = remember { mutableStateOf(false) }
    val isPasswordMatched = remember { mutableStateOf(false) }

    // Call validation functions from ViewModel
    val isValidEmail = viewModel.isUEmailV(email.value.text)
    val isValidPassword = viewModel.isUPwV(password.value.text)
    val isValidConfirmPassword = viewModel.isConfirmPasswordMatch(password.value.text, confPassword.value.text)

    isEmailFormatCorrect.value = isValidEmail
    isPasswordFormatCorrect.value = isValidPassword
    isPasswordMatched.value = isValidConfirmPassword

    val emailGuideMessage = if (isEmailFormatCorrect.value) "Email format is correct" else "Invalid email format"
    val passwordGuideMessage = if (isPasswordFormatCorrect.value) "Password format is correct" else "Password must contain 8 characters with \n at least 1 uppercase, lowercase & numeric character"
    val confirmPasswordGuideMessage = if (isPasswordMatched.value) "Password matches" else "Password is not matches"

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray, RoundedCornerShape(16.dp))
                .verticalScroll(scrollState)
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
                    text = "Sign Up",
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
                    value = username.value,
                    onValueChange = { username.value = it }, // Added missing onValueChange lambda
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Username") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Email",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidEmail
                )

                Text(
                    text = emailGuideMessage,
                    color = if (isEmailFormatCorrect.value) Color.Blue else Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Password",
                    modifier = Modifier.align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(10.dp))

                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                var confPasswordVisible by rememberSaveable { mutableStateOf(false) }
                TextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        pwTyped.value = true
                                    },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Password") },
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
                    modifier = Modifier.fillMaxWidth(),
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
                    value = confPassword.value,
                    onValueChange = {
                        confPassword.value = it
                        pwTyped.value = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Password") },
                    singleLine = true,
                    visualTransformation = if (confPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                    trailingIcon = {
                        val image = if (confPasswordVisible)
                            R.drawable.visibility
                        else R.drawable.visibility_off

                        // Please provide localized description for accessibility services
                        val description = if (confPasswordVisible) "Hide password" else "Show password"
                        IconButton(onClick = { confPasswordVisible = !confPasswordVisible }) {
                            Icon(imageVector = ImageVector.vectorResource(id = image), description)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidConfirmPassword
                )

                Text(
                    text = confirmPasswordGuideMessage,
                    color = if (isPasswordMatched.value) Color.Blue else Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Security Question: ",
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "What was your favourite food as child ?",
                    modifier = Modifier.align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = secureQst.value,
                    onValueChange = {
                        secureQst.value = it
                    },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Enter Your Answer Here") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Text(
                    text = if (secureQst.value != "") "Valid Input" else "This Field Cannot be empty!",
                    color = if (secureQst.value != "") Color.Blue else Color.Red,
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
                            checked = checked.value,
                            onCheckedChange = { isChecked -> checked.value = isChecked },
                            colors = CheckboxDefaults.colors(checkedColor = Color.Green)
                        )
                        Text("I had read and agreed the Term and Privacy")
                    }
                }

                ReuseComponents.CustomButton(
                    text = "Sign Up",
                    onClick = {
                        if(checked.value) {
                            if (vSignUp) {
                                if ( viewModel.isUNameAv(username.value.text,userSU.value) &&
                                    viewModel.isEmailAv(email.value.text,userSU.value) &&
                                    viewModel.isConfirmPasswordMatch(password.value.text, confPassword.value.text)
                                    ){
//                                if ( viewModel.isUNameAv(username.value.text,userSU.value) ){
//                                    Toast.makeText(context, "A", Toast.LENGTH_SHORT).show()
//                                    if(viewModel.isEmailAv(email.value.text,userSU.value)) {
//                                        Toast.makeText(context, "B", Toast.LENGTH_SHORT).show()
//                                        if(viewModel.isConfirmPasswordMatch(password.value.text, confPassword.value.text)){
//                                            Toast.makeText(context, "C", Toast.LENGTH_SHORT).show()
//
//                                        }
//                                    }
//                                }
                                viewModel.addUser(
                                    context = context,
                                    db = db,
                                    userName = username.value.text,
                                    userEmail = email.value.text,
                                    userPw = password.value.text,
                                    userSecureQst = secureQst.value,
                                ) {
                                    walletViewModel.createWallet(db, context, it) {wallet_id ->
                                        val dbHandler: DBHandler = DBHandler(context)
                                        dbHandler.createWallet(wallet_id, it)
                                    }
                                }
                                showToast.value = true
                                checked.value = false
                                    }else{
                                        Toast.makeText(context,"Username or Email has been used, please enter a different one",Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(context,"Please fill up your Email and Password",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(context, "Please agree to the Terms and Privacy", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                if(showToast.value){
                    Toast.makeText(context,"Sign Up Successful",Toast.LENGTH_SHORT).show()
                    dbHandler.addNewUser(
                        username = username.value.text,
                        email = email.value.text,
                        password = password.value.text
                        )
                    navController.navigate(Screen.Login.route){
                        popUpTo(Screen.Login.route){
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
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Login.route) {
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
fun SignUpScreenPreview(){
    val context = LocalContext.current
    val db = DBHandler(context)
    SignUpScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        dbHandler = db,
        viewModel = UserViewModel(),
        walletViewModel = WalletViewModel()
    )
}
