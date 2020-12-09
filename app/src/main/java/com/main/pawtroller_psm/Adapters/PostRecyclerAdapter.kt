package com.main.pawtroller_psm.Adapters
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.Models.Post
import com.main.pawtroller_psm.PostDetailview
import com.main.pawtroller_psm.R
import com.main.pawtroller_psm.PostListview
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.post_card.view.*
import java.lang.Error

class PostRecyclerAdapter (private var PostsList:List<Post>, private var context: Context?) :
    RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>(){

    interface PostRecyclerClickEventHandler {
        fun ShowPostFragment(holder: View, postId:Int)
    }
    private val clickHandler:PostRecyclerClickEventHandler = context as PostRecyclerClickEventHandler

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val userName:TextView = itemView.findViewById(R.id.post_card_username)
            val userImage:ImageView = itemView.findViewById(R.id.post_card_userimage)
            val postTitle:TextView = itemView.findViewById(R.id.post_card_title)
            val postContent:TextView = itemView.findViewById(R.id.post_card_content)
            val postDate:TextView = itemView.findViewById(R.id.post_card_date)

            init {
                itemView.setOnClickListener { view:View ->
                    val position:Int = adapterPosition
                }
            }
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return PostsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.userName.text = PostsList[position].user.name
            Picasso.get().load(PostsList[position].user.avatar_pic_path).into(holder.userImage)
            holder.postTitle.text = PostsList[position].title
            holder.postContent.text = PostsList[position].content
            holder.postDate.text = PostsList[position].created_at.toString()
            holder.itemView.setOnClickListener {clickHandler.ShowPostFragment(it, PostsList[position].id.toInt())}
    } catch (err:Error){
            Log.println(Log.ERROR, "Error BindViewHolder", err.toString())
        }
    }
}