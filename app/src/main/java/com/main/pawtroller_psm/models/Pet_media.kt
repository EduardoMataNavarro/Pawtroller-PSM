package com.main.pawtroller_psm.models

data class Pet_media(
    val id: String,
    val type: String,
    val path: String,
    val pet_id: String,
    val created_at: String,
    val updated_at : String
)

data class ResponsePetMedia(
    val message: String,
    val pet_media:Pet_media
)