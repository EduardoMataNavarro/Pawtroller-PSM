package com.main.pawtroller_psm.interfaces

import android.os.Bundle

interface DataExchangeFragment {

    fun obtenerDatosFragment(arguments: Bundle?)

    fun enviarDatosFragment(bundle: Bundle)
}