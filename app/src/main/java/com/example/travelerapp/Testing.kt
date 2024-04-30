//package com.example.travelerapp
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.requiredHeight
//import androidx.compose.foundation.layout.requiredWidth
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.BlurredEdgeTreatment
//import androidx.compose.ui.draw.blur
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.graphics.Shadow
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun Testing(modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .requiredWidth(width = 390.dp)
//            .requiredHeight(height = 844.dp)
//            .background(color = Color.White)
//    ) {
//        Darkmodefalse()
//        Text(
//            text = "Select User Type",
//            color = Color.Black,
//            style = TextStyle(
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                shadow = Shadow(color = Color.Black.copy(alpha = 0.25f), offset = Offset(0f, 4f), blurRadius = 4f)
//            ),
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 113.dp,
//                    y = 138.dp)
//                .requiredWidth(width = 327.dp)
//                .requiredHeight(height = 175.dp))
//        Box(
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 145.dp,
//                    y = 583.dp)
//                .requiredWidth(width = 101.dp)
//                .requiredHeight(height = 51.dp)
//        ) {
//            ButtonPrimary()
//        }
//        Box(
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 49.dp,
//                    y = 300.dp)
//                .requiredWidth(width = 120.dp)
//                .requiredHeight(height = 150.dp)
//                .blur(radius = 4.dp,
//                    edgeTreatment = BlurredEdgeTreatment.Unbounded)
//                .background(color = Color.White)
//                .border(border = BorderStroke(1.dp, Color.Black)))
//        Box(
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 208.dp,
//                    y = 300.dp)
//                .requiredWidth(width = 120.dp)
//                .requiredHeight(height = 150.dp)
//                .blur(radius = 4.dp,
//                    edgeTreatment = BlurredEdgeTreatment.Unbounded)
//                .background(color = Color.White)
//                .border(border = BorderStroke(1.dp, Color(0xff0047ff))))
//        Icon(
//            painter = painterResource(id = R.drawable.invoker),
//            contentDescription = "24/User Interface/User",
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 246.dp,
//                    y = 330.dp))
//        Icon(
//            painter = painterResource(id = R.drawable.invoker),
//            contentDescription = "24/User Interface/User",
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 85.dp,
//                    y = 330.dp))
//        Text(
//            text = "User",
//            color = Color.Black,
//            style = TextStyle(
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                shadow = Shadow(color = Color.Black.copy(alpha = 0.25f), offset = Offset(0f, 4f), blurRadius = 4f)),
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 85.dp,
//                    y = 395.dp)
//                .requiredWidth(width = 327.dp)
//                .requiredHeight(height = 175.dp))
//        Text(
//            text = "Travel\n",
//            color = Color.Black,
//            style = TextStyle(
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                shadow = Shadow(color = Color.Black.copy(alpha = 0.25f), offset = Offset(0f, 4f), blurRadius = 4f)),
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 241.dp,
//                    y = 389.dp)
//                .requiredWidth(width = 327.dp)
//                .requiredHeight(height = 175.dp))
//        Text(
//            text = "Agency",
//            color = Color.Black,
//            style = TextStyle(
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                shadow = Shadow(color = Color.Black.copy(alpha = 0.25f), offset = Offset(0f, 4f), blurRadius = 4f)),
//            modifier = Modifier
//                .align(alignment = Alignment.TopStart)
//                .offset(x = 236.dp,
//                    y = 408.dp)
//                .requiredWidth(width = 327.dp)
//                .requiredHeight(height = 175.dp))
//    }
//}
//
//@Composable
//fun Darkmodefalse(modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .requiredHeight(height = 29.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(color = Color.White))
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.invoker),
//                contentDescription = "Cellular",
//                modifier = Modifier
//                    .requiredWidth(width = 18.dp)
//                    .requiredHeight(height = 12.dp))
//            Image(
//                painter = painterResource(id = R.drawable.invoker),
//                contentDescription = "Wifi",
//                modifier = Modifier
//                    .requiredWidth(width = 16.dp)
//                    .requiredHeight(height = 12.dp))
//            Image(
//                painter = painterResource(id = R.drawable.invoker),
//                contentDescription = "Battery",
//                colorFilter = ColorFilter.tint(Color(0xff170e2b).copy(alpha = 0.4f)),
//                modifier = Modifier
//                    .requiredWidth(width = 24.dp)
//                    .requiredHeight(height = 12.dp))
//        }
//        Text(
//            text = "12:30",
//            color = Color(0xff170e2b),
//            style = TextStyle(
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Medium),
//            modifier = Modifier
//                .fillMaxSize())
//    }
//}
//
//@Composable
//fun ButtonPrimary(modifier: Modifier = Modifier) {
//    Column(
//        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = modifier
//            .background(color = Color(0xff5db075))
//            .padding(horizontal = 32.dp,
//                vertical = 16.dp)
//    ) {
//        Text(
//            text = "Next",
//            color = Color.White,
//            textAlign = TextAlign.Center,
//            style = TextStyle(
//                fontSize = 16.sp))
//    }
//}
//
//@Preview(widthDp = 390, heightDp = 844)
//@Composable
//private fun TestingPreview() {
//    Testing(Modifier)
//}