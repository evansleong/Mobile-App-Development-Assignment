package com.example.travelerapp

import ReuseComponents
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.data.Trip
import com.example.travelerapp.viewModel.TransactionViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.example.travelerapp.viewModel.WalletViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


@Composable
fun PaymentScreen(
    navController: NavController,
    context: Context,
    tripViewModel: TripViewModel,
    walletViewModel: WalletViewModel,
    transactionViewModel: TransactionViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = "Wallet"
        ReuseComponents.TopBar(title = title, navController)
        val wallet = walletViewModel.userWallet
        val focusRequester = remember { FocusRequester() }
        val db = Firebase.firestore
        val tripState = remember { mutableStateOf<Trip?>(null) }
        val deposit = remember { mutableStateOf(0.0) }
        val selectedPackage = tripViewModel.selectedTripId
        LaunchedEffect(selectedPackage) {
            tripViewModel.readSingleTrip(db, selectedPackage.toString()) { trip ->
                if (trip != null) {
                    tripState.value = trip
                    deposit.value = trip.tripDeposit
                }
            }
        }
        val amount = tripViewModel.numPax.toInt().times(deposit.value.toDouble())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.2f)
                .background(color = Color(0xFF5DB075))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Spacer(modifier = Modifier.padding(top = 240.dp))
                Column {
                    Text(
                        text = "Your Balance",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFFCFCFC),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "MYR${wallet?.available}",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 2.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .weight(0.8f)
                .padding(top = 32.dp)
                .padding(horizontal = 16.dp)
        ) {

            val showDialog = remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color(0xFFA0D3AF))
            ) {
                TextField(
                    value = amount.toString(),
                    onValueChange = {  },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .focusRequester(focusRequester)
                        .background(color = Color(0xFFE1E1E1)),
                    label = { Text(text = "Enter your Reload Amount", color = Color.Black.copy(alpha = 0.32f), fontWeight = FontWeight.Light) },
                    singleLine = true,
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE1E1E1),
                        unfocusedContainerColor = Color(0xFFE1E1E1),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                )
            }

            var description by remember { mutableStateOf("") }
            Column {
                Text(
                    text = "Description",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .focusRequester(focusRequester)
                        .background(color = Color(0xFFE1E1E1)),
                    label = { Text(text = "What's the transaction for?", color = Color.Black.copy(alpha = 0.32f), fontWeight = FontWeight.Light) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE1E1E1),
                        unfocusedContainerColor = Color(0xFFE1E1E1),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
            ) {
                Button(
                    onClick = { showDialog.value = true },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color(0xFFEDE9E9)
                    ),
                    shape = RoundedCornerShape(32.dp),
                    contentPadding = PaddingValues(vertical = 20.dp, horizontal = 60.dp),
                    modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Pay",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
                var wrongAttempts = 0
                if (showDialog.value) {
                    PinInputDialog(
                        onContinue = { pin ->
                            if (pin.length < 6) {
                                Toast.makeText(context, "Invalid PIN. PIN must be 6 digits.", Toast.LENGTH_SHORT).show()
                            } else {
                                if (walletViewModel.checkPin(pin)) {

                                    walletViewModel.updateBalance(db, context, amount.toString(), "Payment") {
                                        if (it) {
                                            Toast.makeText(context, "Successfully make Payment RM$amount", Toast.LENGTH_SHORT).show()
                                            showDialog.value = false
                                            wallet?.user_id?.let {
                                                transactionViewModel.createTx(db, context, "Payment", amount.toString(), description, user_id = it)
                                            }
                                            tripState.value?.let {
                                                tripViewModel.updateAvailable(db, it.isAvailable, tripViewModel.numPax.toInt(), it.tripId) {
                                                    if (it) {
                                                        Toast.makeText(context, "Successfully update isAvailable to Firebase", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        Toast.makeText(context, "Error updating isAvailable to Firebase", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                            navController.navigate(Screen.Wallet.route)
                                        }
                                    }
                                } else {
                                    wrongAttempts++
                                    val remainingAttempts = 5 - wrongAttempts
                                    if (remainingAttempts > 0) {
                                        Toast.makeText(context, "Invalid PIN. $remainingAttempts attempts remaining", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "You have exceeded the maximum number of attempts.", Toast.LENGTH_SHORT).show()
                                        showDialog.value = false
                                        navController.navigate(Screen.UserDisplayPackageList.route)
                                    }
                                }
                            }
                        },
                        onDismiss = {
                            // Reset wrong attempts counter when PinInputDialog is dismissed
                            wrongAttempts = 0
                            showDialog.value = false
                            navController.popBackStack()
                        }
                    )
                }
            }
        }

        ReuseComponents.NavBar(text = title, navController = navController)
    }
}

@Composable
@Preview
fun PaymentScreenPreview(){
    PaymentScreen(
        navController = rememberNavController(),
        context = LocalContext.current,
        tripViewModel = TripViewModel(),
        walletViewModel = WalletViewModel(),
        transactionViewModel = TransactionViewModel()
    )
}