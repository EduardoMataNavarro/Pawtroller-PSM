package com.main.pawtroller_psm

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.RegistrarPet
import com.main.pawtroller_psm.Models.TipoMascota
import com.main.pawtroller_psm.Models.User
import kotlinx.android.synthetic.main.fragment_crear_pet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period
import java.util.*

class CrearPetActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener{

    var day = 0
    var month = 0
    var year = 0
    var savedDay = ""
    var savedMonth = ""
    var savedYear = ""
    var desc: String = ""
    var pettype: String= ""

    var birthdatePetReg:TextView?=null
    private var option: Spinner?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_pet)
        supportActionBar?.hide()

        val datos: Intent = intent
        var listaMascostaString = datos.getStringExtra("listaMascostaString")
        var gson = Gson()
        var listaTipoMascota:List<TipoMascota> = gson.fromJson(listaMascostaString, Array<TipoMascota>::class.java).toList() as ArrayList<TipoMascota>
        var userString = datos.getStringExtra("userString")
        var gson2 = Gson()
        var user: User = gson2.fromJson(userString,User::class.java)


        birthdatePetReg = findViewById(R.id.bithdatePetReg)
        option = findViewById(R.id.spinnerRegPet)

        var options:MutableList<String> = mutableListOf()

        for (item in listaTipoMascota)
            options.add(item.name)

        val arrayAdapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_spinner_item,options)

        spinnerRegPet.adapter= arrayAdapter

        spinnerRegPet.onItemSelectedListener = object:

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for ( item in listaTipoMascota){
                    if(options[p2].equals(listaTipoMascota[p2].name)) {
                        desc = listaTipoMascota[p2].description
                        pettype = listaTipoMascota[p2].id
                    }
                    break
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

        crearMascotaContinuacion(user)
    }

    private fun crearMascotaContinuacion(user:User) {
        btnCrearPet.setOnClickListener(){

            val registrarPet: RegistrarPet = obtenerDatosCrearPet(user)

            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<List<Pet>> = service.agregarMascotas(registrarPet)

            result.enqueue(object : Callback<List<Pet>> {
                override fun onResponse(call: Call<List<Pet>>,response: Response<List<Pet>>) {
                    val respuesta = response.body()
                    val id: String = respuesta!!.get(0).id.toString()
                    if (id != null) {
                        Toast.makeText(this@CrearPetActivity,"Pet registrada con exito",Toast.LENGTH_LONG).show()
                        val iMainActivity = Intent(applicationContext, CrearPetActivity2::class.java)
                        startActivity(iMainActivity)
                    } else {
                        Toast.makeText(
                            this@CrearPetActivity,
                            "Ocurrio un error al registrar a tu mascota",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                    Toast.makeText(this@CrearPetActivity, t.message, Toast.LENGTH_LONG).show()
                }

            })

        }
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

        return RegistrarPet(userid,name,nickName,descripcion,birthdate.toString() ,age.toString(),pettype.toString())
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