package com.main.pawtroller_psm.Models

import java.util.*

data class Pet(
    val id: String,
    val name: String,
    val nickname:String,
    val birthdate: Date,
    val age: Int,
    val status: Int,
    val owner_id: Int,
    val type_is: Int

)
