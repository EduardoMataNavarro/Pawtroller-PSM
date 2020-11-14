package com.main.pawtroller_psm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(1000)
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        registro()

        iniciarApp()
    }

    fun registro(){
        text_registro.setOnClickListener(){
            val iMainActivity2= Intent(applicationContext,MainActivity2::class.java)
            startActivity(iMainActivity2)
        }
    }

    fun iniciarApp(){
        btn_login.setOnClickListener(){
            val iMainApp= Intent(applicationContext,MainApp::class.java)
            startActivity(iMainApp)
        }
    }

}