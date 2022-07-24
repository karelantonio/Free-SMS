/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cu.kareldv.android.freesms.view.dialogs.ProxyInfo
import java.net.Proxy

class ProxiesDatabase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (id PRIMARY KEY AUTOINCREMENT, host TEXT, port INTEGER, type TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insert(proxEntity: ProxyEntity) {
        val db = writableDatabase
        try {
            db.insert(TABLE_NAME, null, proxEntity.toContentValues())
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
        val ROW_1 = "host"
        val ROW_2 = "port"
        val ROW_3 = "type"
    }
}

data class ProxyEntity(
    val id: Int,
    val host: String,
    val port: Int,
    val type: Proxy.Type,
)

fun ProxyEntity.toContentValues(): ContentValues {
    val cv = ContentValues()
    cv.put(ProxiesDatabase.ROW_1, host)
    cv.put(ProxiesDatabase.ROW_2, port)
    cv.put(ProxiesDatabase.ROW_3, type.toString())
    return cv
}

fun ProxyEntity.toProxyInfo(): ProxyInfo = ProxyInfo(
    host = host,
    port = port,
    type = type
)

fun ProxyInfo.toProxyEntity(): ProxyEntity = ProxyEntity(
    id = 0,
    host = host,
    port = port,
    type = type
)