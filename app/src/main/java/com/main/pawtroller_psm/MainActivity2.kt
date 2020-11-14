package com.main.pawtroller_psm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.hide()
        iniciarSesion()
    }


    fun iniciarSesion(){
        text_identificador.setOnClickListener(){
            val iMainActivity= Intent(applicationContext,MainActivity::class.java)
            startActivity(iMainActivity)
        }
    }
}