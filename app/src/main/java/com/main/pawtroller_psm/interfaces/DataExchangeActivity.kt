package com.main.pawtroller_psm.interfaces

import android.content.Intent

interface DataExchangeActivity{

    fun obtenerDatosActivity(datos: Intent)

    fun enviarDatosActivity(iActivity: Intent)
}