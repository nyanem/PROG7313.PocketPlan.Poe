package com.example.pocketplan

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import java.sql.Blob

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "PocketPlan.db"

        // Table names
        private const val TABLE_TRANSACTIONS = "transactions"
        private const val TABLE_RECEIPTS = "receipts"

        // Common column names
        private const val KEY_ID = "id"

        // Transactions table columns
        private const val KEY_AMOUNT = "amount"
        private const val KEY_CATEGORY = "category"
        private const val KEY_DATE = "date"
        private const val KEY_RECEIPT_ID = "receipt_id"


    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Transactions table
        val createTransactionsTable = """
            CREATE TABLE $TABLE_TRANSACTIONS (
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_AMOUNT REAL,
                $KEY_CATEGORY TEXT,
                $KEY_DATE TEXT,
                $KEY_RECEIPT_ID INTEGER
            )
        """.trimIndent()

        // Create Receipts table
        val createReceiptsTable = """
    CREATE TABLE $TABLE_RECEIPTS (
        $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        image BLOB
    )
    """.trimIndent()
        db.execSQL(createTransactionsTable)
        db.execSQL(createReceiptsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECEIPTS")

        // Create tables again
        onCreate(db)
    }

    // Add a new transaction
    fun addTransaction(transaction: Transaction): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_AMOUNT, transaction.amount)
            put(KEY_CATEGORY, transaction.category)
            put(KEY_DATE, transaction.date)
            // If there's a receipt URI, save the receipt ID
            transaction.receiptUri?.let {
                val lastPathSegment = it.lastPathSegment
                if (lastPathSegment != null && lastPathSegment.isNotEmpty()) {
                    put(KEY_RECEIPT_ID, lastPathSegment.toInt())
                }
            }
        }

        // Insert row
        val id = db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()
        return id
    }

    fun insertImage(imageBytes: ByteArray): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("image", imageBytes)
        }
        val id = db.insert(TABLE_RECEIPTS, null, values)
        db.close()
        return id
    }


    // Get all transactions
    fun getAllTransactions(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS ORDER BY $KEY_DATE DESC"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID))
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_AMOUNT))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))

                // Get receipt URI if available
                val receiptId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_RECEIPT_ID))
                val receiptUri = if (receiptId > 0) {
                    Uri.parse("content://com.example.pocketplan.fileprovider/$receiptId")
                } else null

                val transaction = Transaction(id , amount, category, date, receiptUri)
                transactions.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return transactions
    }

    // Add receipt and return its ID
    fun addReceipt(uri: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            val KEY_URI = ""
            put(KEY_URI, uri)
        }

        // Insert row
        val id = db.insert(TABLE_RECEIPTS, null, values)
        db.close()
        return id
    }

    // Get current balance
    fun getBalance(): Double {
        val transactions = getAllTransactions()
        return transactions.sumOf { it.amount }
    }
}