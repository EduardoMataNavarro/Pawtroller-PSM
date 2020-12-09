package com.main.pawtroller_psm.datos

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.main.pawtroller_psm.interfaces.DataExchangeActivity
import com.main.pawtroller_psm.interfaces.DataExchangeFragment
import com.main.pawtroller_psm.models.Pet
import com.main.pawtroller_psm.models.RegistrarPet
import com.main.pawtroller_psm.models.TipoMascota

class DatosMascota: DataExchangeActivity, DataExchangeFragment {

    var listaTipoMascota: List<TipoMascota> = listOf()
    var listaTipoMascotaString:  String = "[]"
    var listaMascotaUsuario : List<Pet> = listOf()
    var listaMascotaUsuarioString: String = "[]"
    var registrarPet: RegistrarPet?= null
    var registrarPetString:String = ""

    var gson = Gson()

    override fun obtenerDatosActivity(datos: Intent) {
        listaTipoMascotaString = datos.getStringExtra("listaTipoMascotaString")!!
        listaTipoMascota = ArrayList(gson.fromJson(listaTipoMascotaString, Array<TipoMascota>::class.java).toList())

        if(datos.getStringExtra("registrarPetString")!=null) {
            registrarPetString = datos?.getStringExtra("registrarPetString")!!
            registrarPet = gson.fromJson(registrarPetString, RegistrarPet::class.java)
        }

    }

    override fun enviarDatosActivity(iActivity: Intent) {
        if(registrarPet!=null) {
            registrarPetString = gson.toJson(registrarPet!!)
        }else{
            registrarPetString= ""
        }
        listaTipoMascotaString = gson.toJson(listaTipoMascota!!)
        iActivity.putExtra("registrarPetString", registrarPetString)
        iActivity.putExtra("listaTipoMascotaString",listaTipoMascotaString)
    }

    override fun obtenerDatosFragment(arguments: Bundle?) {
        listaMascotaUsuarioString= arguments?.getString("listaMascotaUsuarioString").toString()
        listaTipoMascotaString = arguments?.getString("listaTipoMascotaString").toString()
        if(!"[]".equals(listaMascotaUsuarioString)) {
            listaMascotaUsuario =
                ArrayList(
                    gson.fromJson(listaMascotaUsuarioString, Array<Pet>::class.java)
                        .toList()
                )
        }
        if(!"[]".equals(listaTipoMascota)) {
            listaTipoMascota = ArrayList(
                gson.fromJson(listaTipoMascotaString, Array<TipoMascota>::class.java).toList()
            )
        }

    }

    override fun enviarDatosFragment(bundle: Bundle) {
        listaMascotaUsuarioString = gson.toJson(listaMascotaUsuario)
        listaTipoMascotaString = gson.toJson(listaTipoMascota)
        bundle.putString("listaMascotaUsuarioString", listaMascotaUsuarioString)
        bundle.putString("listaTipoMascotaString", listaTipoMascotaString)
    }
}