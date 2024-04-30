package com.example.travelerapp

import ReuseComponents
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun AgencyAddPackageScreen(
    navController: NavController,
    context: Context
) {
    val db = Firebase.firestore

    val activity = context as Activity

    var uploadedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker =
        rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uploadedImageUri = uri
        }

    var expanded by remember { mutableStateOf(false) }

    val tripLengthOptions = listOf("1 DAY TRIP", "2 DAYS 1 NIGHT", "3 DAYS 2 NIGHTS")

    val tripLength = remember {
        mutableStateOf(tripLengthOptions[0])
    }

    val tripPackageName = remember {
        mutableStateOf(TextFieldValue())
    }

    val tripPackageFees = remember {
        mutableStateOf(TextFieldValue())
    }

    val tripPackageDeposit = remember {
        mutableStateOf(TextFieldValue())
    }

    val tripPackageDesc = remember {
        mutableStateOf(TextFieldValue())
    }

    val tripPackageDeptDate = remember {
        mutableStateOf(TextFieldValue())
    }

    val tripPackageRetDate = remember {
        mutableStateOf(TextFieldValue())
    }

    var isChecked by remember { mutableStateOf(false) }

    val value = if (isChecked) 1 else 0

    var selectedOption by remember { mutableStateOf(emptyList<String>()) }


    // Function to launch image picker
    fun pickImage() {
        imagePicker.launch("image/*")
    }

    // Check if an image has been uploaded
    val isImageUploaded = uploadedImageUri != null

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var dbHandler: DBHandler = DBHandler(context)

        val title = "Add Package"
        ReuseComponents.TopBar(title = title, navController, showBackButton = true)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { pickImage() }
                .background(Color.Gray) // Grey background when no image is uploaded
        ) {
            // Display the uploaded image or a placeholder
            if (isImageUploaded) {
                Image(
                    painter = rememberImagePainter(uploadedImageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
                IconButton(
                    onClick = {
                        uploadedImageUri = null
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Cancel")
                }
            } else {
                Text(
                    text = "Upload Photo",
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                )
            }
        }

        Box(
            modifier = Modifier
                .width(350.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Trip Length"
                    )
                }
                item {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = it
                        },
                    ) {
                        TextField(
                            value = tripLength.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .width(350.dp),
                            shape = RoundedCornerShape(20.dp)
                        )

                        if (expanded) {
                            ExposedDropdownMenu(
                                expanded = true,
                                onDismissRequest = {
                                    expanded = false
                                }
                            ) {
                                tripLengthOptions.forEach { tripLengthOptions: String ->
                                    DropdownMenuItem(
                                        text = { Text(text = tripLengthOptions) },
                                        onClick = {
                                            tripLength.value = tripLengthOptions
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Trip Name"
                    )
                    TextField(
                        value = tripPackageName.value,
                        onValueChange = { tripPackageName.value = it },
                        placeholder = { Text(text = "Enter the trip name:") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Trip Fees"
                    )
                    TextField(
                        value = tripPackageFees.value,
                        onValueChange = { tripPackageFees.value = it },
                        placeholder = { Text(text = "Enter the trip fees:") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Deposit"
                    )
                    TextField(
                        value = tripPackageDeposit.value,
                        onValueChange = { tripPackageDeposit.value = it },
                        placeholder = { Text(text = "Enter the trip deposit:") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Description"
                    )
                    TextField(
                        value = tripPackageDesc.value,
                        onValueChange = { tripPackageDesc.value = it },
                        placeholder = { Text(text = "Enter the trip desc:") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Departure Date"
                    )
                    TextField(
                        value = tripPackageDeptDate.value,
                        onValueChange = { tripPackageDeptDate.value = it },
                        placeholder = { Text(text = "Enter the trip dept date:") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Return Date"
                    )
                    TextField(
                        value = tripPackageRetDate.value,
                        onValueChange = { tripPackageRetDate.value = it },
                        placeholder = { Text(text = "Enter the trip Ret date:") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
                item {
                    Text(
                        text = "Active"
                    )
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        modifier = Modifier.clickable { isChecked = !isChecked }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text("Option (can choose more than 1):")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Included breakfast",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = selectedOption.contains("Included breakfast"),
                            onCheckedChange = {
                                selectedOption =
                                    if (it) selectedOption + "Included breakfast" else selectedOption - "Included breakfast"
                            }
                        )
                    }
                    Divider()
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Free Parking",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = selectedOption.contains("Free Parking"),
                            onCheckedChange = {
                                selectedOption =
                                    if (it) selectedOption + "Free Parking" else selectedOption - "Free Parking"
                            }
                        )
                    }
                    Divider()
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Travel Insurance",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = selectedOption.contains("Travel Insurance"),
                            onCheckedChange = {
                                selectedOption =
                                    if (it) selectedOption + "Travel Insurance" else selectedOption - "Travel Insurance"
                            }
                        )
                    }
                    Divider()
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tipping and Taxes",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = selectedOption.contains("Tipping and Taxes"),
                            onCheckedChange = {
                                selectedOption =
                                    if (it) selectedOption + "Tipping and Taxes" else selectedOption - "Tipping and Taxes"
                            }
                        )
                    }
                    Divider()
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Full Board Meals",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = selectedOption.contains("Full Board Meals"),
                            onCheckedChange = {
                                selectedOption =
                                    if (it) selectedOption + "Full Board Meals" else selectedOption - "Full Board Meals"
                            }
                        )
                    }
                    Divider()
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Airport transport",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = selectedOption.contains("Airport transport"),
                            onCheckedChange = {
                                selectedOption =
                                    if (it) selectedOption + "Airport transport" else selectedOption - "Airport transport"
                            }
                        )
                    }
                    Divider()
                }


                item {
                    ReuseComponents.CustomButton(
                        text = "Save",
                        onClick = {
                            dbHandler.addNewTrip(
                                tripPackageName.value.text,
                                tripLength.value,
                                tripPackageFees.value.text.toDouble(),
                                tripPackageDeposit.value.text.toDouble(),
                                tripPackageDesc.value.text,
                                tripPackageDeptDate.value.text,
                                tripPackageRetDate.value.text,
                                uploadedImageUri?.toString(),
                                selectedOption
                            )
                            addDataToFirestore(
                                context = context,
                                db = db,
                                tripPackageName = tripPackageName.value.text,
                                tripLength = tripLength.value,
                                tripPackageFees = tripPackageFees.value.text.toDouble(),
                                tripPackageDeposit = tripPackageDeposit.value.text.toDouble(),
                                tripPackageDesc = tripPackageDesc.value.text,
                                tripPackageDeptDate = tripPackageDeptDate.value.text,
                                tripPackageRetDate = tripPackageRetDate.value.text,
                                uploadedImageUri = uploadedImageUri?.toString(),
                                selectedOption = selectedOption
//                                isChecked = isChecked
                            )
                            Toast.makeText(context, "Trip Added to Database", Toast.LENGTH_SHORT)
                                .show()
                        })
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewAgencyAddPackageScreen() {
    AgencyAddPackageScreen(navController = rememberNavController(), context = LocalContext.current)
}
