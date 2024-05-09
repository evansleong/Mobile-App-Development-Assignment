package com.example.travelerapp

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.data.Trip
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AgencyEditPackageScreen(
    navController: NavController,
    trip: Trip,
    context: Context,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore
    val tripState = remember { mutableStateOf<Trip?>(null) }

    val selectedPackage = tripViewModel.selectedTripId

    var editedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedOptions by remember { mutableStateOf(trip.options.toMutableSet()) }


    LaunchedEffect(selectedPackage) {
        tripViewModel.readSingleTrip(db, selectedPackage.toString()) { trip ->
            tripState.value = trip
        }
    }

    LaunchedEffect(trip) {
        selectedOptions = trip.options.toMutableSet()
    }

    tripState.value?.let { trip ->
        var isEditing by remember { mutableStateOf(false) }
        var editedTripName by remember { mutableStateOf(trip.tripName) }
        var editedTripFees by remember { mutableStateOf(trip.tripFees) }
        var editedTripDeposit by remember { mutableStateOf(trip.tripDeposit) }
        var editedTripDesc by remember { mutableStateOf(trip.tripDesc) }
        var editedDepartureDate by remember { mutableStateOf(trip.depDate) }
        var editedReturnDate by remember { mutableStateOf(trip.retDate) }
        var editedTripLength by remember { mutableStateOf(trip.tripLength) }
        var editedOptions by remember { mutableStateOf(trip.options.joinToString(separator = "\n")) }
        var readOldImageUri by remember { mutableStateOf(trip.tripUri) }

        fun handleImageUpload(context: Context, imageUri: Uri?) {
            tripViewModel.uploadImage(
                context = context,
                imageUri = imageUri,
                onSuccess = { downloadUrl ->
                    editedImageUri = Uri.parse(downloadUrl)
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

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val title = "Edit ${trip.tripName}"
            ReuseComponents.TopBar(title = title, navController, showBackButton = true)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { pickImage(imagePicker) }
                    .background(Color.Gray)
            ) {
                val displayImageUri = editedImageUri ?: readOldImageUri
                    Image(
                        painter = rememberAsyncImagePainter(displayImageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )
                    IconButton(
                        onClick = {
                            pickImage(imagePicker)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Cancel")
                    }
                }
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .width(350.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(text = "Trip Name", fontWeight = FontWeight.Bold)
                        EditableStringFieldWithButton(
                            text = editedTripName,
                            onTextChanged = { editedTripName = it }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text(text = "Trip Description", fontWeight = FontWeight.Bold)
                        EditableStringFieldWithButton(
                            text = editedTripDesc,
                            onTextChanged = { editedTripDesc = it }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text(text = "Trip Fees", fontWeight = FontWeight.Bold)
                        EditableDoubleFieldWithButton(
                            value = editedTripFees,
                            onValueChanged = { editedTripFees = it }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text(text = "Trip Deposit", fontWeight = FontWeight.Bold)
                        EditableDoubleFieldWithButton(
                            value = editedTripDeposit,
                            onValueChanged = { editedTripDeposit = it }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Options (Select at least 1):", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        val allOptions = listOf("Included breakfast", "Free Parking", "Travel Insurance", "Tipping and Taxes", "Full Board Meals", "Airport Transport")
                        allOptions.forEach { option ->
                            OptionsCheckbox(option, selectedOptions, trip)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        ReuseComponents.CustomButton(
                            text = "Save Changes",
                            onClick = {
                                if (selectedOptions.isEmpty()) {
                                    selectedOptions = trip.options.toMutableSet() // Keep previous options
                                }
                                tripViewModel.editTrip(
                                    context = navController.context,
                                    db = Firebase.firestore,
                                    tripId = trip.tripId,
                                    newTripUri = editedImageUri?.toString() ?: trip.tripUri,
                                    newTripName = editedTripName,
                                    newTripLength = editedTripLength,
                                    newTripFees = editedTripFees,
                                    newTripDesc = editedTripDesc,
                                    newDeptDate = editedDepartureDate,
                                    newRetDate = editedReturnDate,
                                    newOptions = selectedOptions.toList(),
                                )
                                navController.popBackStack()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditableStringFieldWithButton(
    text: String,
    onTextChanged: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = text,
            onValueChange = { onTextChanged(it) },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun EditableDoubleFieldWithButton(
    value: Double,
    onValueChanged: (Double) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = value.toString(),
            onValueChange = { text ->
                // Handle parsing the text to Double and update the value
                onValueChanged(text.toDoubleOrNull() ?: value)
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun OptionsCheckbox(option: String, selectedOptions: MutableSet<String>, trip: Trip) {
    var isChecked by remember { mutableStateOf(option in selectedOptions || option in trip.options) }

    LaunchedEffect(selectedOptions) {
        isChecked = option in selectedOptions || option in trip.options
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = option,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        androidx.compose.material3.Checkbox(
            checked = isChecked,
            onCheckedChange = { newCheckedState ->
                isChecked = newCheckedState
                if (newCheckedState) {
                    selectedOptions += option // Add the option to the selected set
                } else {
                    selectedOptions -= option // Remove the option from the selected set
                }
            }
        )
    }
    Divider()
}


