package com.main.pawtroller_psm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.ResetPasswordUser
import com.main.pawtroller_psm.Models.ResponseLogin
import com.main.pawtroller_psm.Models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class UserProfile : Fragment() {

    var listaMascotaUsuario :List<Pet> = listOf()
    var user:User ?=null
    private val AVATAR_CODE = 1000
    private val BANNER_CODE = 1002
    private val AVATAR = "avatar"
    private val BANNER = "banner"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_user_profile, container, false)

        var userString = arguments?.getString("userString")
        var listaMascotaUsuarioString= arguments?.getString("listaMascotaUsuarioString").toString()
        var gson = Gson()
        user = gson.fromJson(userString, User::class.java)

        var createdAtParse = LocalDate.parse(user!!.created_at, DateTimeFormatter.ISO_DATE_TIME)
        var createdAt = createdAtParse.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
        var birthDate = LocalDate.parse(user!!.birthdate, DateTimeFormatter.ISO_DATE_TIME)
        var period =  Period.between(birthDate, LocalDate.now())
        val edad = period.years

        if(!"[]".equals(listaMascotaUsuarioString)) {
             listaMascotaUsuario= ArrayList(
                 gson.fromJson(
                     listaMascotaUsuarioString,
                     Array<Pet>::class.java
                 ).toList()
             )
        }
        view.userName.text = user!!.name
        view.correoUser.text = "Correo: " + user!!.email
        view.edadUser.text = "Edad: "+ edad.toString()
        view.registroUser.text = "Miembro desde: " + createdAt
        Picasso.get().load(user!!.avatar_pic_path).into(view.userImage)
        Picasso.get().load(user!!.banner_pic_path).into(view.fondoUser)

        view.passEditBtn.setOnClickListener(){
            val pass:String = view.editTextTextPassword2.text.toString()
            val resetPass: String = view.editTextTextPassword.text.toString()
            if("".equals(pass) || "".equals(resetPass)) {
                Toast.makeText(
                    context,
                    "Introduce tu password actual y tu nuevo password",
                    Toast.LENGTH_LONG
                ).show()
            }else {
                cambiarPass(pass, resetPass)
            }
        }

        view.userImage.setOnClickListener(){
            cambiarAvatar(AVATAR)
        }

        view.fondoUser.setOnClickListener(){
            cambiarAvatar(BANNER)
        }
        return view
    }

    private fun cambiarAvatar(tipoImagen: String) {
        val imagenIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagenIntent.setType("image/")
        if(AVATAR.equals(tipoImagen)) {
            startActivityForResult(
                Intent.createChooser(imagenIntent, "Selecciona la aplicacion"),
                AVATAR_CODE
            )
        }else if(BANNER.equals(tipoImagen)){
            startActivityForResult(
                Intent.createChooser(imagenIntent, "Selecciona la aplicacion"),
                BANNER_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === AppCompatActivity.RESULT_OK) {
            val path: Uri? = data?.data

            val parcelFileDescriptor =
                context!!.contentResolver.openFileDescriptor(path!!, "r", null) ?: return

            var file = File(context!!.cacheDir, context!!.contentResolver.getFileName(path))
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            if(requestCode===AVATAR_CODE) {
                resetAvatar(file,AVATAR)
            }else if(requestCode===BANNER_CODE){
                resetAvatar(file,BANNER)
            }

        }
    }

    private fun resetAvatar(file: File, tipoImagen:String) {
       var requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
        var filePart = MultipartBody.Part.createFormData("media", file!!.name, requestBody)

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseLogin> = service.cambiarMediaUsuario(
            filePart, RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tipoImagen),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user!!.id)
        )

        result.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                val respuesta = response.body()
                if (response.isSuccessful) {
                    if(AVATAR.equals(tipoImagen)) {
                        Toast.makeText(context, "Avatar actualizado con éxito", Toast.LENGTH_LONG)
                            .show()
                    }else{
                        Toast.makeText(context, "Banner actualizado con éxito", Toast.LENGTH_LONG)
                            .show()
                    }
                    //TODO actualizar la vista para que se vea el cambio de banner y avatar
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            context,
                            jObjError.getJSONObject("errors").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun cambiarPass(pass: String, resetPass: String) {
        val resetPasswordUser = ResetPasswordUser(pass, resetPass, user!!.id)

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseLogin> = service.resetPassword(resetPasswordUser)

        result.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                val respuesta = response.body()
                if (response.isSuccessful) {
                    Toast.makeText(context, "Password actualizado con éxito", Toast.LENGTH_LONG)
                        .show()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            context,
                            jObjError.getJSONObject("errors").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(context, "Error" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfile().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecycler()
    }

    fun initRecycler(){
        recyclerPets.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerPetAdapter(listaMascotaUsuario)
        recyclerPets.adapter = adapter
    }

}