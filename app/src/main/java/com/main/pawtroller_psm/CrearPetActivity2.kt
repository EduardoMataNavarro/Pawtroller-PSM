package com.main.pawtroller_psm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.RegistrarPet
import com.main.pawtroller_psm.Models.UploadRequestBody
import kotlinx.android.synthetic.main.fragment_crear_pet.*
import kotlinx.android.synthetic.main.fragment_nueva_foto_pet.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class CrearPetActivity2 : AppCompatActivity() , UploadRequestBody.UploadCallback{

    var nameRegCon: TextView? = null
    var imgRegCon: ImageView? = null
    var file: File ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_pet2)
        supportActionBar?.hide()

        val datos: Intent = intent
        var registrarPetString = datos.getStringExtra("registrarPetString")
        var gson = Gson()
        var registrarPet = gson.fromJson(registrarPetString, RegistrarPet::class.java)

        nameRegCon = findViewById(R.id.nameRegCon)
        imgRegCon = findViewById(R.id.imgRegCon)

        nameRegCon!!.text = registrarPet.name

        imgRegCon!!.setOnClickListener() {
            cargarImagen()
        }

        btnRegPet2.setOnClickListener() {
            completarDatos(registrarPet)
        }

        fabCerrarVentana2.setOnClickListener(){
            finish()
        }
    }

    private fun cargarImagen() {
        val imagenIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagenIntent.setType("image/")
        startActivityForResult(Intent.createChooser(imagenIntent, "Selecciona la aplicacion"), 10)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            val path: Uri? = data?.data
            imgRegCon?.setImageURI(path)

            val parcelFileDescriptor = contentResolver.openFileDescriptor(path!!, "r", null)?:return

            file = File(cacheDir, contentResolver.getFileName(path))
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

        }
    }

    private fun completarDatos(registrarPet: RegistrarPet?) {
        var requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
        var filePart = MultipartBody.Part.createFormData("petImg", file!!.name, requestBody)

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Pet>> = service.agregarMascotas(
            filePart, RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                registrarPet!!.name
            ),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrarPet!!.nickname),
            RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                registrarPet!!.description
            ),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrarPet!!.userid),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrarPet!!.birthdate),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrarPet!!.age),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrarPet!!.pettype)
        )

        result.enqueue(object : Callback<List<Pet>> {
            override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                val respuesta = response.body()
                if (respuesta!![0].id == null) {
                    Toast.makeText(this@CrearPetActivity2, "Ocurrio un error", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this@CrearPetActivity2,"Pet registrada con éxito",Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                Toast.makeText(this@CrearPetActivity2, t.message, Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onProgressUpdate(percentage: Int) {
        TODO("Not yet implemented")
    }

}





