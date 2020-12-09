package com.main.pawtroller_psm.LocalDatabase

import android.provider.BaseColumns

class Contract {
    class Draft:BaseColumns{
        companion object {
            val ID = "id"
            val USER_ID = "userid"
            val CATEGORY_ID = "categoriaid"
            val TITLE = "title"
            val CONTENT = "content"
            val CREATION_DATE = "creationdate"
        }
    }
}