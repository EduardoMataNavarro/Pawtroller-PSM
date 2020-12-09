package com.main.pawtroller_psm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.main.pawtroller_psm.Adapters.CommentRecyclerAdapter
import com.main.pawtroller_psm.Models.Comment
import com.main.pawtroller_psm.Models.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_post_detailview.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import org.w3c.dom.Text
import java.lang.Error

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailview.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailview : Fragment() {
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
        var rootView:View = inflater.inflate(R.layout.fragment_post_detailview, container, false)

        var commentList= arguments?.getString("CommentsList").toString()
        var postData= arguments?.getString("PostData").toString()

        try {
            var PostData: Post = Gson().fromJson(postData, Post::class.java) as Post
            var CommentList:List<Comment> = Gson().fromJson(commentList, Array<Comment>::class.java).toList()

            rootView.post_detail_username.text= PostData.user.nickname
            Picasso.get().load(PostData.user.avatar_pic_path).into(rootView.post_detail_userimage)
            rootView.post_detail_titulo.text = PostData.title
            rootView.post_detail_fecha.text = PostData.created_at.toString()
            rootView.post_detail_contenido.text = PostData.content
            rootView.post_detail_counter.text = "Comentarios: " + CommentList.count().toString()

            var recycler = rootView.post_comment_section
            recycler.adapter = CommentRecyclerAdapter(CommentList)
            recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        catch (err:Error){
            Log.println(Log.ERROR, "PITO: ", err.message.toString())
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostDetailview.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostDetailview().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}