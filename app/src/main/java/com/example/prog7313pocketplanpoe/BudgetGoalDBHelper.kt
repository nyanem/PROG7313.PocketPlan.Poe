package com.example.prog7313pocketplanpoe

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getDoubleOrNull

class BudgetGoalDBHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "BudgetGoals.db"
        private const val DATABASE_VERSION = 8
        private const val COLUMN_MIN_GOAL = "min_goal"
        private const val COLUMN_MAX_GOAL = "max_goal"


        const val TABLE_NAME = "budget_goals"
        const val COLUMN_ID = "id"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_GOAL_AMOUNT = "goal_amount"
        const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {


        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_GOAL_AMOUNT REAL NOT NULL,
                $COLUMN_TIMESTAMP INTEGER NOT NULL
            );
        """.trimIndent()
        db.execSQL(createTable)


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertGoal(category: String, amount: Double, timestamp: Long): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY, category)
            put(COLUMN_GOAL_AMOUNT, amount)
            put(COLUMN_TIMESTAMP, timestamp)
        }
        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L
    }

    fun getGoalForCategory(category: String): Double? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_GOAL_AMOUNT FROM $TABLE_NAME WHERE $COLUMN_CATEGORY = ? ORDER BY $COLUMN_TIMESTAMP DESC LIMIT 1",
            arrayOf(category)
        )


        var result: Double? = null
        if (cursor.moveToFirst()) {
            result = cursor.getDouble(0)
        }

        cursor.close()
        db.close()
        return result
    }

    fun getAllGoals(): List<BudgetGoal> {
        val goals = mutableListOf<BudgetGoal>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_TIMESTAMP DESC")

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val category = getString(getColumnIndexOrThrow(COLUMN_CATEGORY))
                val goalAmount = getDouble(getColumnIndexOrThrow(COLUMN_GOAL_AMOUNT))
                val timestamp = getLong(getColumnIndexOrThrow(COLUMN_TIMESTAMP))

                goals.add(BudgetGoal(id, category, goalAmount, timestamp))
            }
            close()
        }

        return goals
    }

}

// Data class to represent a goal
data class BudgetGoal(
    val id: Int,
    val category: String,
    val goalAmount: Double,
    val timestamp: Long
)

