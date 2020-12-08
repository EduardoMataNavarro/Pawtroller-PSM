package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.main.pawtroller_psm.Models.EstatusPet
import com.main.pawtroller_psm.Models.ResponseEstatusPet
import kotlinx.android.synthetic.main.activity_cambiar_estatus.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var desccripcionTxt: TextView ? = null
    var fechaTxt: TextView ?= null

    var estatusPet: EstatusPet ?= null
    var petid: String ?= null
    var estatus: String ?= null

class CambiarEstatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_estatus)

        supportActionBar?.hide()

        val datos: Intent = intent
        petid = datos.getStringExtra("petid")
        estatus = datos.getStringExtra("estatus")

        desccripcionTxt = findViewById(R.id.descestatusPet)
        fechaTxt = findViewById(R.id.fechaEstatusPet)



        fabCerrarVentana3.setOnClickListener(){
            finish()
        }

        btnAgregarStatus.setOnClickListener(){
            agregarStatus()
        }
    }

    private fun agregarStatus() {

            obtenerDatosEstatusPet()
            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<ResponseEstatusPet> = service.cambiarStatus(estatusPet!!)

            result.enqueue(object : Callback<ResponseEstatusPet> {
                override fun onResponse(
                    call: Call<ResponseEstatusPet>,
                    response: Response<ResponseEstatusPet>
                ) {
                    val respuesta = response.body()
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CambiarEstatusActivity,
                            "Estatus actualizado con éxito",
                            Toast.LENGTH_LONG
                        ).show()
                        val iMainActivity = Intent(applicationContext, MainActivity::class.java)
                        startActivity(iMainActivity)
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                this@CambiarEstatusActivity,
                                jObjError.getJSONObject("errors").toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@CambiarEstatusActivity,
                                e.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseEstatusPet>, t: Throwable) {
                    Toast.makeText(this@CambiarEstatusActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun obtenerDatosEstatusPet() {
        val descripcion = desccripcionTxt!!.text.toString()
        val fecha = fechaEstatusPet!!.text.toString()
        estatusPet = EstatusPet(petid!!,estatus!!,descripcion,fecha)
    }
}
