package com.example.travelerapp.agencyScreen

import ReuseComponents
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.DateUtils
import com.example.travelerapp.R
import com.example.travelerapp.localDb.DBHandler
import com.example.travelerapp.screen.Screen
import com.example.travelerapp.viewModel.AgencyViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun AgencyAddPackageScreen(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel,
    tripViewModel: TripViewModel
) {
    val currentDate = LocalDate.now()

    val loggedInAgency = viewModel.loggedInAgency

    val db = Firebase.firestore

    var uploadedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    // Show Snackbar immediately when the composable is executed
    LaunchedEffect(true) {
        snackbarHostState.showSnackbar(
            message = "Please complete all fields",
            actionLabel = "Dismiss"
        )
    }

    var showConfirmDialog by remember { mutableStateOf(false) }

    var isDialogOpen by remember { mutableStateOf(false) }

    var showDeptDateDialog by remember { mutableStateOf(false) }

    var showRetDateDialog by remember { mutableStateOf(false) }

    val customTripLength = remember { mutableStateOf(TextFieldValue()) }

    val tripId = UUID.randomUUID().toString().substring(0, 6)

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

    val tripAvailable = remember {
        mutableStateOf(TextFieldValue())
    }

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

    fun pickImage(imagePicker: ActivityResultLauncher<String>) {
        imagePicker.launch("image/*")
    }

    fun handleImageUpload(context: Context, imageUri: Uri?) {
        tripViewModel.uploadImage(
            context = context,
            imageUri = imageUri,
            onSuccess = { downloadUrl ->
                uploadedImageUri = Uri.parse(downloadUrl)
            },
            onFailure = { exception ->
                Log.e("ImageUpload", "Error uploading image: ${exception.message}")
            }
        )
    }

    // Function to calculate trip length
    fun calculateTripLength(departureMillis: Long?, returnMillis: Long?): String {
        if (departureMillis != null && returnMillis != null) {
            val departureDate = DateUtils().convertMillisToLocalDate(departureMillis)
            val returnDate = DateUtils().convertMillisToLocalDate(returnMillis)
            val days = ChronoUnit.DAYS.between(departureDate, returnDate) + 1
            val nights = if (days > 1) days - 1 else 0
            return if (days < 2) "1 DAY TRIP"
                    else if (days < 0) "invalid field"
                    else "$days DAYS $nights NIGHTS"
        }
        return "invalid field"
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        handleImageUpload(context, uri)
    }

    val isImageUploaded = uploadedImageUri != null

    val tripLength = remember {
        mutableStateOf(calculateTripLength(tripPackageDeptDate.selectedDateMillis, tripPackageRetDate.selectedDateMillis))
    }

    var tripPackageNameError by remember { mutableStateOf(false) }

    var tripPackageFeesError by remember { mutableStateOf(false) }

    var numberOfPaxError by remember { mutableStateOf(false) }

    var tripPackageDepositError by remember { mutableStateOf(false) }

    var tripPackageDescError by remember { mutableStateOf(false) }

    // Validation functions
    fun validateInputs(): Boolean {
        var isValid = true

        // Trip Image validation
        if (uploadedImageUri == null) {
            isValid = false
            Toast.makeText(context, "Please upload trip image", Toast.LENGTH_SHORT).show()
        }

        // Trip Length validation
        if (tripLength.value.isEmpty() || (tripLength.value == "OTHERS" && customTripLength.value.text.isEmpty())) {
            isValid = false
            Toast.makeText(context, "Please enter valid trip length", Toast.LENGTH_SHORT).show()
        }

        // Trip Name validation
        if (tripPackageName.value.text.isEmpty()) {
            isValid = false
            tripPackageNameError = true
            Toast.makeText(context, "Please enter trip name", Toast.LENGTH_SHORT).show()
        } else {
            tripPackageNameError = false
        }

        // Trip Fees validation
        if (tripPackageFees.value.text.isEmpty() || tripPackageFees.value.text.toDoubleOrNull() == null || tripPackageFees.value.text.toDouble() < 1) {
            isValid = false
            tripPackageFeesError = true
            Toast.makeText(context, "Please enter valid trip fees (minimum 1)", Toast.LENGTH_SHORT).show()
        } else {
            tripPackageFeesError = false
        }

        // Trip Deposit validation
        if (tripPackageDeposit.value.text.isEmpty() || tripPackageDeposit.value.text.toDoubleOrNull() == null || tripPackageDeposit.value.text.toDouble() < 1) {
            isValid = false
            tripPackageDepositError = true
            Toast.makeText(context, "Please enter valid trip deposit (minimum 1 and no decimal points allowed)", Toast.LENGTH_SHORT).show()
        } else {
            tripPackageDepositError = false
        }

        // Description validation
        if (tripPackageDesc.value.text.isEmpty()) {
            isValid = false
            tripPackageDescError = true
            Toast.makeText(context, "Please enter trip description", Toast.LENGTH_SHORT).show()
        } else {
            tripPackageDescError = false
        }

        // Available validation
        if (tripAvailable.value.text.isEmpty() || tripAvailable.value.text.toIntOrNull() == null || tripAvailable.value.text.toInt() < 1) {
            isValid = false
            numberOfPaxError  = true
            Toast.makeText(context, "Please enter valid number (minimum 1)", Toast.LENGTH_SHORT).show()
        } else {
            numberOfPaxError = false
        }

        // Departure Date validation
        if (deptDateMillisToLocalDate == null) {
            isValid = false
            Toast.makeText(context, "Please select valid departure date", Toast.LENGTH_SHORT).show()
        }
        else if (deptDateMillisToLocalDate.isBefore(currentDate)) {
            isValid = false
            Toast.makeText(context, "Cannot select date earlier then TODAY", Toast.LENGTH_SHORT).show()
        }

        // Return Date validation
        if (retDateMillisToLocalDate == null ) {
            isValid = false
            Toast.makeText(context, "Please select valid return date", Toast.LENGTH_SHORT).show()
        } else if (retDateMillisToLocalDate.isBefore(currentDate)) {
            isValid = false
            Toast.makeText(context, "Cannot select date earlier then TODAY", Toast.LENGTH_SHORT).show()
        }
        else if (deptDateMillisToLocalDate != null && retDateMillisToLocalDate != null) {
            if (retDateMillisToLocalDate < deptDateMillisToLocalDate) {
                isValid = false
                Toast.makeText(context, "Return date cannot be earlier than departure date", Toast.LENGTH_SHORT).show()
            }
        }

        // Options validation
        if (selectedOption.isEmpty()) {
            isValid = false
            Toast.makeText(context, "Please select at least one option", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }

    fun showConfirmationDialog() {
        showConfirmDialog = true
    }

    fun handleSaveButtonClick() {
        if (validateInputs()) {
            showConfirmationDialog()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var dbHandler: DBHandler = DBHandler(context)

        val title = "Add New Travel Package - [ ${tripPackageName.value.text} ]"
        ReuseComponents.TopBar(
            title = title,
            navController,
            showBackButton = true,
            showLogoutButton = true,
            isAtSettingPage = true,
            isAgencySide = true,
            onLogout = {
                navController.navigate(route = Screen.UserOrAdmin.route) {
                    popUpTo(Screen.UserOrAdmin.route) {
                        inclusive = true
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { pickImage(imagePicker) }
                .background(Color.Gray)
        ) {
            if (isImageUploaded) {
                Image(
                    painter = rememberAsyncImagePainter(uploadedImageUri),
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
                Image(
                    painter = painterResource(R.drawable.uploadimg),
                    contentDescription = "Upload Photo Placeholder",
                    modifier = Modifier
                        .fillMaxWidth()
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
                    Text(text = "Trip Name", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripPackageName.value,
                        onValueChange = { tripPackageName.value = it },
                        label = { Text(text = "Enter the trip location", color = Color.Black.copy(alpha = 0.32f), fontWeight = FontWeight.Light) },
                        singleLine = true,
                        isError = tripPackageNameError,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                    if (tripPackageNameError) {
                        Text(
                            text = "invalid field",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Trip Fees", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripPackageFees.value,
                        onValueChange = { tripPackageFees.value = it },
                        label = { Text(text = "MYR", color = Color.Black.copy(alpha = 0.32f), fontWeight = FontWeight.Light) },
                        singleLine = true,
                        isError = tripPackageFeesError,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    if (tripPackageFeesError) {
                        Text(
                            text = "invalid field",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Deposit", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripPackageDeposit.value,
                        onValueChange = { tripPackageDeposit.value = it },
                        label = { Text(text = "MYR", color = Color.Black.copy(alpha = 0.32f), fontWeight = FontWeight.Light) },
                        singleLine = true,
                        isError = tripPackageDepositError,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    if (tripPackageDepositError) {
                        Text(
                            text = "invalid field",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Description", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripPackageDesc.value,
                        onValueChange = { tripPackageDesc.value = it },
                        label = { Text(text = "Enter the trip description", color = Color.Black.copy(alpha = 0.32f), fontWeight = FontWeight.Light) },
                        singleLine = true,
                        isError = tripPackageDescError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    )
                    if (tripPackageDescError) {
                        Text(
                            text = "invalid field",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Number Of Pax Available", fontWeight = FontWeight.Bold)
                    TextField(
                        value = tripAvailable.value,
                        onValueChange = { tripAvailable.value = it },
                        label = { Text(text = "No. of PAX", color = Color.Black.copy(alpha = 0.32f), fontWeight = FontWeight.Light) },
                        singleLine = true,
                        isError = numberOfPaxError,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    if (numberOfPaxError) {
                        Text(
                            text = "invalid field",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Departure Date", fontWeight = FontWeight.Bold)
                    Box {
                        TextField(
                            value = deptDateToString,
                            onValueChange = {},
                            readOnly = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { showDeptDateDialog = true }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.DateRange,
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
                                    onClick = {
                                        showDeptDateDialog = false
                                        tripLength.value = calculateTripLength(
                                            tripPackageDeptDate.selectedDateMillis,
                                            tripPackageRetDate.selectedDateMillis
                                        )
                                    }
                                ) {
                                    Text(text = "Confirm")
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

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Return Date", fontWeight = FontWeight.Bold)
                    Box {
                        TextField(
                            value = retDateToString,
                            onValueChange = {},
                            readOnly = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { showRetDateDialog = true }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.DateRange,
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
                                    onClick = {
                                        showRetDateDialog = false
                                        tripLength.value = calculateTripLength(
                                            tripPackageDeptDate.selectedDateMillis,
                                            tripPackageRetDate.selectedDateMillis
                                        )}
                                ) {
                                    Text(text = "Confirm")
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
                    Text(text = "Trip Length", fontWeight = FontWeight.Bold)
                    Box {
                        TextField(
                            value = calculateTripLength(
                                tripPackageDeptDate.selectedDateMillis,
                                tripPackageRetDate.selectedDateMillis
                            ),
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp)
                        )
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
                        onClick = { handleSaveButtonClick() }
                    )
                }

                item {
                    // Confirmation Dialog
                    if (showConfirmDialog) {
                        AlertDialog(
                            onDismissRequest = { showConfirmDialog = false },
                            title = {
                                Text("Confirm Save")
                            },
                            text = {
                                Text("Are you sure you want to save this trip package?")
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        tripViewModel.addTrip(
                                            context = context,
                                            db = db,
                                            tripId = tripId,
                                            tripPackageName = tripPackageName.value.text.uppercase(),
                                            tripLength = tripLength.value,
                                            tripPackageFees = tripPackageFees.value.text.toDouble(),
                                            tripPackageDeposit = tripPackageDeposit.value.text.toDouble(),
                                            tripPackageDesc = tripPackageDesc.value.text.uppercase(),
                                            tripPackageDeptDate = deptDateToString,
                                            tripPackageRetDate = retDateToString,
                                            uploadedImageUri = uploadedImageUri?.toString(),
                                            selectedOption = selectedOption,
                                            isAvailable = tripAvailable.value.text.toInt(),
                                            noOfUserBooked = 0,
                                            agencyUsername = loggedInAgency?.agencyUsername ?: "user",
                                            agencyId = loggedInAgency?.agencyId ?: "agencyId",
                                            onSuccess = {
                                                dbHandler.addNewTrip(
                                                    tripId,
                                                    tripPackageName.value.text.uppercase(),
                                                    tripLength.value,
                                                    tripPackageFees.value.text.toDouble(),
                                                    tripPackageDeposit.value.text.toDouble(),
                                                    tripPackageDesc.value.text.uppercase(),
                                                    deptDateToString,
                                                    retDateToString,
                                                    uploadedImageUri?.toString(),
                                                    selectedOption,
                                                    tripAvailable.value.text.toInt(),
                                                    0,
                                                    loggedInAgency?.agencyUsername ?: ""
                                                )
                                            },
                                        )
                                        Toast.makeText(
                                            context,
                                            "Trip Added to Database",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        showConfirmDialog = false
                                        navController.popBackStack()
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("Save")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showConfirmDialog = false },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                }
            }
            }
            // Add a SnackbarHost at the end of your UI hierarchy
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                // Snackbar with action to dismiss
                Snackbar(
                    action = {
                        IconButton(
                            onClick = { snackbarHostState.currentSnackbarData?.dismiss() }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                ) {
                    Text(
                        text = "All fields must not be empty"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAgencyAddPackageScreen() {
    AgencyAddPackageScreen(navController = rememberNavController(), context = LocalContext.current, viewModel = AgencyViewModel(), tripViewModel = TripViewModel())
}