package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.main.pawtroller_psm.models.ResponseLogin
import com.main.pawtroller_psm.models.UserLogin
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(){

    var emailTxt:TextView?=null
    var passwordTxt:TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(1000)
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()



        emailTxt= findViewById(R.id.emailLogin)
        passwordTxt= findViewById(R.id.passwordLogin)

        ocultarTeclado()
        registroFormulario()

        iniciarApp()
    }

    fun ocultarTeclado(){
        btn_login?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) hideSoftKeyboard()
        }
    }

    fun registroFormulario(){
        text_registro.setOnClickListener(){
            val iMainActivity2= Intent(applicationContext, MainActivity2::class.java)
            startActivity(iMainActivity2)
        }
    }

    fun iniciarApp(){
        btn_login.setOnClickListener(){
            login()
        }
    }


    //funcion encargada del login
    fun login(){
        val email:String = emailTxt!!.text.toString()
        val password:String = passwordTxt!!.text.toString()

        val datosCorrectos:Boolean =true
        if(datosCorrectos) {
            val userLogin = UserLogin(email, password)

            val service: Service = RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<ResponseLogin> = service.login(userLogin)

            result.enqueue(object : Callback<ResponseLogin> {
                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error" + t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    val respuesta = response.body()
                    if (response.isSuccessful) {
                        if("success".equals(respuesta!!.message)) {

                            var gson = Gson()
                            var userString = gson.toJson(respuesta!!.user[0])
                            val iMainApp = Intent(applicationContext, MainApp::class.java)
                            iMainApp.putExtra("userString", userString)
                            startActivity(iMainApp)
                        }else{
                            Toast.makeText(this@MainActivity, respuesta.message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                this@MainActivity,
                                jObjError.getJSONObject("errors").toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }
    }

    fun validaDatos(email:String,password:String):Boolean{
        val datoVacio:String = ""

        if(email.equals(datoVacio) ||password.equals(datoVacio)){
            Toast.makeText(this@MainActivity, "Introduce todos los campos", Toast.LENGTH_LONG).show()
            return false
        }
        if(!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this@MainActivity, "Introduce una dirección de correo valida", Toast.LENGTH_LONG).show()
            return false
        }
        if(password.length<8 || password.length >16 ){
            Toast.makeText(this@MainActivity, "El password debe contener un minimo de 8 y un máximo de 16 carácteres" , Toast.LENGTH_LONG).show()
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