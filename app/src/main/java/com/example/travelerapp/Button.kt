import android.graphics.Paint.Style
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.navigation.NavController
import com.example.travelerapp.R
import com.example.travelerapp.Screen
import com.example.travelerapp.ui.theme.CusFont1
import java.util.Calendar


object ReuseComponents {

    enum class ButtonState { Pressed, Idle }
    fun Modifier.bounceClick() = composed {
        var buttonState by remember { mutableStateOf(ButtonState.Idle) }
        val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.70f else 1f)

        this
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {  }
            )
            .pointerInput(buttonState) {
                awaitPointerEventScope {
                    buttonState = if (buttonState == ButtonState.Pressed) {
                        waitForUpOrCancellation()
                        ButtonState.Idle
                    } else {
                        awaitFirstDown(false)
                        ButtonState.Pressed
                    }
                }
            }
    }

    @Composable
    fun CustomButton(text: String, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                Color(0xFF5DB075)
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .bounceClick()
        ) {
            Text(text, style = TextStyle(fontFamily = CusFont1))
        }
    }

    fun MutableState<String>.getValueAsString(): String {
        return this.value
    }

    @Composable
    fun RoundedOutlinedTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        label: @Composable (String) -> Unit,
        shape: Shape = RoundedCornerShape(8.dp), // Default rounded corner shape
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            shape = shape,
            singleLine = true,
            modifier = modifier.padding(4.dp)
        )
    }

    @Composable
    fun NavBar(text: String, navController: NavController) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 56.dp, vertical = 16.dp)
            ) {
                val homeColor = if (text == "Home") Color.Blue else MaterialTheme.colorScheme.onBackground
                val packageColor = if (text == "Travel Package") Color.Blue else MaterialTheme.colorScheme.onBackground
                val walletColor = if (text == "Wallet") Color.Blue else MaterialTheme.colorScheme.onBackground
                val reviewColor = if (text == "Review") Color.Blue else MaterialTheme.colorScheme.onBackground

                Icon(
                    painter = painterResource(R.drawable.home),
                    contentDescription = "Home",
                    tint = homeColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            navController.navigate(route = Screen.Home.route){
                                popUpTo(Screen.Home.route) {
                                    inclusive = false
                                }
                            }
                        }
                )
                Icon(
                    painter = painterResource(R.drawable.resource_package),
                    contentDescription = "Travel Package",
                    tint = packageColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            navController.navigate(route = Screen.UserDisplayPackageList.route){
                                popUpTo(Screen.UserDisplayPackageList.route) {
                                    inclusive = false
                                }
                            }
                        }
                )
                Icon(
                    painter = painterResource(R.drawable.wallet),
                    contentDescription = "Wallet",
                    tint = walletColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            navController.navigate(route = Screen.Wallet.route) {
                                popUpTo(Screen.Wallet.route) {
                                    inclusive = false
                                }
                            }
                        }
                )
                Icon(
                    painter = painterResource(R.drawable.map),
                    contentDescription = "Trip",
                    tint = reviewColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            navController.navigate(route = Screen.Review.route) {
                                popUpTo(Screen.Review.route) {
                                    inclusive = false
                                }
                            }
                        }
                )
            }
        }
    }

    @Composable
    fun TopBar(
        title: String,
        navController: NavController,
        showBackButton: Boolean = false,
        showLogoutButton: Boolean = false,
        isAtSettingPage: Boolean = false,
        isAgencySide: Boolean = false, // New parameter to indicate if on agency side
        onLogout: (() -> Unit)? = null
    ) {
        var expanded by remember { mutableStateOf(false) }

        val items = mutableListOf("Settings")

        if (showLogoutButton) {
            items.add("Logout")
        }

        Surface(
            color = MaterialTheme.colorScheme.background,
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
                            painter = painterResource(R.drawable.back_button),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.ExtraBold
                )
                if (!isAtSettingPage) {
                    IconButton(
                        onClick = {
                            if (isAgencySide) {
                                navController.navigate(route = Screen.AgencySetting.route)
                            } else {
                                navController.navigate(route = Screen.Settings.route)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Box {
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background),
                        ) {
                            items.forEach {
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    // Depending on the option selected, perform some action
                                    when (it) {
                                        "Settings" -> {
                                            if (isAgencySide) {
                                                navController.navigate(route = Screen.AgencySetting.route)
                                            } else {
                                                navController.navigate(route = Screen.Settings.route)
                                            }
                                        }

                                        "Logout" -> {
                                            onLogout?.invoke()
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


    @Composable
    fun AgencyNavBar(text: String, navController: NavController) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 100.dp, vertical = 16.dp)
            ) {
                val agencyHomeColor = if (text == "AgencyHome") Color.Blue else Color.Black
                val agencyPackageColor = if (text == "AgencyPackage") Color.Blue else Color.Black

                Icon(
                    painter = painterResource(R.drawable.home),
                    contentDescription = "Home",
                    tint = agencyHomeColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            navController.navigate(route = Screen.AgencyHome.route)
                        }
                )
                Icon(
                    painter = painterResource(R.drawable.map),
                    contentDescription = "Package",
                    tint = agencyPackageColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            navController.navigate(route = Screen.AgencyPackageList.route)
                        }
                )
            }
        }
    }

    @Composable
    fun RoundImg(
        modifier: Modifier = Modifier,
        painter: Painter,
        contentDescription: String?
    ){
        Image(
            modifier = Modifier
                .fillMaxHeight()
//                .height(50.dp)
//                .padding(8.dp)
                .clip(CircleShape),
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            )
    }

    @Composable
    fun DatePicker() {
        val datesList = listOf<String>("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        var dayCounter: Int = 1
        var week: Int = 1
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                datesList.forEach {
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it.substring(0, 3),
                            style = TextStyle(MaterialTheme.colorScheme.onSurface, fontFamily = CusFont1))
                    }
                }
            }
            var initWeekday = 3 // wednesday
            while (dayCounter <= 31) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                ) {
                    if (initWeekday > 0) {
                        repeat(initWeekday) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    for (i in week..(7 - initWeekday)) {
                        if (dayCounter <= 31) {
                            val isToday = dayCounter == currentDay
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isToday) Color(0xFF43b3fb) else MaterialTheme.colorScheme.background,
                                        CircleShape
                                    )
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = dayCounter++.toString(),
                                    style = TextStyle(Color.Gray,fontFamily = CusFont1)
                                    )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    initWeekday = 0
                }
            }
        }
    }

}