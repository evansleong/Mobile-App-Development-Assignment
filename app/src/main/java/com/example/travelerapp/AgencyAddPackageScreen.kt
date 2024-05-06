package com.example.travelerapp

import ReuseComponents
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    context: Context,
    viewModel: AgencyViewModel
) {
    val loggedInAgency = viewModel.loggedInAgency

    val db = Firebase.firestore

    val activity = context as Activity

    var uploadedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uploadedImageUri = uri
        }

    var expanded by remember { mutableStateOf(false) }

    var isDialogOpen by remember { mutableStateOf(false) }

    var showDeptDateDialog by remember { mutableStateOf(false) }

    var showRetDateDialog by remember { mutableStateOf(false) }

    val customTripLength = remember { mutableStateOf(TextFieldValue()) }

    val tripLengthOptions = listOf("1 DAY TRIP", "2 DAYS 1 NIGHT", "3 DAYS 2 NIGHTS", "OTHERS")

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

    val tripPackageDeptDate = rememberDatePickerState()

    val tripPackageRetDate = rememberDatePickerState()

    val deptDateMillisToLocalDate = tripPackageDeptDate.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }

    val retDateMillisToLocalDate = tripPackageRetDate.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }

    val deptDateToString = deptDateMillisToLocalDate?.let {
        DateUtils().dateToString(deptDateMillisToLocalDate)
    } ?: ""

    val retDateToString = retDateMillisToLocalDate?.let {
        DateUtils().dateToString(retDateMillisToLocalDate)
    } ?: ""

    var isOthersSelected by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf(emptyList<String>()) }

    // Function to launch image picker
    fun pickImage() {
        imagePicker.launch("image/*")
    }

    fun handleOthersOptionClick() {
        isOthersSelected = true
    }

    // Check if an image has been uploaded
    val isImageUploaded = uploadedImageUri != null

    fun saveCustomTripLength() {
        tripLength.value = customTripLength.value.text
        isDialogOpen = false
    }

    // Validation functions
    fun validateInputs(): Boolean {
        var isValid = true

        // Trip Image validation
        if (uploadedImageUri == null) {
            isValid = false
            Toast.makeText(context, "Please upload trip image", Toast.LENGTH_SHORT).show()
        }

        // Trip Uri validation
        if (tripLength.value.isEmpty() || (tripLength.value == "OTHERS" && customTripLength.value.text.isEmpty())) {
            isValid = false
            Toast.makeText(context, "Please enter valid trip length", Toast.LENGTH_SHORT).show()
        }

        // Trip Length validation
        if (tripLength.value.isEmpty() || (tripLength.value == "OTHERS" && customTripLength.value.text.isEmpty())) {
            isValid = false
            Toast.makeText(context, "Please enter valid trip length", Toast.LENGTH_SHORT).show()
        }

        // Trip Name validation
        if (tripPackageName.value.text.isEmpty()) {
            isValid = false
            Toast.makeText(context, "Please enter trip name", Toast.LENGTH_SHORT).show()
        }

        // Trip Fees validation
        if (tripPackageFees.value.text.isEmpty() || tripPackageFees.value.text.toDoubleOrNull() == null) {
            isValid = false
            Toast.makeText(context, "Please enter valid trip fees", Toast.LENGTH_SHORT).show()
        }

        // Trip Deposit validation
        if (tripPackageDeposit.value.text.isEmpty() || tripPackageDeposit.value.text.toDoubleOrNull() == null) {
            isValid = false
            Toast.makeText(context, "Please enter valid trip deposit", Toast.LENGTH_SHORT).show()
        }

        // Description validation
        if (tripPackageDesc.value.text.isEmpty()) {
            isValid = false
            Toast.makeText(context, "Please enter trip description", Toast.LENGTH_SHORT).show()
        }

        // Departure Date validation
        if (deptDateMillisToLocalDate == null) {
            isValid = false
            Toast.makeText(context, "Please select valid departure date", Toast.LENGTH_SHORT).show()
        }

        // Return Date validation
        if (retDateMillisToLocalDate == null) {
            isValid = false
            Toast.makeText(context, "Please select valid return date", Toast.LENGTH_SHORT).show()
        }

        // Options validation
        if (selectedOption.isEmpty()) {
            isValid = false
            Toast.makeText(context, "Please select at least one option", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var dbHandler: DBHandler = DBHandler(context)

        val title = "Welcome, ${loggedInAgency?.agencyUsername}"
        ReuseComponents.TopBar(
            title = title,
            navController,
            showBackButton = true
        )

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
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Trip Length", fontWeight = FontWeight.Bold)
                }

                item {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                    ) {
                        TextField(
                            value = tripLength.value,
                            onValueChange = {
                                if (it == "OTHERS") {
                                    handleOthersOptionClick()
                                } else {
                                    tripLength.value = it
                                }
                            },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .width(350.dp),
                            shape = RoundedCornerShape(20.dp),
                        )

                        if (expanded) {
                            ExposedDropdownMenu(
                                expanded = true,
                                onDismissRequest = { expanded = false }
                            ) {
                                tripLengthOptions.forEach { tripLengthOption ->
                                    DropdownMenuItem(
                                        text = { Text(text = tripLengthOption) },
                                        onClick = {
                                            if (tripLengthOption == "OTHERS") {
                                                handleOthersOptionClick()
                                            } else {
                                                tripLength.value = tripLengthOption
                                                expanded = false
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    if (isOthersSelected) {
                        OthersOptionInputDialog(
                            onContinue = { customOption ->
                                tripLength.value = customOption
                                isOthersSelected = false
                            },
                            onDismiss = { isOthersSelected = false }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Trip Name", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripPackageName.value,
                        onValueChange = { tripPackageName.value = it },
                        placeholder = { Text(text = "Enter the trip location") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Trip Fees", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripPackageFees.value,
                        onValueChange = { tripPackageFees.value = it },
                        placeholder = { Text(text = "Enter the trip fees:") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Deposit", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripPackageDeposit.value,
                        onValueChange = { tripPackageDeposit.value = it },
                        placeholder = { Text(text = "Enter the trip deposit:") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Description", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripPackageDesc.value,
                        onValueChange = { tripPackageDesc.value = it },
                        placeholder = { Text(text = "Enter the trip desc:") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Departure Date", fontWeight = FontWeight.Bold)
                    Box {
                        TextField(
                            value = deptDateToString,
                            onValueChange = { /* No operation, as this is non-editable */ },
                            readOnly = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { showDeptDateDialog = true }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Icon(
                            painter = painterResource(R.drawable.calender),
                            contentDescription = "Calendar",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { showDeptDateDialog = true }
                                .align(Alignment.CenterEnd)
                                .padding(end = 16.dp)
                        )
                    }
                    if (showDeptDateDialog) {
                        DatePickerDialog(
                            onDismissRequest = { showDeptDateDialog = false },
                            confirmButton = {
                                Button(
                                    onClick = { showDeptDateDialog = false }
                                ) {
                                    Text(text = "ok")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showDeptDateDialog = false }
                                ) {
                                    Text(text = "Cancel")
                                }
                            }
                        ) {
                            DatePicker(
                                state = tripPackageDeptDate
                            )
                        }
                    }
                }

// Inside your Composable function
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Return Date", fontWeight = FontWeight.Bold)
                    Box {
                        TextField(
                            value = retDateToString,
                            onValueChange = { /* No operation, as this is non-editable */ },
                            readOnly = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { showRetDateDialog = true }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Icon(
                            painter = painterResource(R.drawable.calender),
                            contentDescription = "Calendar",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { showRetDateDialog = true }
                                .align(Alignment.CenterEnd)
                                .padding(end = 16.dp)
                        )
                    }
                    if (showRetDateDialog) {
                        DatePickerDialog(
                            onDismissRequest = { showRetDateDialog = false },
                            confirmButton = {
                                Button(
                                    onClick = { showRetDateDialog = false }
                                ) {
                                    Text(text = "ok")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showRetDateDialog = false }
                                ) {
                                    Text(text = "Cancel")
                                }
                            }
                        ) {
                            DatePicker(
                                state = tripPackageRetDate
                            )
                        }
                    }
                }


                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Option (At least 1):", fontWeight = FontWeight.Bold)
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
                            if (validateInputs()) {
                            dbHandler.addNewTrip(
                                tripPackageName.value.text,
                                tripLength.value,
                                tripPackageFees.value.text.toDouble(),
                                tripPackageDeposit.value.text.toDouble(),
                                tripPackageDesc.value.text,
                                deptDateToString,
                                retDateToString,
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
                                tripPackageDeptDate = deptDateToString,
                                tripPackageRetDate = retDateToString,
                                uploadedImageUri = uploadedImageUri?.toString(),
                                selectedOption = selectedOption,
                                agencyUsername = loggedInAgency?.agencyUsername ?: "user"
                            )
                            Toast.makeText(context, "Trip Added to Database", Toast.LENGTH_SHORT)
                                .show()
                        }
                        })
                }
            }
        }
    }
}

@Composable
fun OthersOptionInputDialog(onContinue: (String) -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        var customOption by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enter Trip Length",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = customOption,
                    onValueChange = { customOption = it },
                    placeholder = { Text("Custom Option") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onContinue(customOption) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }
    }
}



@Preview
@Composable
fun PreviewAgencyAddPackageScreen() {
    AgencyAddPackageScreen(navController = rememberNavController(), context = LocalContext.current, viewModel = AgencyViewModel())
}