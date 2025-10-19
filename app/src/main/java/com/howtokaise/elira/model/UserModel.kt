package com.howtokaise.elira.model

data class UserModel(
    val name : String = "",
    val email : String = "",
    val uid : String = "",
    val cartItems : Map<String,Long> = emptyMap()
)
