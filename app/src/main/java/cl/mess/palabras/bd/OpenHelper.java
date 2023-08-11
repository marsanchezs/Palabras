package cl.mess.palabras.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OpenHelper extends android.database.sqlite.SQLiteOpenHelper {
    public OpenHelper(Context contexto, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, name, factory, version);
        Context miContexto = contexto;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TABLA PALABRA
        db.execSQL("CREATE TABLE PALABRA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, PALABRA TEXT, SIGNIFICADO TEXT) ");

        //TABLA CITA
        db.execSQL("CREATE TABLE CITA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, CITA TEXT, AUTOR TEXT) ");

        //TABLA AUTOR
        db.execSQL("CREATE TABLE AUTOR (Id INTEGER PRIMARY KEY AUTOINCREMENT, AUTOR TEXT) ");

        //TABLA WORD
        db.execSQL("CREATE TABLE WORD (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, WORD TEXT, TRADUCCION TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
