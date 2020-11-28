package com.main.pawtroller_psm

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RestEngine {

    companion object{

        fun getRestEngine(): Retrofit {
            //Creamos el interceptor
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client =  OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit =  Retrofit.Builder()
                .baseUrl("https://pawtroller-webapi.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return  retrofit

        }
    }
}