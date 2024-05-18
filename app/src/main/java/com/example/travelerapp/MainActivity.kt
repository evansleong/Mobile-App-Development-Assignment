package com.example.travelerapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.travelerapp.ui.theme.TravelerAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContext:Context = this
        val mainDbHandler =DBHandler(appContext)
        setContent {
            var dark = remember {
                mutableStateOf(false)
            }
            TravelerAppTheme(darkTheme = dark.value) {
                navController = rememberNavController()
                SetUpNavGraph(
                    navController = navController,
                    dbHandler = mainDbHandler,
                    darkTheme = dark.value,
                    onDarkThemeChanged = {dark.value = !dark.value}
                    )

            }
        }
    }

    @Preview
    @Composable
    fun TravelerAppPreview() {
        TravelerApp()
    }

    @Composable
    fun TravelerApp() {
        MaterialTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Hello, Jetpack Compose!")
            }
        }
    }
}
