package com.example.prog7313pocketplanpoe


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CategoryDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PocketPlan.db"
        private const val DATABASE_VERSION = 8
        private const val TABLE_NAME = "selected_categories"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertCategory(name: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    fun getAllCategories(): List<String> {
        val db = readableDatabase
        val categories = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT name FROM selected_categories", null)
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return categories
    }
}
