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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.viewModel.AgencyViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun AgencyChangeUsernameScreen(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel,
) {
    val db = Firebase.firestore

    val user = viewModel.loggedInAgency
    val username = remember{ mutableStateOf("") }

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
                    text = "Change Username",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Username",
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = username.value,
                    onValueChange = {
                        username.value = it
                    },
                    shape = RoundedCornerShape(16.dp),
                    label = { BasicText(text = "Insert New Username") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Text(
                    text = if (username.value != "") "Valid Input" else "This Field Cannot be empty!",
                    color = if (username.value != "") Color.Blue else Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                ReuseComponents.CustomButton(
                    text = "Change Username",
                    onClick = {
                        if (username.value != "") {
                            viewModel.updateAgencyData(context, db, user?.agencyId.toString(), username.value, user?.agencyPassword.toString())
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AgencyChangeUsernamePreview() {
    AgencyChangeUsernameScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        viewModel = AgencyViewModel()
    )
}
