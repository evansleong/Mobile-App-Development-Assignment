package com.example.travelerapp

import ReuseComponents
import android.content.Context
import android.media.Image
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.viewModel.AgencyViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID


@Composable
fun AgencySettingScreen(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel
) {
    val storage = FirebaseStorage.getInstance().reference
    val firestore = FirebaseFirestore.getInstance()

    val loggedInAgency = viewModel.loggedInAgency

    val changeName = remember {
        mutableStateOf(TextFieldValue())
    }
    val changePw = remember {
        mutableStateOf(TextFieldValue())
    }
    var changeImgUri by remember {
        mutableStateOf<Uri?>(null)
    }

//    var readOldImageUri by remember { mutableStateOf("") }

    val darkTheme = remember {
        mutableStateOf(false)
    }

    var dropCheck = remember {
        mutableStateOf(false)
    }

    var dropChose = remember {
        mutableStateOf("English")
    }

    LaunchedEffect(loggedInAgency) {
        loggedInAgency?.agencyPicture?.let {
            changeImgUri = Uri.parse(it)
        }
    }

//    changeImgUri = Uri.parse(loggedInAgency?.agencyPicture ?: "")

    fun handleImageUpload(context: Context, imageUri: Uri?) {
        viewModel.uploadImage(
            context = context,
            imageUri = imageUri,
            onSuccess = { downloadUrl ->
                changeImgUri = Uri.parse(downloadUrl)
//                readOldImageUri = downloadUrl
            },
            onFailure = { exception ->
                Log.e("ImageUpload", "Error uploading image: ${exception.message}")
            }
        )
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        handleImageUpload(context, uri)
    }


    fun pickImage(imagePicker: ActivityResultLauncher<String>) {
        imagePicker.launch("image/*")
    }

//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        handleImageUpload(context, uri)
//    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = "Settings"
        ReuseComponents.TopBar(title = title, navController, showBackButton = true,isAtSettingPage = true)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color.White),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(text = title)
            item {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .border(
                            BorderStroke(2.dp, Color(0xffafafaf)),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .fillMaxWidth()
                        .height(90.dp),
                ) {
                    Surface (
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFf9f9f9),
                        shape = RoundedCornerShape(20.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
//                                .background(color = Color.LightGray)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
//                            if (changeImgUri != null || readOldImageUri.isNotEmpty()) {
                            if (changeImgUri != null) {
//                                val displayImageUri = changeImgUri ?: Uri.parse(readOldImageUri)
                                Image(
                                    painter = rememberAsyncImagePainter(changeImgUri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                ReuseComponents.RoundImg(
                                    Modifier,
                                    painter = painterResource(R.drawable.blank_profile_picture_973460_1_1_1024x1024),
                                    contentDescription = null
                                )
                            }

//                            if (changeImgUri == null) {
//                                ReuseComponents.RoundImg(
//                                    Modifier,
//                                    painter = painterResource(R.drawable.blank_profile_picture_973460_1_1_1024x1024),
//                                    contentDescription = null
//                                )
//                            } else {
//                                Image(
//                                    painter = rememberAsyncImagePainter(changeImgUri),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(64.dp)
//                                        .clip(CircleShape),
//                                    contentScale = ContentScale.Crop
//                                )
//                            }
                            Column(
                                modifier = Modifier.fillMaxSize()
                                    .clickable { pickImage(imagePicker) }
                            ) {
                                Text("Your Photo",
                                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp),
                                    style = TextStyle(fontSize = 18.sp)
                                )

                                Text(text = "Adding a profile picture makes\nyour profile more personalized.",
                                    modifier = Modifier
                                        .padding(bottom = 0.dp),
                                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Light, color = Color(0xff959595)))
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Change Username",
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    TextField(
                        value = changeName.value,
                        onValueChange = { changeName.value = it },
                        shape = RoundedCornerShape(16.dp),
                        label = { BasicText(text = "Insert New Username") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .border(BorderStroke(0.dp, Color.Transparent))
                    )

                    Text(
                        text = "Change Password",
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    TextField(
                        value = changePw.value,
                        onValueChange = { changePw.value = it },
                        shape = RoundedCornerShape(16.dp),
                        label = { BasicText(text = "Insert New Username") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .border(BorderStroke(0.dp, Color.Transparent))
                    )

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){

                        Text(
                            text = "Dark Theme",
                            modifier = Modifier
                                .padding(bottom = 0.dp),
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )

                        Switch(checked = darkTheme.value,
                            onCheckedChange = {darkTheme.value = it})
                    }

                    Text(
                        text = "Languages",
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable(onClick = { dropCheck.value = true })
                            .border(
                                BorderStroke(2.dp, Color.Transparent),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color(0xFFf9f9f9),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){

                                Text(
                                    text = dropChose.value,
                                    modifier = Modifier.padding(start = 15.dp)
                                )
                                DropdownMenu(
                                    expanded = dropCheck.value,
                                    onDismissRequest = { dropCheck.value = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    DropdownMenuItem(
                                        text = {Text(text = "English")},
                                        onClick = {
                                            dropChose.value = "English"
                                            dropCheck.value = false
                                        })
                                    DropdownMenuItem(
                                        text = {Text(text = "Chinese")},
                                        onClick = {
                                            dropChose.value = "Chinese"
                                            dropCheck.value = false
                                        })
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Settings",
                                    tint = Color.Black,
                                    modifier = Modifier.padding(end = 15.dp)
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            viewModel.editAgencyPicture(
                                context = navController.context,
                                db = Firebase.firestore,
                                agencyId = loggedInAgency?.agencyId ?: "",
                                newPicture = changeImgUri.toString()
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Update Profile Picture")
                    }
                }
            }

        }

        ReuseComponents.NavBar(text = title, navController = navController)
    }
    if(darkTheme.value){
        Toast.makeText(context,"Dark Theme Applied",Toast.LENGTH_SHORT).show()
    }

//    changeImgUri.let { uri ->
//        LaunchedEffect(uri) {
//            val imageRef: StorageReference = storage.child("agency_pictures/${UUID.randomUUID()}")
//            try {
//                imageRef.putFile(uri).await()
//                val downloadUrl = imageRef.downloadUrl.await()
//                firestore.collection("agencies")
//                    .document(loggedInAgency?.agencyId ?: "") // Replace with the actual document ID of the agency
//                    .update("agencyPicture", downloadUrl.toString())
//                Toast.makeText(context, "Profile picture updated successfully", Toast.LENGTH_SHORT).show()
//                viewModel.editAgencyPicture(
//                    context = navController.context,
//                    db = Firebase.firestore,
//                    agencyId = loggedInAgency?.agencyId ?: "",
//                    newPicture = changeImgUri.toString()
//                )
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        context,
//                        "Failed to update profile picture: ${e.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }


}

@Composable
@Preview
fun AgencySettingScreenPreview(){
    AgencySettingScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        viewModel = AgencyViewModel()
    )
}