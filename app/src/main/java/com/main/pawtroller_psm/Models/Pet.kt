package com.main.pawtroller_psm.Models

data class Pet(
    val id: String,
    val name: String,
    val nickname:String,
    val birthdate: String,
    val age: String,
    val status_id: String,
    val user_id: String,
    val type_id: String,
    val updated_at: String,
    val created_at: String,
    val description: String,
    val img_path: String

)

data class TipoMascota(
    val id: String,
    val name:String,
    val description: String,
    val created_at: String,
    val updated_at: String
)

data class RegistrarPet(
    val userid: String,
    val name: String,
    val nickname: String,
    val description: String,
    val birthdate: String,
    val age: String,
    val pettype: String,
    val petimg:String
)