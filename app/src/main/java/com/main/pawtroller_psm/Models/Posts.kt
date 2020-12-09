package com.main.pawtroller_psm.Models

import android.app.Application
import android.widget.Toast
import androidx.annotation.MainThread
import com.main.pawtroller_psm.CreatePostActivity
import com.main.pawtroller_psm.LocalDatabase.Contract
import com.main.pawtroller_psm.MainApp
import okhttp3.internal.format
import java.lang.Error
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import kotlin.contracts.contract

data class PostCategory (
    val id:String,
    val name:String,
    val description:String,
    val timestamps: Timestamp
)

data class PostCreationData(
    val id:Int,
    val title:String,
    val content:String,
    val userid:Int,
    val categoryid:Int,
    val created_at: Timestamp
)

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val user_id: Int,
    val category_id: Int,
    val created_at: Timestamp,
    val user: User
)

class LocalPosts {
    fun getFromLocalDB(user_id: Int): ArrayList<PostCreationData> {
        var posts = arrayListOf<PostCreationData>()
        try {
            val query = "SELECT * FROM ${CreatePostActivity.TB_NAME} " +
                    "WHERE userid = ${user_id}"
            val db = CreatePostActivity.DB.writableDatabase
            var queryCursor = db.rawQuery(query, null)
            queryCursor.moveToFirst()
            do {
                posts.add(
                    PostCreationData(
                        queryCursor.getInt(queryCursor.getColumnIndex(Contract.Draft.ID)),
                        queryCursor.getString(queryCursor.getColumnIndex(Contract.Draft.TITLE)),
                        queryCursor.getString(queryCursor.getColumnIndex(Contract.Draft.CONTENT)),
                        queryCursor.getInt(queryCursor.getColumnIndex(Contract.Draft.USER_ID)),
                        queryCursor.getInt(queryCursor.getColumnIndex(Contract.Draft.CATEGORY_ID)),
                        Timestamp.valueOf(format(queryCursor.getString(queryCursor.getColumnIndex(Contract.Draft.CREATION_DATE)),"yyyy-m[m]-d[d] hh:mm:ss[.f…]"))
                    )
                )
            } while (queryCursor.moveToNext())
            db.close()
            return posts
        } catch (err: Error) {
            Toast.makeText(
                CreatePostActivity.CONTEXT,
                "Error: ${err.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
        return posts
    }

    fun addToLocalDB(postData: PostCreationData) {
        try {
            val query = "INSERT INTO ${CreatePostActivity.TB_NAME} (" +
                    "${Contract.Draft.TITLE}," +
                    "${Contract.Draft.CONTENT}," +
                    "${Contract.Draft.USER_ID}," +
                    "${Contract.Draft.CATEGORY_ID})" +
                    "VALUES (" +
                    "${postData.title}," +
                    "${postData.content}" +
                    "${postData.userid}," +
                    "${postData.categoryid});"
            val db = CreatePostActivity.DB.writableDatabase
            db.execSQL(query)
            db.close()
        } catch (err: Error) {
            Toast.makeText(
                CreatePostActivity.CONTEXT,
                "Error: ${err.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun updateToLocalDB(postData: PostCreationData, id: Int): PostCreationData? {
        var post: PostCreationData? = null
        try {
            val updateQuery = "UPDATE ${CreatePostActivity.TB_NAME} SET " +
                    "${Contract.Draft.TITLE} = ${postData.title}," +
                    "${Contract.Draft.CONTENT} = ${postData.content}," +
                    "${Contract.Draft.CATEGORY_ID} = ${postData.categoryid}," +
                    "WHERE ${Contract.Draft.ID} = ${id};"
            var dbUpdate = CreatePostActivity.DB.writableDatabase
            dbUpdate.execSQL(updateQuery)
            dbUpdate.close()

            val selectQuery =
                "SELECT * FROM ${CreatePostActivity.TB_NAME} WHERE ${Contract.Draft.ID} = ${id};"
            var dbSelect = CreatePostActivity.DB.writableDatabase
            val cursor = dbSelect.rawQuery(selectQuery, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                post = PostCreationData(
                    cursor.getInt(cursor.getColumnIndex(Contract.Draft.ID)),
                    cursor.getString(cursor.getColumnIndex(Contract.Draft.TITLE)),
                    cursor.getString(cursor.getColumnIndex(Contract.Draft.CONTENT)),
                    cursor.getInt(cursor.getColumnIndex(Contract.Draft.USER_ID)),
                    cursor.getInt(cursor.getColumnIndex(Contract.Draft.CATEGORY_ID)) ,
                    Timestamp.valueOf(format(cursor.getString(cursor.getColumnIndex(Contract.Draft.CREATION_DATE)),"yyyy-m[m]-d[d] hh:mm:ss[.f…]"))
                )
            }
            return post

        } catch (err: Error) {
            Toast.makeText(
                CreatePostActivity.CONTEXT,
                "Error: ${err.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
        return post
    }
}
