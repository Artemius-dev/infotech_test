package com.example.myapplication.network.response

data class ButtonToActionItem(
    val cool_down: Int,
    val enabled: Boolean,
    val priority: Int,
    val type: String,
    val valid_days: List<Int>
)