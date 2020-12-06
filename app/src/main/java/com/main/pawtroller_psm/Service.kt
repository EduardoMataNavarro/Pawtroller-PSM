package com.main.pawtroller_psm

import com.main.pawtroller_psm.Models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {

    @POST("api/loginuser")
    fun login(@Body userLogin: UserLogin): Call<ResponseLogin>

    @POST("api/signupuser")
    fun registrar(@Body userResgister: UserRegister): Call <List<User>>

    @GET("api/pet_type")
    fun obtenerMascotas(): Call<List<List<TipoMascota>>>

    @Multipart
    @POST("api/pet/create")
    fun agregarMascotas(@Part petImg: MultipartBody.Part, @Part ("name") name: RequestBody, @Part ("nickname") nickname: RequestBody, @Part ("description") description: RequestBody,
                        @Part ("userid") userId: RequestBody,@Part ("birthdate") birthdate: RequestBody,@Part ("age") age: RequestBody,
                        @Part ("pettype") pettype: RequestBody): Call<List<Pet>>

    @GET("api/pet/user/{id}")
    fun consultaMascotasPorUsuario(@Path("id")id: Int): Call<List<List<Pet>>>

    @GET("api/pet/{id}/media")
    fun consultaImagenPorMascota(@Path("id")id: Int): Call<List<List<Pet_media>>>

}