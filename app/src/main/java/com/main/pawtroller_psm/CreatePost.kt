package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.gson.Gson
import com.main.pawtroller_psm.models.PostCategory
import com.main.pawtroller_psm.models.TipoMascota
import com.main.pawtroller_psm.models.User
import kotlinx.android.synthetic.main.fragment_create_post.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [create_post.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreatePost : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView =  inflater.inflate(R.layout.fragment_create_post, container, false)

        var userString = ""
        var categoriesString = ""
        userString = arguments?.getString("userString").toString()
        categoriesString = arguments?.getString("categoriesList").toString()

        user = Gson().fromJson(userString, User::class.java)
        var postCategories:List<PostCategory> = Gson().fromJson(categoriesString, Array<PostCategory>::class.java).toList()

        var categoriesSpinner = rootView.post_create_catspinner
        var categoriesList: MutableList<String> = mutableListOf()
        for (category in postCategories)
            categoriesList.add(category.name)
        //val catSpinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categoriesList)


        return rootView
    }

    fun OpenCreatePost(userString:String){

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<List<TipoMascota>>> = service.obtenerMascotas()
        var arrayMascotas: List<List<TipoMascota>> = listOf()

        result.enqueue(object : Callback<List<List<TipoMascota>>> {
            override fun onResponse(call: Call<List<List<TipoMascota>>>, response: Response<List<List<TipoMascota>>>) {
                arrayMascotas = response.body()!!
                var gson = Gson()
                var listaMascostaString = gson.toJson(arrayMascotas[0])

                val iCrearPetActivity = Intent(context,CrearPetActivity::class.java)
                iCrearPetActivity.putExtra("listaMascostaString",listaMascostaString)
                iCrearPetActivity.putExtra("userString",userString)
                startActivity(iCrearPetActivity)
            }

            override fun onFailure(call: Call<List<List<TipoMascota>>>, t: Throwable) {
                Toast.makeText(context, "Error" + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment create_post.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreatePost().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}