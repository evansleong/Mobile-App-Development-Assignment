import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.travelerapp.DBHandler
import com.example.travelerapp.Screen
import com.example.travelerapp.updateWalletPIN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AddPINScreen(
    navController: NavController,
    context: Context
) {
    val db = Firebase.firestore
    var pin1 by remember { mutableStateOf("") }
    var pin2 by remember { mutableStateOf("") }

    var dbHandler: DBHandler = DBHandler(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set Your Wallet PIN",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Enter Your Wallet PIN: ",
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = pin1,
                onValueChange = {
                    if (it.length <= 6) {
                        pin1 = it
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                modifier = Modifier.padding(4.dp).fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Re-enter your Wallet PIN:",
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = pin2,
                onValueChange = {
                    if (it.length <= 6) {
                        pin2 = it
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                modifier = Modifier.padding(4.dp).fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(4.dp))

            ReuseComponents.CustomButton(
                text = "Confirm",
                onClick = {
                    if (pin1.isNotEmpty() && pin2.isNotEmpty()) {
                        if (pin1 == pin2) {
                            updateWalletPIN(db, context, pin2)
                            Toast.makeText(context, "Welcome to Traveller Apps", Toast.LENGTH_SHORT)
                                .show()
//                            dbHandler.setWalletPIN(pin2.toInt())
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) {
                                    inclusive = false
                                }
                            }
                        } else {
                            Toast.makeText(context, "PINs do not match", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "PIN field cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AddPINScreenPreview(){
    AddPINScreen(
        navController = rememberNavController(),
        context = LocalContext.current
    )
}
