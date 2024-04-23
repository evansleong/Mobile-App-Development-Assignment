import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.navigation.NavController
import com.example.travelerapp.R
import com.example.travelerapp.Screen

object ReuseComponents {

    @Composable
    fun CustomButton(text: String, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text)
        }
    }

    @Composable
    fun RoundedOutlinedTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        label: @Composable (String) -> Unit,
        shape: Shape = RoundedCornerShape(8.dp), // Default rounded corner shape
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = shape,
            modifier = modifier.padding(4.dp)
        )
    }

    @Composable
    fun NavBar(text: String, navController: NavController) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 56.dp, vertical = 16.dp)
            ) {
                val homeColor = if (text == "Home") Color.Blue else Color.Black
                val packageColor = if (text == "Package") Color.Blue else Color.Black
                val walletColor = if (text == "Wallet") Color.Blue else Color.Black
                val tripColor = if (text == "Trip") Color.Blue else Color.Black

                Icon(
                    painter = painterResource(R.drawable.home),
                    contentDescription = "Home",
                    tint = homeColor,
                    modifier = Modifier.clickable {
                        navController.navigate(route = Screen.Home.route)
                    }
                )
                Icon(
                    painter = painterResource(R.drawable.resource_package),
                    contentDescription = "Package",
                    tint = packageColor,
                    modifier = Modifier.clickable {
                        navController.navigate(route = Screen.Package.route)
                    }
                )
                Icon(
                    painter = painterResource(R.drawable.wallet),
                    contentDescription = "Wallet",
                    tint = walletColor,
                    modifier = Modifier.clickable {
                        navController.navigate(route = Screen.Wallet.route)
                    }
                )
                Icon(
                    painter = painterResource(R.drawable.map),
                    contentDescription = "Trip",
                    tint = tripColor,
                    modifier = Modifier.clickable {
                        navController.navigate(route = Screen.Trip.route)
                    }
                )
            }
        }
    }

    @Composable
    fun TopBar(title: String, navController: NavController, showBackButton : Boolean = false) {
        var expanded by remember { mutableStateOf(false) }

        val items = listOf("Profile", "Settings")

        Surface(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (showBackButton) {
                    IconButton(
                        onClick = { navController.popBackStack() } // Navigate back to previous screen
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
                Text(
                    text = title,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notification",
                        tint = Color.Black
                    )
                }
                Box{
                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.Black
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White),
                    ) {
                        items.forEach {
                            DropdownMenuItem(onClick = {
                                expanded = false
                                // Depending on the option selected, perform some action
                                when (it) {
                                    "Profile" -> {
                                        navController.navigate(route = Screen.Profile.route)
                                    }
                                    "Settings" -> {
                                        navController.navigate(route = Screen.Settings.route)
                                    }
                                }
                            },
                                text = { Text(it) }
                            )
                        }
                    }
                }
            }
        }
    }

}