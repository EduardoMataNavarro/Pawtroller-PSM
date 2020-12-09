package com.main.pawtroller_psm

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

class LoadingDialog {

    var activity: Activity ?= null
    var dialog : AlertDialog ?= null

    constructor(myActivity: Activity){
        activity=myActivity
    }

    fun startLoadingdialog(){

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity!!.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_layout,null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog?.show()
    }



    fun dismissDialog(){
        dialog?.dismiss()
    }
}
