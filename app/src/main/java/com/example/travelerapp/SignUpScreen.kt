package com.example.travelerapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
    var username = remember { mutableStateOf(TextFieldValue()) }
    var email = remember { mutableStateOf(TextFieldValue()) }
    var password = remember { mutableStateOf(TextFieldValue()) }
    val checked = remember { mutableStateOf(false) }
    val showToast = remember { mutableStateOf(false) }

    val vEmail = viewModel.isUEmailV(email.value.text)
    val vPw = viewModel.isUPwV(password.value.text)

    val vSignUp = vEmail && vPw && checked.value

    val userSU = remember {
        mutableStateOf(emptyList<User>())
    }

    viewModel.readUData(db){ userList -> userSU.value = userList }

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

                Spacer(modifier = Modifier.height(15.dp))

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
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Password",
                    modifier = Modifier.align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(10.dp))

                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
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
                    modifier = Modifier.fillMaxWidth()
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
//                        navController.navigate(Screen.Home.route) {
//                                                popUpTo(Screen.Home.route) {
//                                                    inclusive = true
//                                                }
//                                            }
                        if(checked.value) {
                            if (vSignUp) {
                                if(viewModel.isUNameAv(username.value.text,userSU.value) &&
                                    viewModel.isEmailAv(email.value.text,userSU.value)){
                                viewModel.addUser(
                                    context = context,
                                    db = db,
                                    userName = username.value.text,
                                    userEmail = email.value.text,
                                    userPw = password.value.text
                                ){
                                    walletViewModel.createWallet(db, context, it) {wallet_id ->
                                        val dbHandler: DBHandler = DBHandler(context)
                                        dbHandler.createWallet(wallet_id, it)
                                    }
                                }
                                showToast.value = true
                                checked.value = false
//                                auth.createUserWithEmailAndPassword(email, password)
//                                    .addOnCompleteListener { task ->
//                                        if (task.isSuccessful) {
//                                            // User created successfully
//                                            navController.navigate(Screen.AddPIN.route) {
//                                                popUpTo(Screen.AddPIN.route) {
//                                                    inclusive = true
//                                                }
//                                            }
//                                        }
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
