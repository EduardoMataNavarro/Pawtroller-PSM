package com.main.pawtroller_psm

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.main.pawtroller_psm.datos.DatosMascota
import com.main.pawtroller_psm.datos.DatosUsuario
import com.main.pawtroller_psm.models.RegistrarPet
import com.main.pawtroller_psm.models.User
import kotlinx.android.synthetic.main.fragment_crear_pet.*
import java.time.LocalDate
import java.time.Period
import java.util.*

class CrearPetActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    var datosUsuario = DatosUsuario()
    var datosMascota = DatosMascota()

    var day = 0
    var month = 0
    var year = 0
    var savedDay = "01"
    var savedMonth = "01"
    var savedYear = "1900"
    var desc: String = ""
    var pettype: String= ""

    var birthdatePetReg:TextView?=null
    private var option: Spinner?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_pet)
        supportActionBar?.hide()

        datosUsuario.obtenerDatosActivity(intent)
        datosMascota.obtenerDatosActivity(intent)

        birthdatePetReg = findViewById(R.id.bithdatePetReg)
        option = findViewById(R.id.spinnerRegPet)

        var options:MutableList<String> = mutableListOf()

        for (item in datosMascota.listaTipoMascota)
            options.add(item.name)

        val arrayAdapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_spinner_item,options)

        spinnerRegPet.adapter= arrayAdapter

        spinnerRegPet.onItemSelectedListener = object:

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for ( item in datosMascota.listaTipoMascota){
                    if(options[p2].equals(datosMascota.listaTipoMascota[p2].name)) {
                        desc = datosMascota.listaTipoMascota[p2].description
                        pettype = datosMascota.listaTipoMascota[p2].id
                        break
                    }
                }
                textView10.text = desc
                tipePetReg.text= pettype
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                desc=""
                pettype= ""
            }

        }

        abrirDatePicker()

        crearMascotaContinuacion(datosUsuario.user!!)

        fabCerrarVentana.setOnClickListener(){
            finish()
        }
    }

    private fun crearMascotaContinuacion(user:User) {
        btnCrearPet.setOnClickListener() {

            val registrarPet: RegistrarPet = obtenerDatosCrearPet(user)

            if (validarDatos(registrarPet)) {

                val iMainActivity = Intent(applicationContext, CrearPetActivity2::class.java)
                datosMascota.registrarPet= registrarPet
                datosMascota.enviarDatosActivity(iMainActivity)
                startActivity(iMainActivity)
                finish()
            }
        }
    }

    private fun validarDatos(registrarPet: RegistrarPet): Boolean {
        if("1900-01-01".equals(registrarPet.birthdate) || "".equals(registrarPet.name) || "".equals(registrarPet.nickname)
            || "".equals(registrarPet.pettype) || "".equals(registrarPet.description)) {
            Toast.makeText(this@CrearPetActivity, "Completa todos los campos" , Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun obtenerDatosCrearPet(user: User): RegistrarPet {

        val name: String = namePetReg!!.text.toString()
        val nickName: String = nicknamePetReg!!.text.toString()
        val descripcion: String = textInputReg4!!.text.toString()
        val birthdate = LocalDate.parse(savedYear + "-" +  savedMonth + "-" + savedDay)
        var period = Period.between(birthdate, LocalDate.now())
        var age: Int = period.years
        var pettype:String = tipePetReg!!.text.toString()
        val userid: String = user.id.toString()


        return RegistrarPet(userid,name,nickName,descripcion,birthdate.toString() ,age.toString(),pettype.toString(),"")
    }


    fun getDateCalendar(){
        val cal:Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    fun abrirDatePicker(){
      buttonFechaPetReg.setOnClickListener(){
          getDateCalendar()
          DatePickerDialog(this, this, year, month, day).show()
      }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if(dayOfMonth>9){
            savedDay = dayOfMonth.toString()
        }else{
            savedDay = "0" +  dayOfMonth.toString()
        }
        if(month>8) {
            savedMonth = (month + 1).toString()
        }else{
            savedMonth = "0" + (month+ 1).toString()
        }
        savedYear = year.toString()

        birthdatePetReg!!.text = "$savedDay/$savedMonth/$savedYear"
    }
}