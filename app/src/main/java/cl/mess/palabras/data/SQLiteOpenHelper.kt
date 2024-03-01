package cl.mess.palabras.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteOpenHelper(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        //TABLA PALABRA
        db.execSQL("CREATE TABLE PALABRA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, PALABRA TEXT, SIGNIFICADO TEXT) ")

        //TABLA CITA
        db.execSQL("CREATE TABLE CITA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, CITA TEXT, AUTOR TEXT) ")

        //TABLA AUTOR
        db.execSQL("CREATE TABLE AUTOR (Id INTEGER PRIMARY KEY AUTOINCREMENT, AUTOR TEXT) ")

        //TABLA WORD
        db.execSQL("CREATE TABLE WORD (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, WORD TEXT, TRADUCCION TEXT) ")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { }
}
