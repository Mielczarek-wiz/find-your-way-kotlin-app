package com.example.findyourway

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DataBase(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,
    null, DATABASE_VER) {
    companion object {
        private val DATABASE_VER = 7
        private val DATABASE_NAME = "Roads.db"

        //Table roads
        private val TABLE_NAME_ROADS = "ROADS"
        private val COL_ID = "ID"
        private val COL_NAME = "NAME"
        private val COL_ROAD = "ROAD"
        private val COL_LENGHT = "LENGHT"
        private val COL_DES = "DESCRIPTION"
        private val COL_TYPE = "TYPE"
        //Table
        private val TABLE_NAME_TIMES = "TIMES"
        private val COL_ID_TIME = "ID_TIME"
        private val COL_DATE = "DATE"
        private val COL_TIME = "TIME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME_ROADS " +
                "($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_LENGHT INTEGER," +
                " $COL_DES TEXT, $COL_NAME TEXT UNIQUE, $COL_ROAD TEXT, $COL_TYPE TEXT)")
        val CREATE_TABLE_2_QUERY = ("CREATE TABLE $TABLE_NAME_TIMES" +
                " ($COL_ID_TIME INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COL_DATE DATE, $COL_TIME INTEGER, $COL_ID INTEGER)")
        db!!.execSQL(CREATE_TABLE_QUERY)
        db!!.execSQL(CREATE_TABLE_2_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_TIMES")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ROADS")
        onCreate(db!!)
    }


    @SuppressLint("NewApi")
    fun addTime(time: Int, id: Long): Long {
        val id= id+1
        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val formatedDate = sdf.format(date)
        val db= this.writableDatabase
        val values = ContentValues()
        values.put(COL_DATE, formatedDate)
        values.put(COL_TIME, time)
        values.put(COL_ID, id)
        val result = db.insert(TABLE_NAME_TIMES, null, values)
        return result
    }
    @SuppressLint("Range")
    fun getShortestTime(id:Long): String {
        val id= id+1
        val selectQuery="SELECT $COL_DATE, $COL_TIME FROM " +
                "$TABLE_NAME_TIMES WHERE $COL_ID = '$id' ORDER BY $COL_TIME"

        val db= this.readableDatabase
        val cursor : Cursor?
        try {
            cursor =  db.rawQuery(selectQuery, null)

        } catch(e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ""
        }
        var time: String ="None"
        if(cursor.moveToFirst()) {
            val secs= cursor.getString(cursor.getColumnIndex(COL_TIME)).toInt()
            val seconds: Int = (secs % 60)
            val minutes: Int = (secs % 3600) / 60
            val hours: Int = secs / 3600
            val shortestTime = hours.toString().plus(":").plus(minutes.toString())
                .plus(":").plus(seconds.toString())
            time="DATE: ".plus(cursor.getString(cursor.getColumnIndex(COL_DATE)).toString())
                .plus(" TIME: ").plus(shortestTime)
        }
        db.close()
        return time
    }
    @SuppressLint("Range")
    fun getLastTime(id:Long): String{
        val id= id+1
        val selectQuery="SELECT $COL_DATE, $COL_TIME FROM $TABLE_NAME_TIMES WHERE" +
                " $COL_ID = '$id' ORDER BY $COL_ID_TIME DESC"

        val db= this.readableDatabase
        val cursor : Cursor?
        try {
            cursor =  db.rawQuery(selectQuery, null)

        } catch(e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ""
        }
        var time: String ="None"
        if(cursor.moveToFirst()) {
            val secs= cursor.getString(cursor.getColumnIndex(COL_TIME)).toInt()
            val seconds: Int = (secs % 60)
            val minutes: Int = (secs % 3600) / 60
            val hours: Int = secs / 3600
            val lastTime = hours.toString().plus(":")
                .plus(minutes.toString()).plus(":")
                .plus(seconds.toString())
            time="DATE: ".plus(cursor.getString(cursor.getColumnIndex(COL_DATE)).toString())
                .plus(" TIME: ").plus(lastTime)
        }
        db.close()
        return time
    }
    fun addRoad(name: String, road: String, lenght: Int, des: String, type: String): Long {
        val db= this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, name)
        values.put(COL_ROAD, road)
        values.put(COL_LENGHT, lenght)
        values.put(COL_DES, des)
        values.put(COL_TYPE, type)

        val selectQuery="SELECT * FROM $TABLE_NAME_ROADS WHERE $COL_NAME=?";

        val cursor = db.rawQuery(selectQuery, arrayOf(name))
        if(cursor.moveToFirst()){
            db.close()
            return 0
        } else {
            val result = db.insert(TABLE_NAME_ROADS, null, values)
            db.close()
            return result
        }
    }
    @SuppressLint("Range")
    fun getNames(type: String): ArrayList<String>{
        val names: ArrayList<String> = ArrayList()
        val selectQuery="SELECT $COL_NAME FROM $TABLE_NAME_ROADS WHERE $COL_TYPE= ? " +
                "ORDER BY $COL_ID"
        val db= this.readableDatabase
        val cursor : Cursor?
        try {
            cursor =  db.rawQuery(selectQuery, arrayOf(type))

        } catch(e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if(cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(cursor.getColumnIndex(COL_NAME)).toString())
            } while (cursor.moveToNext());
        }
        db.close()
        return names
    }
    @SuppressLint("Range")
    fun getRoad(id: Long): String{
        val id = id +1
        val selectQuery="SELECT $COL_NAME, $COL_ROAD, " +
                "$COL_LENGHT, $COL_DES FROM $TABLE_NAME_ROADS WHERE $COL_ID = '$id'"
        val db= this.readableDatabase
        val cursor : Cursor?
        try {
            cursor =  db.rawQuery(selectQuery, null)
        } catch(e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return "Failure"
        }
        var result = ""

        if(cursor.moveToFirst()) {
            do {
                result = "TRASA: ".plus(cursor.getString(cursor.getColumnIndex(COL_NAME))
                    .plus("\n" + "\n" + "\n" + "\n" + "\n" + "\n\n\nPRZEBIEG:\n")
                    .plus(cursor.getString(cursor.getColumnIndex(COL_ROAD)).toString())
                    .plus("\n\nDLUGOSC: ").plus(cursor.getInt(cursor.getColumnIndex(
                    COL_LENGHT)).toString())
                    .plus("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nOPIS: ")
                    .plus(cursor.getString(cursor.getColumnIndex(COL_DES))))
            } while (cursor.moveToNext());
        }
        db.close()
        return result
    }
    @SuppressLint("Range")
    fun getName(id: Long): String{
        val id = id +1
        val selectQuery="SELECT $COL_NAME FROM $TABLE_NAME_ROADS WHERE $COL_ID = '$id'"
        val db= this.readableDatabase
        val cursor : Cursor?
        try {
            cursor =  db.rawQuery(selectQuery, null)
        } catch(e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return "Failure"
        }
        var result = ""

        if(cursor.moveToFirst()) {
            do {
                result = (cursor.getString(cursor.getColumnIndex(COL_NAME)).toString())
            } while (cursor.moveToNext());
        }
        db.close()
        return result
    }
    @SuppressLint("Range")
    fun getId(type:String): ArrayList<Long> {
        val idks: ArrayList<Long> = ArrayList()
        val selectQuery="SELECT $COL_ID FROM $TABLE_NAME_ROADS WHERE $COL_TYPE = ?"
        val db= this.readableDatabase
        val cursor : Cursor?
        try {
            cursor =  db.rawQuery(selectQuery, arrayOf(type))

        } catch(e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return idks
        }

        if(cursor.moveToFirst()) {
            do {
                idks.add(cursor.getLong(cursor.getColumnIndex(COL_ID)))
            } while (cursor.moveToNext());
        }
        db.close()
        return idks
    }
}