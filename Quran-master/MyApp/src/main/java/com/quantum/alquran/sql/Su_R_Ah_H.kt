package com.quantum.alquran.sql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.quantum.alquran.R
import com.quantum.alquran.model.Su_R_Ah_Li

class Su_R_Ah_H(val context: Context) :
    SQLiteOpenHelper(context, "Su_r_ahList_Surah.db", null, 1) {

    val names = context.resources.getStringArray(R.array.surah_name)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            ("CREATE TABLE "
                    + "Surah"
                    ) + " (id INTEGER PRIMARY KEY, " +
                    "name TEXT, revelation TEXT, " +
                    "verse INTEGER, name_arabic TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Surah")
        onCreate(db)
    }

    fun insertData(data: ArrayList<Su_R_Ah_Li>): Boolean {
        val db = this.writableDatabase
        for (x in data) {
            val contentValues = ContentValues()
            contentValues.put("id", x.pos)
            contentValues.put("name", x.name)
            contentValues.put("revelation", x.revelation)
            contentValues.put("verse", x.verse)
            contentValues.put("name_arabic", x.nameAr)
            db.insert("Surah", null, contentValues)
        }
        return true
    }

    fun readDataAt(pos: Int): Su_R_Ah_Li? {
        var data: Su_R_Ah_Li? = null
        val db = this.readableDatabase
        val selectQuery = "SELECT  * FROM Surah WHERE id == $pos"
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                data = Su_R_Ah_Li(
                    pos = cursor.getInt(0),
                    name =  names[pos-1], //cursor.getString(1),
                    revelation = cursor.getString(2),
                    verse = cursor.getInt(3),
                    nameAr = cursor.getString(4)
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return data
    }

    fun readData(): ArrayList<Su_R_Ah_Li> {
        val data = ArrayList<Su_R_Ah_Li>()
        val db = this.readableDatabase
        val selectQuery = "SELECT  * FROM Surah"
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                data.add(
                    Su_R_Ah_Li(
                        pos = cursor.getInt(0),
                        name = names[data.size],//cursor.getString(1),
                        revelation = cursor.getString(2),
                        verse = cursor.getInt(3),
                        nameAr = cursor.getString(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return data
    }
}