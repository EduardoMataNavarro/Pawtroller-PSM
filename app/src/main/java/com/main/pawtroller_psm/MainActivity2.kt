package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.main.pawtroller_psm.models.User
import com.main.pawtroller_psm.models.UserRegister
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity2 : AppCompatActivity() {

    var nameRegTxt: TextView?=null
    var lastnameRegTxt: TextView?=null
    var emailregTxt: TextView?=null
    var passRegTxt: TextView?=null
    var confirpassRegTxt: TextView?=null
    var birthdateTxt: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.hide()

        nameRegTxt= findViewById(R.id.nameRegTxt)
        lastnameRegTxt= findViewById(R.id.lastnameRegTxt)
        emailregTxt= findViewById(R.id.emailRegTxt)
        passRegTxt= findViewById(R.id.passRegTxt)
        confirpassRegTxt= findViewById(R.id.confirpassRegTxt)
        birthdateTxt= findViewById(R.id.birthdateTxt)

        ocultarTeclado()
        iniciarSesion()
        iniciarRegistro()
    }

    fun ocultarTeclado(){
        boton_registrar?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) hideSoftKeyboard()
        }
    }

    fun iniciarSesion(){
        text_identificador.setOnClickListener(){
            val iMainActivity= Intent(applicationContext,MainActivity::class.java)
            startActivity(iMainActivity)
        }
    }

    fun iniciarRegistro(){
        boton_registrar.setOnClickListener(){
            registrar()
        }
    }

    //funcion para ejecutar la peticion del registro
    fun registrar(){
        val name: String = nameRegTxt!!.text.toString() + " " + lastnameRegTxt!!.text.toString()
        val email: String = emailregTxt!!.text.toString()
        val password: String = passRegTxt!!.text.toString()
        val password_confirmation:String = confirpassRegTxt!!.text.toString()
        val birthdate: String = birthdateTxt!!.text.toString()

        val datosCorrectos:Boolean = validaDatos(name,email,password,password_confirmation,birthdate)

        if(datosCorrectos) {
            val userRegister = UserRegister(name, email, password, password_confirmation, birthdate)

            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<List<User>> = service.registrar(userRegister)

            result.enqueue(object : Callback<List<User>> {
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(this@MainActivity2, "Error" + t.message, Toast.LENGTH_LONG)
                        .show()
                }

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    val respuesta = response.body()

                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity2, "Usuario Creado con exito", Toast.LENGTH_LONG).show()
                        val iMainActivity = Intent(applicationContext, MainActivity::class.java)
                        startActivity(iMainActivity)
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                this@MainActivity2,
                                jObjError.getJSONObject("errors").toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity2, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }
    }

    fun validaDatos(name:String,email:String,password:String,password_confirmation:String,birthdate:String):Boolean{
        val passwordRegex = "^(?=.*\\d)(?=.*[\\u0021-\\u002b\\u003c-\\u0040])(?=.*[A-Z])(?=.*[a-z])\\S{8,16}\$".toRegex()
        val datoVacio:String = ""

        if(name.equals(datoVacio) || email.equals(datoVacio) ||password.equals(datoVacio) ||password_confirmation.equals(datoVacio) ||birthdate.equals(datoVacio)){
            Toast.makeText(this@MainActivity2, "Introduce todos los campos", Toast.LENGTH_LONG).show()
            return false
        }
        if(!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this@MainActivity2, "Introduce una dirección de correo valida", Toast.LENGTH_LONG).show()
            return false
        }
        if(password.length<8 || password.length >16 ){
            Toast.makeText(this@MainActivity2, "El password debe contener un minimo de 8 y un máximo de 16 carácteres" , Toast.LENGTH_LONG).show()
            return false
        }
        if(!password.contains(passwordRegex) ) {
            Toast.makeText(this@MainActivity2, "El password debe contener minimo una minuscula, una mayuscula, un digito y un caracter especial" , Toast.LENGTH_LONG).show()
            return false
        }
        if(!password.equals(password_confirmation)){
            Toast.makeText(this@MainActivity2, "Las contraseñas no coinciden" , Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}