package com.example.prog7313pocketplanpoe

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.widget.ImageView
import com.example.prog7313pocketplanpoe.Transaction


class PocketPlanDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PocketPlan.db"
        private const val DATABASE_VERSION = 8

        // Category Table
        private const val CATEGORY_TABLE_NAME = "selected_categories"
        private const val CATEGORY_COLUMN_ID = "id"
        private const val CATEGORY_COLUMN_NAME = "name"

        // User Table
        private const val USER_TABLE_NAME = "users"
        private const val USER_COLUMN_ID = "id"
        private const val USER_COLUMN_USERNAME = "username"
        private const val USER_COLUMN_PASSWORD = "password"
        private const val USER_COLUMN_NAME = "name"
        private const val USER_COLUMN_SURNAME = "surname"
        private const val USER_COLUMN_MOBILE = "mobile"
        private const val USER_COLUMN_EMAIL = "email"

        // Survey Table
        private const val SURVEY_TABLE_NAME = "survey_data"
        private const val SURVEY_COLUMN_ID = "id"
        private const val SURVEY_COLUMN_INCOME = "income"
        private const val SURVEY_COLUMN_MAX_SAVING = "max_saving"
        private const val SURVEY_COLUMN_MIN_SAVING = "min_saving"

        // Category Goals Table
        private const val CATEGORY_GOAL_TABLE_NAME = "category_goals"
        private const val GOAL_COLUMN_ID = "id"
        private const val GOAL_CATEGORY_NAME = "category_name"
        private const val GOAL_AMOUNT = "goal_amount"

        // Transactions Table
        private const val TABLE_TRANSACTIONS = "transactions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_AMOUNT = "amount"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_RECEIPT_URI = "receiptUri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createCategoryTable = """
            CREATE TABLE IF NOT EXISTS $CATEGORY_TABLE_NAME (
                $CATEGORY_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $CATEGORY_COLUMN_NAME TEXT NOT NULL
            )
        """

        val createUserTable = """
            CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME (
                $USER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $USER_COLUMN_USERNAME TEXT,
                $USER_COLUMN_PASSWORD TEXT,
                $USER_COLUMN_NAME TEXT,
                $USER_COLUMN_SURNAME TEXT,
                $USER_COLUMN_MOBILE TEXT,
                $USER_COLUMN_EMAIL TEXT
            )
        """

        val createSurveyTable = """
            CREATE TABLE IF NOT EXISTS $SURVEY_TABLE_NAME (
                $SURVEY_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $SURVEY_COLUMN_INCOME REAL NOT NULL,
                $SURVEY_COLUMN_MAX_SAVING REAL NOT NULL,
                $SURVEY_COLUMN_MIN_SAVING REAL NOT NULL
            )
        """

        val createCategoryGoalTable = """
            CREATE TABLE IF NOT EXISTS $CATEGORY_GOAL_TABLE_NAME (
                $GOAL_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $GOAL_CATEGORY_NAME TEXT NOT NULL,
                $GOAL_AMOUNT REAL NOT NULL
            )
        """

        val createTransactionsTable = """
        CREATE TABLE IF NOT EXISTS transactions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            amount REAL NOT NULL,
            date TEXT NOT NULL
        );
    """.trimIndent()

        db.execSQL(createTransactionsTable)

        db.execSQL(createCategoryTable)
        db.execSQL(createUserTable)
        db.execSQL(createSurveyTable)
        db.execSQL(createCategoryGoalTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS transactions")
        onCreate(db)
    }

    // ---------------- CATEGORY METHODS ----------------
    fun insertCategory(name: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(CATEGORY_COLUMN_NAME, name)
        }
        return db.insert(CATEGORY_TABLE_NAME, null, values) != -1L
    }

    fun getAllCategories(): List<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT DISTINCT $CATEGORY_COLUMN_NAME FROM $CATEGORY_TABLE_NAME", null)
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0))
        }
        cursor.close()
        return list
    }

    // ---------------- USER METHODS ----------------
    fun insertUser(username: String, password: String, name: String, surname: String, mobile: String, email: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(USER_COLUMN_USERNAME, username)
            put(USER_COLUMN_PASSWORD, password)
            put(USER_COLUMN_NAME, name)
            put(USER_COLUMN_SURNAME, surname)
            put(USER_COLUMN_MOBILE, mobile)
            put(USER_COLUMN_EMAIL, email)
        }
        return db.insert(USER_TABLE_NAME, null, values) != -1L
    }

    fun userExists(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM $USER_TABLE_NAME WHERE $USER_COLUMN_USERNAME = ?", arrayOf(username))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun checkLogin(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM $USER_TABLE_NAME WHERE $USER_COLUMN_USERNAME = ? AND $USER_COLUMN_PASSWORD = ?", arrayOf(username, password))
        val valid = cursor.moveToFirst()
        cursor.close()
        return valid
    }

    fun getAllUsers(): List<String> {
        val db = readableDatabase
        val users = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT $USER_COLUMN_USERNAME FROM $USER_TABLE_NAME", null)
        while (cursor.moveToNext()) {
            users.add(cursor.getString(0))
        }
        cursor.close()
        return users
    }

    // ---------------- SURVEY METHODS ----------------
    fun insertSurveyData(income: Double, maxSaving: Double, minSaving: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(SURVEY_COLUMN_INCOME, income)
            put(SURVEY_COLUMN_MAX_SAVING, maxSaving)
            put(SURVEY_COLUMN_MIN_SAVING, minSaving)
        }
        return db.insert(SURVEY_TABLE_NAME, null, values) != -1L
    }

    fun getMonthlyGoalRange(): Pair<Float, Float> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $SURVEY_COLUMN_MIN_SAVING, $SURVEY_COLUMN_MAX_SAVING FROM $SURVEY_TABLE_NAME LIMIT 1", null)

        var minGoal = 1200f
        var maxGoal = 1500f


        cursor.use {
            if (it.moveToFirst()) {
                minGoal = 1200f
                maxGoal = 15500f
                //minGoal = it.getFloat(it.getColumnIndexOrThrow(SURVEY_COLUMN_MIN_SAVING))
                //maxGoal = it.getFloat(it.getColumnIndexOrThrow(SURVEY_COLUMN_MAX_SAVING))
            }
        }

        return Pair(minGoal, maxGoal)
    }


    fun getLatestSurveyData(): Triple<Double, Double, Double>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $SURVEY_COLUMN_INCOME, $SURVEY_COLUMN_MAX_SAVING, $SURVEY_COLUMN_MIN_SAVING FROM $SURVEY_TABLE_NAME ORDER BY $SURVEY_COLUMN_ID DESC LIMIT 1", null)
        return if (cursor.moveToFirst()) {
            val income = cursor.getDouble(0)
            val maxSaving = cursor.getDouble(1)
            val minSaving = cursor.getDouble(2)
            cursor.close()
            Triple(income, maxSaving, minSaving)
        } else {
            cursor.close()
            null
        }
    }

    fun getMaxSavingGoal(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT MAX($SURVEY_COLUMN_MAX_SAVING) FROM $SURVEY_TABLE_NAME", null)
        val maxGoal = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return maxGoal
    }

    // ---------------- GOAL METHODS ----------------
    fun insertCategoryGoal(categoryName: String, goalAmount: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(GOAL_CATEGORY_NAME, categoryName)
            put(GOAL_AMOUNT, goalAmount)
        }
        val result = db.insertWithOnConflict(CATEGORY_GOAL_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        return result != -1L
    }

    fun getGoalAmount(categoryName: String): Double? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $GOAL_AMOUNT FROM $CATEGORY_GOAL_TABLE_NAME WHERE $GOAL_CATEGORY_NAME = ?", arrayOf(categoryName))
        val amount = if (cursor.moveToFirst()) cursor.getDouble(0) else null
        cursor.close()
        return amount
    }

    // ---------------- TRANSACTION METHODS ----------------
    fun addTransaction(transaction: Transaction): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT, transaction.amount)
            put(COLUMN_CATEGORY, transaction.category)
            put(COLUMN_DATE, transaction.date)
            put(COLUMN_RECEIPT_URI, transaction.receiptUri?.toString() ?: "")
        }
        return db.insert(TABLE_TRANSACTIONS, null, values)
    }

//    fun insertImage(imageBytes: ByteArray): Long {
//        val db = this.writableDatabase
//        val values = ContentValues().apply {
//            put("image", imageBytes)
//        }
//        val id = db.insert(TABLE_RECEIPTS, null, values)
//        db.close()
//        return id
//    }

    fun getBalance(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_AMOUNT) FROM $TABLE_TRANSACTIONS", null)
        val balance = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return balance
    }
    fun insertTransaction(category: String, amount: Double, date: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("category", category)
            put("amount", amount)
            put("date", date)
        }
        db.insert("transactions", null, values)
        db.close()
    }


    fun getAllTransactions(): List<Transaction> {
        val db = readableDatabase
        val transactions = mutableListOf<Transaction>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TRANSACTIONS ORDER BY $COLUMN_DATE DESC", null)
        while (cursor.moveToNext()) {
            transactions.add(
                Transaction(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    receiptUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEIPT_URI))?.let { android.net.Uri.parse(it) }
                )
            )
        }
        cursor.close()
        return transactions
    }

    fun getCategoryTotalsBetweenDates(startDate: String, endDate: String): Map<String, Double> {
        val db = readableDatabase
        val totals = mutableMapOf<String, Double>()
        val cursor = db.rawQuery(
            "SELECT $COLUMN_CATEGORY, SUM($COLUMN_AMOUNT) as total FROM $TABLE_TRANSACTIONS WHERE $COLUMN_DATE BETWEEN ? AND ? GROUP BY $COLUMN_CATEGORY",
            arrayOf(startDate, endDate)
        )
        while (cursor.moveToNext()) {
            val category = cursor.getString(0)
            val total = cursor.getDouble(1)
            totals[category] = total
        }
        cursor.close()
        return totals
    }
}



//package com.example.prog7313pocketplanpoe
//
//
//
//
//import android.content.ContentValues
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import android.widget.Toast
//import com.example.pocketplan.Transaction
//
//
//class PocketPlanDBHelper(context: Context) :
//
//    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    companion object {
//        private const val DATABASE_NAME = "PocketPlan.db"
//        private const val DATABASE_VERSION = 2
//
//        // Table: Categories
//        private const val CATEGORY_TABLE_NAME = "selected_categories"
//        private const val CATEGORY_COLUMN_ID = "id"
//        private const val CATEGORY_COLUMN_NAME = "name"
//
//        // Table: Users
//        private const val USER_TABLE_NAME = "users"
//        private const val USER_COLUMN_ID = "id"
//        private const val USER_COLUMN_USERNAME = "username"
//        private const val USER_COLUMN_PASSWORD = "password"
//        private const val USER_COLUMN_NAME = "name"
//        private const val USER_COLUMN_SURNAME = "surname"
//        private const val USER_COLUMN_MOBILE = "mobile"
//        private const val USER_COLUMN_EMAIL = "email"
//
//        // Table: Survey
//        private const val SURVEY_TABLE_NAME = "survey_data"
//        private const val SURVEY_COLUMN_ID = "id"
//        private const val SURVEY_COLUMN_INCOME = "income"
//        private const val SURVEY_COLUMN_MAX_SAVING = "max_saving"
//        private const val SURVEY_COLUMN_MIN_SAVING = "min_saving"
//
//        // Table: Category Goal Amount
//        private const val CATEGORY_GOAL_TABLE_NAME = "categories_goal"
//        private const val GOAL_COLUMN_ID = "id"
//        private const val CATEGORY_NAME_COLUMN = "category_name"
//        private const val GOAL_AMOUNT_COLUMN = "goal_amount"
//
//        //Table: Transactions
//        private const val TABLE_TRANSACTIONS = "transactions"
//        private const val COLUMN_ID = "id"
//        private const val COLUMN_AMOUNT = "amount"
//        private const val COLUMN_CATEGORY = "category"
//        private const val COLUMN_DATE = "date"
//        private const val COLUMN_RECEIPT_URI = "receiptUri"
//
//    }
//
//    override fun onCreate(db: SQLiteDatabase) {
//        val createCategoryTable = """
//            CREATE TABLE IF NOT EXISTS $CATEGORY_TABLE_NAME (
//                $CATEGORY_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
//                $CATEGORY_COLUMN_NAME TEXT NOT NULL
//            )
//        """
//
//        val createUserTable = """
//            CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME (
//                $USER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
//                $USER_COLUMN_USERNAME TEXT,
//                $USER_COLUMN_PASSWORD TEXT,
//                $USER_COLUMN_NAME TEXT,
//                $USER_COLUMN_SURNAME TEXT,
//                $USER_COLUMN_MOBILE TEXT,
//                $USER_COLUMN_EMAIL TEXT
//            )
//        """
//
//        val createSurveyTable = """
//    CREATE TABLE IF NOT EXISTS $SURVEY_TABLE_NAME (
//        $SURVEY_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
//        $SURVEY_COLUMN_INCOME REAL NOT NULL,
//        $SURVEY_COLUMN_MAX_SAVING REAL NOT NULL,
//        $SURVEY_COLUMN_MIN_SAVING REAL NOT NULL
//    )
//"""
//
//        val createCategoryGoalTable ="""
//    CREATE TABLE IF NOT EXISTS $CATEGORY_GOAL_TABLE_NAME (
//        $GOAL_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
//        $CATEGORY_NAME_COLUMN TEXT NOT NULL,
//        $GOAL_AMOUNT_COLUMN REAL NOT NULL
//    )
//"""
//        val createTransactionTable = """
//            CREATE TABLE $TABLE_TRANSACTIONS (
//                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
//                $COLUMN_AMOUNT REAL NOT NULL,
//                $COLUMN_CATEGORY TEXT NOT NULL,
//                $COLUMN_DATE TEXT NOT NULL,
//                $COLUMN_RECEIPT_URI TEXT
//            );
//        """
//
//        db.execSQL(createTransactionTable)
//        db.execSQL(createCategoryGoalTable)
//        db.execSQL(createSurveyTable)
//        db.execSQL(createCategoryTable)
//        db.execSQL(createUserTable)
//    }
//
//
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS $CATEGORY_TABLE_NAME")
//        db.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
//        db.execSQL("DROP TABLE IF EXISTS $SURVEY_TABLE_NAME")
//        db.execSQL("DROP TABLE IF EXISTS $CATEGORY_GOAL_TABLE_NAME")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
//        onCreate(db)
//    }
//
//    // --- CATEGORY METHODS ---
//
//    fun insertCategories(name: String): Boolean {
//        val db = this.writableDatabase
//        val contentValues = ContentValues().apply {
//            put("name", name)
//        }
//        val result = db.insert("selected_categories", null, contentValues)
//        return result != -1L
//    }
//
//
//    fun getAllCategories(): List<String> {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT DISTINCT name FROM selected_categories", null)
//        val list = mutableListOf<String>()
//        while (cursor.moveToNext()) {
//            list.add(cursor.getString(0))
//        }
//        cursor.close()
//        return list
//    }
//
//
//
//
//    // --- USER METHODS ---
//    fun insertUser(username: String, password: String, name: String, surname: String, mobile: String, email: String): Boolean {
//        val db = this.writableDatabase
//
//        val contentValues = ContentValues().apply {
//            put(USER_COLUMN_USERNAME, username)
//            put(USER_COLUMN_PASSWORD, password)
//            put(USER_COLUMN_NAME, name)
//            put(USER_COLUMN_SURNAME, surname)
//            put(USER_COLUMN_MOBILE, mobile)
//            put(USER_COLUMN_EMAIL, email)
//        }
//        val result = db.insert(USER_TABLE_NAME, null, contentValues)
//        return result != -1L
//
//    }
//
//    fun insertImage(image: ByteArray): Long {
//        val db = writableDatabase
//        val values = ContentValues().apply {
//            put("image_column", image) // Replace with your actual column name
//        }
//        return db.insert("receipt_table", null, values) // Replace with your actual table
//    }
//
//
//    fun userExists(username: String): Boolean {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT * FROM $USER_TABLE_NAME WHERE $USER_COLUMN_USERNAME = ?",
//            arrayOf(username)
//        )
//        val exists = cursor.moveToFirst()
//        cursor.close()
//        return exists
//    }
//
//    fun checkLogin(username: String, password: String): Boolean {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT * FROM $USER_TABLE_NAME WHERE $USER_COLUMN_USERNAME = ? AND $USER_COLUMN_PASSWORD = ?",
//            arrayOf(username, password)
//        )
//        val valid = cursor.moveToFirst()
//        cursor.close()
//        return valid
//    }
//
//    fun getAllUsers(): List<String> {
//        val db = readableDatabase
//        val users = mutableListOf<String>()
//        val cursor = db.rawQuery("SELECT username FROM users", null)
//        if (cursor.moveToFirst()) {
//            do {
//                users.add(cursor.getString(0))
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        db.close()
//        return users
//    }
//    fun insertSurveyData(income: Double, maxSaving: Double, minSaving: Double): Boolean {
//        val db = this.writableDatabase
//        val contentValues = ContentValues().apply {
//            put(SURVEY_COLUMN_INCOME, income)
//            put(SURVEY_COLUMN_MAX_SAVING, maxSaving)
//            put(SURVEY_COLUMN_MIN_SAVING, minSaving)
//        }
//        val result = db.insert(SURVEY_TABLE_NAME, null, contentValues)
//        return result != -1L
//    }
//
//    fun getLatestSurveyData(): Triple<Double, Double, Double>? {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT $SURVEY_COLUMN_INCOME, $SURVEY_COLUMN_MAX_SAVING, $SURVEY_COLUMN_MIN_SAVING FROM $SURVEY_TABLE_NAME ORDER BY $SURVEY_COLUMN_ID DESC LIMIT 1",
//            null
//        )
//        return if (cursor.moveToFirst()) {
//            val income = cursor.getDouble(0)
//            val maxSaving = cursor.getDouble(1)
//            val minSaving = cursor.getDouble(2)
//            cursor.close()
//            Triple(income, maxSaving, minSaving)
//        } else {
//            cursor.close()
//            null
//        }
//
//    }
//
//    fun insertCategoryGoal(categoryName: String, goalAmount: Double): Boolean {
//        val db = this.writableDatabase
//        val contentValues = ContentValues().apply {
//            put("category_name", categoryName)
//            put("goal_amount", goalAmount)
//        }
//
//        val result = db.insertWithOnConflict("category_goal", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
//        db.close()
//        return result != -1L
//    }
//
//    fun getGoalAmount(categoryName: String): Double? {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT goal_amount FROM category_goals WHERE category_name = ?",
//            arrayOf(categoryName)
//        )
//
//        var amount: Double? = null
//        if (cursor.moveToFirst()) {
//            amount = cursor.getDouble(0)
//        }
//
//        cursor.close()
//        db.close()
//        return amount
//    }
//
//    fun insertOrUpdateGoal(category: String, amount: String) {
//        val db = writableDatabase
//        val stmt = db.compileStatement("INSERT OR REPLACE INTO goals (category, amount) VALUES (?, ?)")
//        stmt.bindString(1, category)
//        stmt.bindString(2, amount)
//        stmt.execute()
//        stmt.close()
//    }
//
//    fun getGoalForCategory(category: String): String {
//        val db = readableDatabase
//        val cursor = db.rawQuery("SELECT amount FROM goals WHERE category = ?", arrayOf(category))
//        var goal = ""
//        if (cursor.moveToFirst()) {
//            goal = cursor.getString(0)
//        }
//        cursor.close()
//        return goal
//    }
//
//    fun getMaxSavingGoal(): Double {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT max_Saving FROM survey_data", null)
//        var maxGoal = 0.0
//
//        if (cursor.moveToFirst()) {
//            maxGoal = cursor.getDouble(0)
//        }
//        cursor.close()
//        db.close()
//        return maxGoal
//    }
//    fun getCategoriesBetweenDates(startMillis: Long, endMillis: Long): List<String> {
//        val db = readableDatabase
//        val categories = mutableListOf<String>()
//
//        val cursor = db.rawQuery(
//            "SELECT DISTINCT category FROM survey WHERE dateMillis BETWEEN ? AND ?",
//            arrayOf(startMillis.toString(), endMillis.toString())
//        )
//
//        while (cursor.moveToNext()) {
//            categories.add(cursor.getString(0))
//        }
//
//        cursor.close()
//        db.close()
//        return categories
//    }
//    fun getCategoryTotalsBetweenDates(startDate: String, endDate: String): Map<String, Double> {
//        val totals = mutableMapOf<String, Double>()
//        val db = readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT category, SUM(amount) as total FROM transactions WHERE date BETWEEN ? AND ? GROUP BY category",
//            arrayOf(startDate, endDate)
//        )
//
//        while (cursor.moveToNext()) {
//            val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
//            val total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"))
//            totals[category] = total
//        }
//        cursor.close()
//        return totals
//    }
//
//    fun addTransaction(transaction: Transaction): Long {
//        val db = writableDatabase
//        val values = ContentValues().apply {
//            put(COLUMN_AMOUNT, transaction.amount)
//            put(COLUMN_CATEGORY, transaction.category)
//            put(COLUMN_DATE, transaction.date)
//            put(COLUMN_RECEIPT_URI, transaction.receiptUri?.toString() ?: "")
//        }
//
//        return db.insert(TABLE_TRANSACTIONS, null, values)
//    }
//
//    fun getBalance(): Double {
//        val db = readableDatabase
//        val cursor = db.rawQuery("SELECT SUM(amount) FROM $TABLE_TRANSACTIONS", null)
//        var balance = 0.0
//        if (cursor.moveToFirst()) {
//            balance = cursor.getDouble(0)
//        }
//        cursor.close()
//        return balance
//    }
//
//    // Optional: Get all transactions
//    fun getAllTransactions(): List<Transaction> {
//        val transactions = mutableListOf<Transaction>()
//        val db = readableDatabase
//        val cursor = db.query(TABLE_TRANSACTIONS, null, null, null, null, null, "$COLUMN_DATE DESC")
//
//        while (cursor.moveToNext()) {
//            val transaction = Transaction(
//                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),  // <-- Changed here
//                amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
//                category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
//                date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
//                receiptUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEIPT_URI))?.let {
//                    if (it.isNotEmpty()) android.net.Uri.parse(it) else null
//                }
//            )
//            transactions.add(transaction)
//        }
//        cursor.close()
//        return transactions
//    }
//
//
//}





