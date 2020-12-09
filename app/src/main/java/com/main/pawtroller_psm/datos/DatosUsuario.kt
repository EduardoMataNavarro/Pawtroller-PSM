package com.main.pawtroller_psm.datos

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.main.pawtroller_psm.interfaces.DataExchangeActivity
import com.main.pawtroller_psm.interfaces.DataExchangeFragment
import com.main.pawtroller_psm.models.User

class DatosUsuario: DataExchangeActivity, DataExchangeFragment{

    var user: User ?= null
    var userString: String ?= ""

    var gson = Gson()

    override fun obtenerDatosActivity(datos: Intent) {
        userString = datos.getStringExtra("userString")
        user = gson.fromJson(userString,User::class.java)
    }

    override fun enviarDatosActivity(iActivity: Intent) {
        userString = gson.toJson(user)
        iActivity.putExtra("userString", userString)
    }

    override fun obtenerDatosFragment(arguments: Bundle?) {
        userString= arguments?.getString("userString").toString()
        user = gson.fromJson(userString, User::class.java)
    }

    override fun enviarDatosFragment(bundle: Bundle) {
        bundle.putString("userString", userString)
    }
}