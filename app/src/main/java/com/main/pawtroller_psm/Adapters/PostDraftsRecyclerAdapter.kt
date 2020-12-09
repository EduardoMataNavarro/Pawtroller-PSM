package com.main.pawtroller_psm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.main.pawtroller_psm.R
import com.main.pawtroller_psm.models.PostCreationData
import com.main.pawtroller_psm.models.User
import com.squareup.picasso.Picasso


class PostDraftsRecyclerAdapter (private var PostDraftsList:List<PostCreationData>, private var user: User, private var context: Context?) :
    RecyclerView.Adapter<PostDraftsRecyclerAdapter.ViewHolder>(){

    interface PostDraftEventHandler {
        fun LoadPostDraft(holder: View, draft: PostCreationData)
    }
    private var clickHandler:PostDraftEventHandler = context as PostDraftEventHandler

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val userName: TextView = itemView.findViewById(R.id.post_card_username)
            val userImage: ImageView = itemView.findViewById(R.id.post_card_userimage)
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

        fun updateData(updatedData:List<PostCreationData>){
            this.PostDraftsList = updatedData
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