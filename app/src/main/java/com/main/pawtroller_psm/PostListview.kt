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
import kotlinx.android.synthetic.main.fragment_post_listview.*
import kotlinx.android.synthetic.main.fragment_post_listview.view.*

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
        createPostButton.setOnClickListener { createPostView(userString) }

        recycler.adapter = PostRecyclerAdapter(PostsList, context)
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)

        return rootView
    }

    fun createPostView(userString:String){
        try {
            /* No se llama un service, aquí se manda a llamar al Activity de crear post desde un intent */
            var createPostIntent = Intent(context, CreatePostActivity::class.java)
            createPostIntent.putExtra("userString", userString)
            startActivity(createPostIntent)
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