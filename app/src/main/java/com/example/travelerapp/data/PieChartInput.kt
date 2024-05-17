package com.example.travelerapp.data

import androidx.compose.ui.graphics.Color

data class PieChartInput(
    val color: Color,
    val value:Int,
    val description:String,
    val isTapped:Boolean = false
)
