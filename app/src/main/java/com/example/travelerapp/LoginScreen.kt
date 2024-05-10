package com.example.travelerapp

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.data.User
import com.example.travelerapp.data.Wallet
import com.example.travelerapp.viewModel.WalletViewModel
import com.example.travelerapp.viewModel.UserViewModel
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

@Composable
fun LoginScreen(
    navController: NavController,
    context: Context,
    viewModel: UserViewModel,
    walletViewModel: WalletViewModel
) {
//    val lsContext: Context = this
    val db = Firebase.firestore
    val checked = remember { mutableStateOf(false) }
    val logInEmail = remember {
        mutableStateOf("")
    }

    val logInPw = remember {
        mutableStateOf("")
    }

    val users = remember {
        mutableStateOf((emptyList<User>()))
    }

    viewModel.readUData(db) { userList ->
        users.value = userList
    }

    val walletList = remember { mutableStateOf(emptyList<Wallet>()) }

    walletViewModel.readWallets(db) { wallet ->
        walletList.value = wallet
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
                    text = "Log In",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Email",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = logInEmail.value,
                    onValueChange = {
                        logInEmail.value = it
                    },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Password",
                    modifier = Modifier.align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = logInPw.value,
                    onValueChange = {
                        logInPw.value = it
                    },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Password") },
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
                        val email = logInEmail.value
                        val password = logInPw.value
                        val loginSuccessful = viewModel.checkULoginCred(email, password, users.value)

                        if (loginSuccessful != null) {
                            viewModel.loggedInUser = loginSuccessful

                            val wallet = walletViewModel.checkWallet(viewModel.loggedInUser!!.userId, walletList.value)

                            if (wallet != null){
                                walletViewModel.userWallet = wallet
                                if(wallet.walletPin != "null"){
                                    Toast.makeText(context, "Login Up Successful", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) {
                                            inclusive = true
                                        }
                                    }
                                }else{
                                    Toast.makeText(context, "Add Pin", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.AddPIN.route) {
                                        popUpTo(Screen.AddPIN.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            } else{
                                Toast.makeText(context, "wallet null", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }

//                        if(checked.value){
//                            if(logInEmail.value != "" && logInPw.value != ""){
//                        val emailTemp = logInEmail.getValueAsString()
//                        val pwTemp = logInPw.getValueAsString()
//                        val userExst = viewModel.checkULC(emailTemp,pwTemp,users.value)
////                        val userExst = dbHandler.getUserByEmailNPw(emailTemp, pwTemp)
//
//                        if(userExst!=null) {
//                            navController.navigate(route = Screen.Home.route) {
//                                popUpTo(Screen.Home.route) {
//                                    inclusive = true
//                                }
//                            }
////                        navController.navigate(route = Screen.AddPIN.route) {
////                            popUpTo(Screen.AddPIN.route) {
////                                inclusive = true
////                            }
//                        }
//                        }
//                      }

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
                            navController.navigate(Screen.Signup.route) {
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

@Composable
@Preview
fun LoginScreenPreview(){
    val context = LocalContext.current
    val DBH = DBHandler(context)
    LoginScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        viewModel = UserViewModel(),
        walletViewModel = WalletViewModel()
    )
}