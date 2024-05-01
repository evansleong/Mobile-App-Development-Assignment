package com.example.travelerapp

import ReuseComponents
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.util.appendPlaceholders
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.travelerapp.data.Review
import com.example.travelerapp.data.Trip

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditReviewScreen(
    navController: NavController,
    id: String,
    context: Context
) {
    var dbHandler: DBHandler = DBHandler(context)
    val review = dbHandler.getReviewById(id.toInt())

    val maxWords = 30
    val maxImages = 9
    val title = review?.title
    var reviewTitle by remember { mutableStateOf(title) }
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ReuseComponents.TopBar(title = title.toString(), navController, showBackButton = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ){
            TextField(
                value = reviewTitle?: "",
                onValueChange = {
                    // Limit the input to maxWords words
                    if (it.count { c -> c == ' ' } < maxWords) {
                        reviewTitle = it
                    }
                },
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                ),
                placeholder = { Text(text = "Enter Some Attractive Title Here") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Max $maxWords words",
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Rating: ")
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                StarRatingInput(onRatingChanged = { newRating ->
                    rating = newRating
                })
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Share more about your experience: ",
                modifier = Modifier.padding(vertical = 4.dp)
            )
            TextField(
                value = comment,
                onValueChange = { comment = it },
                colors = TextFieldDefaults.colors(
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                placeholder = { Text(text = "Share details of your own experience at this place") },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(unbounded = true)
                    .heightIn(max = 200.dp, min = 60.dp)
                    .padding(vertical = 4.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            ImageInputButton(context = LocalContext.current, maxImages) { uri ->
                selectedImages = selectedImages + uri
                //selectedImages store all Images Inputted
            }
            Text(
                text = "Max $maxImages images",
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "Make it public",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable(onClick = { isChecked = !isChecked })
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = Color(0xFF1B6DF3)
                        ),
                        shape = RoundedCornerShape(32.dp),
                        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Post",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageInputButton(
    context: Context,
    maxImages: Int = 9,
    onImageSelected: (Uri) -> Unit
) {
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                if (imageUris.size < maxImages) {
                    imageUris = imageUris + it
                    onImageSelected(it) // Call the callback function with the selected URI
                } else {
                    Toast.makeText(context, "Maximum images reached", Toast.LENGTH_SHORT).show()
                }
            }
        }

    fun pickImage() {
        imagePicker.launch("image/*")
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(imageUris) { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp) // Set square size for each image
                    .padding(8.dp)
            )
        }

        item {
            Box(
                modifier = Modifier
                    .size(100.dp) // Set the same size as the images
                    .background(Color.Gray)
                    .clickable {
                        if (imageUris.size < maxImages) {
                            pickImage()
                        } else {
                            Toast
                                .makeText(context, "Maximum images reached", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
fun StarRatingInput(onRatingChanged: (Int) -> Unit) {
    var selectedRating by remember { mutableStateOf(0) }

    Row(verticalAlignment = Alignment.CenterVertically){
        for(i in 1..5){
            StarButton(
                isSelected = i <= selectedRating,
                onClick = { selectedRating = i },
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }

    LaunchedEffect(selectedRating) {
        onRatingChanged(selectedRating)
    }
}

@Composable
fun StarButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val starIcon = if(isSelected) Icons.Filled.Star else Icons.Outlined.Star
    val starColor = if(isSelected) Color(0xFFF9BB04) else Color.Gray

    IconButton(
        onClick = onClick,
        modifier = Modifier.size(60.dp)
    ) {
        Icon(
            imageVector = starIcon,
            contentDescription = null,
            tint = starColor,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun EditReviewScreenPreview() {
    EditReviewScreen(
        navController = rememberNavController(),
        id = "999",
        context = LocalContext.current
    )
}