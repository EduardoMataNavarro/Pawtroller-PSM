package com.main.pawtroller_psm

import com.main.pawtroller_psm.Models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Service {

    @POST("api/loginuser")
    fun login(@Body userLogin: UserLogin): Call<ResponseLogin>

    @POST("api/signupuser")
    fun registrar(@Body userResgister: UserRegister): Call <List<User>>

    @GET("api/pet_type")
    fun obtenerMascotas(): Call<List<List<TipoMascota>>>

    @POST("api/pet/create")
    fun agregarMascotas(@Body crearPet: RegistrarPet): Call<List<Pet>>
}