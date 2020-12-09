package com.main.pawtroller_psm.LocalDatabase

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.main.pawtroller_psm.CrearPetActivity
import com.main.pawtroller_psm.CreatePostActivity

class InitDatabase:SQLiteOpenHelper(CreatePostActivity.CONTEXT, CreatePostActivity.DB_NAME, null, CreatePostActivity.VERSION) {

    val SQL_CREATE_DRAFT_TABLE = "CREATE TABLE ${CreatePostActivity.TB_NAME} (" +
            "${Contract.Draft.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${Contract.Draft.TITLE} VARCHAR(100) NOT NULL," +
            "${Contract.Draft.CONTENT} VARCHAR(255) NOT NULL," +
            "${Contract.Draft.USER_ID} INTEGER NOT NULL," +
            "${Contract.Draft.CATEGORY_ID} INTEGER NOT NULL," +
            "${Contract.Draft.CREATION_DATE} VARCHAR);"
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_DRAFT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${CreatePostActivity.TB_NAME}");
        onCreate(db);
    }

}