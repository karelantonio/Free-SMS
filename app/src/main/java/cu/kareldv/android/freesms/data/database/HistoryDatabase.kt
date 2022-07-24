/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HistoryDatabase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (id TEXT UNIQUE, phone TEXT, message TEXT, last_known_state TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insert(historyEntity: HistoryEntity) {
        val db = writableDatabase
        try {
            db.insert(TABLE_NAME, null, historyEntity.toContentValues())
        } finally {
            db.close()
        }
    }

    fun remove(id: String) {
        val db = writableDatabase
        try {
            db.delete(TABLE_NAME, "id=?", arrayOf(id))
        } finally {
            db.close()
        }
    }

    fun clearAll() {
        val db = writableDatabase
        try {
            db.delete(TABLE_NAME, null, emptyArray())
        } finally {
            db.close()
        }
    }

    companion object {
        val DB_NAME = "history.db"
        val TABLE_NAME = "history"
        val ROW_0 = "id"
        val ROW_1 = "phone"
        val ROW_2 = "message"
        val ROW_3 = "last_known_state"
    }
}