package com.main.pawtroller_psm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.main.pawtroller_psm.Adapters.PostDraftsRecyclerAdapter
import com.main.pawtroller_psm.LocalDatabase.InitDatabase
import com.main.pawtroller_psm.Models.PostDrafts
import com.main.pawtroller_psm.models.*
import kotlinx.android.synthetic.main.fragment_crear_pet.*
import kotlinx.android.synthetic.main.fragment_create_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp

class CreatePostActivity : AppCompatActivity(), PostDraftsRecyclerAdapter.PostDraftEventHandler{
    companion object {
        lateinit var CONTEXT:Context
        lateinit var DB:InitDatabase
        val DB_NAME = "pawtroller.post_drafts"
        val VERSION = 4
        val TB_NAME = "postDraft"
    }

    var postid = 0
    var title:String = ""
    var content:String = ""
    var userid:Int = 0
    var categoryid:Int = 0
    var isEditing:Boolean = false

    var userData:User? = null
    var createPostFragment = CreatePost()
    var draftsAdapter:PostDraftsRecyclerAdapter? = null
    var draftsList:List<PostCreationData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_create_post)
        supportActionBar?.hide()
        CONTEXT = applicationContext
        DB = InitDatabase()

        var intentData: Intent = intent
        var userString = intentData.getStringExtra("userString")
        userData = Gson().fromJson(userString, User::class.java)

        var postCatsString = intentData.getStringExtra("categoriesList")
        var catsList = Gson().fromJson(postCatsString, Array<PostCategory>::class.java).toList()

        draftsList = getUserDrafts(userData!!.id.toInt())
        draftsAdapter = PostDraftsRecyclerAdapter(draftsList!!, userData!!, this)

        post_drafts_recycler.adapter = draftsAdapter
        post_drafts_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var spinnerOptions:MutableList<String> = mutableListOf()
        for (category in catsList)
            spinnerOptions.add(category.name)

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerOptions)
        post_create_catspinner.adapter = arrayAdapter
        post_create_catspinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            /* AdapterView = Padre de donde ocurrió la seleccion,
               View = vista dentro del adapterview,
               Int = posicion de vista en el adaptador,
               long = row del elemento seleccionado
            */
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                for (item in catsList){
                    if (spinnerOptions[p2].equals(catsList[p2].name)) {
                        categoryid = catsList[p2].id
                    }
                    break
                }
                post_create_catspinner.setSelection(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                categoryid = 0
            }
        }
        btn_create_postsave.setOnClickListener { updateSaveLocalPost() }
        btn_create_post.setOnClickListener { createRemotePost() }
        btn_create_postcancel.setOnClickListener { cancelPostCreation() }

        getUserDrafts(userData!!.id.toInt())
    }

    fun getUserDrafts(userid:Int): ArrayList<PostCreationData> {
        return PostDrafts().getFromLocalDB(userid)
    }

    fun updateUserDrafts(newList:List<PostCreationData>){
        draftsAdapter?.updateData(newList)
        draftsAdapter?.notifyDataSetChanged()
    }

    fun createRemotePost(){
        title = post_create_title.text.toString()
        content = post_create_content.text.toString()

        var newPostData = PostCreationData(
            0,
            this.title,
            this.content,
            userData!!.id.toInt(),
            categoryid,
            Timestamp(System.currentTimeMillis()).toString()
        )

        var service:Service = RestEngine.getRestEngine().create(Service::class.java)
        var result:Call<List<Post>> = service.createPost(newPostData)
        result.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                Toast.makeText(this@CreatePostActivity, "Se ha creado un post", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@CreatePostActivity, "Error: ${t.message.toString()}", Toast.LENGTH_LONG).show()
            }
        })
        resetGlobalVars()
    }

    fun updateSaveLocalPost(){
        this.title = post_create_title.text.toString()
        this.content = post_create_content.text.toString()

        var postData = PostCreationData(
            this.postid,
            this.title,
            this.content,
            userData!!.id.toInt(),
            this.categoryid,
            Timestamp(System.currentTimeMillis()).toString()
        )
        var postDraftObject = PostDrafts()

        if (isEditing){
            postDraftObject.updateToLocalDB(postData)
        } else {
            postDraftObject.addToLocalDB(postData)
        }
        resetGlobalVars()
        var posts: List<PostCreationData> = postDraftObject.getFromLocalDB(userData!!.id.toInt())
        updateUserDrafts(posts)
        resetGlobalVars()
    }

    fun cancelPostCreation(){
        finish()
    }

    override fun LoadPostDraft(holder: View, draft: PostCreationData) {
        this.postid = draft.id
        this.title = draft.title
        this.content = draft.content
        this.categoryid = draft.categoryid
        this.userid = draft.userid
        isEditing = true

        post_create_title.setText(this.title)
        post_create_content.setText(this.content)
        post_create_catspinner.setSelection(categoryid)
    }

    fun resetGlobalVars(){
        this.postid = 0
        this.title = ""
        this.content = ""
        this.categoryid = 0
        this.isEditing = false

        post_create_content.setText("")
        post_create_title.setText("")
    }
}