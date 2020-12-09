package com.main.pawtroller_psm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.Models.Comment
import com.main.pawtroller_psm.Models.Post
import com.main.pawtroller_psm.Models.PostCreationData
import com.main.pawtroller_psm.Models.User
import com.main.pawtroller_psm.R
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class PostDraftsRecyclerAdapter (private var PostDraftsList:List<PostCreationData>, private var user:User, private var context: Context?) :
    RecyclerView.Adapter<PostDraftsRecyclerAdapter.ViewHolder>(){

    interface PostDraftEventHandler {
        fun LoadPostDraft(holder: View, draft: PostCreationData)
    }
    private var clickHandler:PostDraftEventHandler = context as PostDraftEventHandler

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val userName: TextView = itemView.findViewById(R.id.comment_username)
            val userImage: ImageView = itemView.findViewById(R.id.comment_userimage)
            val timestamp: TextView = itemView.findViewById(R.id.post_card_date)
            val title: TextView = itemView.findViewById(R.id.post_card_title)
            val content: TextView = itemView.findViewById(R.id.post_card_content)

            init {
                itemView.setOnClickListener { view: View ->
                    val position:Int = adapterPosition
                    //Toast.makeText(itemView.context, "Hola este es el post ${ CommentsList[position].id }", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return PostDraftsList.count()
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int){
            holder.userName.text = user.name
            Picasso.get().load(user.avatar_pic_path).into(holder.userImage)
            holder.title.text = PostDraftsList[position].title
            holder.content.text = PostDraftsList[position].content
            holder.itemView.setOnClickListener { clickHandler.LoadPostDraft(it, PostDraftsList[position]) }
        }
}