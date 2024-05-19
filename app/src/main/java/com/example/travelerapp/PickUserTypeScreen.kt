package com.example.travelerapp

import ReuseComponents
import android.text.Layout
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.travelerapp.R
import com.example.travelerapp.R.*
import com.example.travelerapp.R.drawable.blank_profile_picture_973460_1_1_1024x1024
import com.example.travelerapp.ui.theme.CusFont1
import com.example.travelerapp.ui.theme.CusFont2

@Composable
fun PickUserTypeScreen(
    navController: NavController
) {
    var selectedOption by remember { mutableStateOf("User") }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
            )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(116.dp))

            Text(
                text = "Choose User Type",
                style = TextStyle(color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.ExtraBold,fontFamily = CusFont2)
            )

            Spacer(modifier = Modifier.height(96.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {

                Box(
                    modifier = Modifier
//                    .fillMaxWidth()
                        .weight(1f)
                        .height(200.dp)
                        .padding(8.dp, 0.dp)
                        .background(
                            color = if (selectedOption == "User") Color.Blue else Color.LightGray,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable {
                            selectedOption = "User"
                            Log.d("Clickable", "user clicked $selectedOption")
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(drawable.blank_profile_picture_973460_1_1_1024x1024),
                            contentDescription = "user_img",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .height(120.dp)
                        )
                        Text(
                            text = "User",
                            color = Color.White,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                    .fillMaxWidth()
                        .weight(1f)
                        .height(200.dp)
                        .padding(8.dp, 0.dp)
                        .background(
                            color = if (selectedOption == "Agency") Color.Blue else Color.LightGray,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable {
                            selectedOption = "Agency"
                            Log.d("Clickable", "agency clicked $selectedOption")
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(drawable.blank_profile_picture_973460_1_1_1024x1024),
                            contentDescription = "user_img",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .height(120.dp)
                        )
                        Text(
                            text = "Agency",
                            color = Color.White,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Selected Option: $selectedOption",
//            )

            Spacer(modifier = Modifier.height(80.dp))
            
            ReuseComponents.CustomButton(
                text = "Next",
                onClick = {
                    if(selectedOption=="User"){
                        navController.navigate(route = Screen.Login.route)
                    }else{
                        navController.navigate(route = Screen.AgencyLogin.route)
                    }
                }
            )
                

        }
    }
}