package com.main.pawtroller_psm.Models

import android.widget.Toast
import com.main.pawtroller_psm.CreatePostActivity
import com.main.pawtroller_psm.LocalDatabase.Contract
import okhttp3.internal.format
import java.lang.Error
import java.sql.Timestamp

class PostDrafts {
    fun getFromLocalDB(user_id: Int): ArrayList<PostCreationData> {
        var posts = arrayListOf<PostCreationData>()
        try {
            val query = "SELECT * FROM ${CreatePostActivity.TB_NAME} " +
                    "WHERE userid = ${user_id}"
            val db = CreatePostActivity.DB.writableDatabase
            var queryCursor = db.rawQuery(query, null)
            if (queryCursor.count>0){
                queryCursor.moveToFirst()
                do {
                    posts.add(
                        PostCreationData(
                            queryCursor.getInt(queryCursor.getColumnIndex(Contract.Draft.ID)),
                            queryCursor.getString(queryCursor.getColumnIndex(Contract.Draft.TITLE)),
                            queryCursor.getString(queryCursor.getColumnIndex(Contract.Draft.CONTENT)),
                            queryCursor.getInt(queryCursor.getColumnIndex(Contract.Draft.USER_ID)),
                            queryCursor.getInt(queryCursor.getColumnIndex(Contract.Draft.CATEGORY_ID)),
                            queryCursor.getString(queryCursor.getColumnIndex(Contract.Draft.CREATION_DATE))
                        )
                    )
                } while (queryCursor.moveToNext())
            }
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
                    "${Contract.Draft.CATEGORY_ID}," +
                    "${Contract.Draft.CREATION_DATE})" +
                    " VALUES (" +
                    "'${postData.title}'," +
                    "'${postData.content}'," +
                    "${postData.userid}," +
                    "${postData.categoryid}," +
                    "'${postData.created_at}');"
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

    fun updateToLocalDB(postData: PostCreationData) {
        try {
            val updateQuery = "UPDATE ${CreatePostActivity.TB_NAME} SET " +
                    "${Contract.Draft.TITLE} = '${postData.title}', " +
                    "${Contract.Draft.CONTENT} = '${postData.content}', " +
                    "${Contract.Draft.CATEGORY_ID} = ${postData.categoryid} " +
                    "WHERE ${Contract.Draft.ID} = ${postData.id};"
            var dbUpdate = CreatePostActivity.DB.writableDatabase
            dbUpdate.execSQL(updateQuery)
            dbUpdate.close()
        } catch (err: Error) {
            Toast.makeText(
                CreatePostActivity.CONTEXT,
                "Error: ${err.message.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun removeFromLocalDB(postId: Int){
        try {
            val query = "DELETE FROM ${CreatePostActivity.TB_NAME} WHERE ${Contract.Draft.ID} = ${postId}"
            var dbDelete = CreatePostActivity.DB.writableDatabase
            dbDelete.execSQL(query)
            dbDelete.close()
        } catch (err:Error){
            Toast.makeText(CreatePostActivity.CONTEXT, "Error: ${err.message.toString()}", Toast.LENGTH_LONG).show()
        }
    }
}