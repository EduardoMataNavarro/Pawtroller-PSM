package com.main.pawtroller_psm

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.main.pawtroller_psm.Adapters.PostRecyclerAdapter
import com.main.pawtroller_psm.Models.Pet
import com.main.pawtroller_psm.Models.Post
import com.main.pawtroller_psm.Models.PostCategory
import kotlinx.android.synthetic.main.fragment_post_listview.*
import kotlinx.android.synthetic.main.fragment_post_listview.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [post_listview.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostListview : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        var rootView:View = inflater.inflate(R.layout.fragment_post_listview, container, false)

        var userString:String = ""
        var postsList:String = ""
        userString= arguments?.getString("userString").toString()
        postsList= arguments?.getString("PostsList").toString()

        var PostsList:List<Post> = Gson().fromJson(postsList, Array<Post>::class.java).toList() as ArrayList<Post>
        var recycler = rootView.post_recyclerView
        var createPostButton:Button = rootView.button_create_post
        createPostButton.setOnClickListener { createPostActivity(userString) }

        recycler.adapter = PostRecyclerAdapter(PostsList, context, userString)
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)

        return rootView
    }

    fun createPostActivity(userString:String){
        try {
            var createPostIntent = Intent(context, CreatePostActivity::class.java)
            createPostIntent.putExtra("userString", userString)

            val service:Service = RestEngine.getRestEngine().create(Service::class.java)
            var result: Call<List<List<PostCategory>>> = service.getPostCategoriesList()
            result.enqueue(object : Callback<List<List<PostCategory>>> {

                override fun onResponse(call: Call<List<List<PostCategory>>>, response: Response<List<List<PostCategory>>>) {
                    try {
                        val respuesta = response.body()
                        val categoriesList:List<PostCategory> = respuesta!![0]
                        var categoriesListString:String = Gson().toJson(categoriesList)

                        createPostIntent.putExtra("categoriesList", categoriesListString)
                        startActivity(createPostIntent)

                    } catch (err:Error) {
                        Toast.makeText(context, "Err: ${err.message.toString()}", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<List<List<PostCategory>>>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message.toString()}", Toast.LENGTH_LONG).show()
                }

            })
        } catch (err:Error) {
            Toast.makeText(this.context, "Error: ${err.message.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment post_listview.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostListview().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}