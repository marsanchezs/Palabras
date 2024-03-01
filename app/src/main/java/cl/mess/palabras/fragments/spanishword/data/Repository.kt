package cl.mess.palabras.fragments.spanishword.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import cl.mess.palabras.BuildConfig
import cl.mess.palabras.data.Constants.CONDITION
import cl.mess.palabras.data.Constants.CONTENT_VALUES
import cl.mess.palabras.data.Constants.DATABASE_NAME
import cl.mess.palabras.data.Constants.DATABASE_VERSION
import cl.mess.palabras.data.Constants.DATE
import cl.mess.palabras.data.Constants.ERROR
import cl.mess.palabras.data.Constants.MEANING
import cl.mess.palabras.data.Constants.OK
import cl.mess.palabras.data.Constants.QUERY
import cl.mess.palabras.data.Constants.TABLE_WORD_SPANISH
import cl.mess.palabras.data.Constants.WORD
import cl.mess.palabras.data.SQLiteOpenHelper
import cl.mess.palabras.fragments.spanishword.data.model.SpanishWord

class Repository {
    fun addSpanishWord(context: Context, spanishWord: SpanishWord): String {
        val response: String = OK
        val adm = SQLiteOpenHelper(
            context = context,
            name = DATABASE_NAME,
            factory = null,
            version = DATABASE_VERSION
        )
        val date = spanishWord.date.replace("'", "\\'")
        val word = spanishWord.word.replace("'", "''")
        val meaning = spanishWord.meaning.replace("'", "''")

        try {
            val db = adm.writableDatabase
            val cvWord = ContentValues().apply {
                put(DATE, date)
                put(WORD, word)
                put(MEANING, meaning)
            }
            db.insert(TABLE_WORD_SPANISH, null, cvWord)
            println("$CONTENT_VALUES $cvWord")

        } catch (e: Exception) {
            e.message?.let { exception ->
                if (BuildConfig.DEBUG) {
                    Log.e(ERROR, exception)
                }
            }
            return e.message ?: OK
        }
        return response
    }

    fun deleteSpanishWord(context: Context, spanishWord: SpanishWord): String {
        var response = OK
        val adm = SQLiteOpenHelper(
            context = context,
            name = DATABASE_NAME,
            factory = null,
            version = DATABASE_VERSION
        )
        val date = spanishWord.date.replace("'", "\\'")
        val word = spanishWord.word.replace("'", "''")
        val meaning = spanishWord.meaning.replace("'", "''")
        val query =
            "DELETE FROM $TABLE_WORD_SPANISH WHERE FECHA = '$date' AND PALABRA = '$word' AND SIGNIFICADO = '$meaning'"

        try {
            val db = adm.writableDatabase
            db.execSQL(query)
            Log.e(QUERY, query)
        } catch (e: Exception) {
            e.message?.let { exception ->
                if (BuildConfig.DEBUG) {
                    Log.e(ERROR, exception)
                }
            }
            response = e.message ?: OK
        }
        return response
    }

    fun getAllSpanishWords(context: Context): ArrayList<SpanishWord> {
        val adm = SQLiteOpenHelper(
            context = context,
            name = DATABASE_NAME,
            factory = null,
            version = DATABASE_VERSION
        )
        val db = adm.writableDatabase
        val words = ArrayList<SpanishWord>()
        val query = "SELECT DISTINCT FECHA, PALABRA, SIGNIFICADO FROM PALABRA ORDER BY PALABRA"

        var row: Cursor? = null
        try {
            Log.e(QUERY, query)
            row = db.rawQuery(query, null)
            if (row.moveToFirst()) {
                do {
                    val word = SpanishWord(
                        date = row.getString(0),
                        word = row.getString(1),
                        meaning = row.getString(2)
                    )
                    words.add(word)
                } while (row.moveToNext())
            }
        } catch (e: Exception) {
            e.message?.let { exception ->
                if (BuildConfig.DEBUG) {
                    Log.e(ERROR, exception)
                }
            }
        } finally {
            row?.close()
            db.close()
        }
        return words
    }

    fun updateSpanishWord(
        context: Context,
        spanishWord: SpanishWord,
        newSpanishWord: SpanishWord
    ): String {
        var response = OK
        val date = spanishWord.date.replace("'", "\\'")
        val word = spanishWord.word.replace("'", "''")
        val meaning = spanishWord.meaning.replace("'", "''")
        val condition = "FECHA = '$date' AND PALABRA = '$word' AND SIGNIFICADO = '$meaning'"
        Log.e(CONDITION, condition)
        val adm = SQLiteOpenHelper(
            context = context,
            name = DATABASE_NAME,
            factory = null,
            version = DATABASE_VERSION
        )
        val newDate = newSpanishWord.date.replace("'", "\\'")
        val newWord = newSpanishWord.word.replace("'", "''")
        val newMeaning = newSpanishWord.meaning.replace("'", "''")

        try {
            val bd = adm.writableDatabase
            val cvWord = ContentValues().apply {
                put("FECHA", newDate)
                put("PALABRA", newWord)
                put("SIGNIFICADO", newMeaning)
            }
            println("$CONTENT_VALUES$cvWord")
            bd.update(TABLE_WORD_SPANISH, cvWord, condition, null)
        } catch (e: Exception) {
            e.message?.let { exception ->
                if (BuildConfig.DEBUG) {
                    Log.e(ERROR, exception)
                }
            }
            response = e.message ?: OK
        }
        return response
    }

    fun validateSpanishWord(context: Context, spanishWord: SpanishWord): Boolean {
        var response: Boolean
        val adm = SQLiteOpenHelper(
            context = context,
            name = DATABASE_NAME,
            factory = null,
            version = DATABASE_VERSION
        )
        val db = adm.writableDatabase

        val word = spanishWord.word.replace("'", "''")
        val meaning = spanishWord.meaning.replace("'", "''")

        val query = "SELECT * FROM PALABRA WHERE PALABRA = '$word' AND SIGNIFICADO = '$meaning'"

        var row: Cursor? = null
        try {
            Log.e(QUERY, query)
            row = db.rawQuery(query, null)
            response = row.count > 0
        } catch (e: Exception) {
            e.message?.let { exception ->
                if (BuildConfig.DEBUG) {
                    Log.e(ERROR, exception)
                }
            }
            response = false
        } finally {
            row?.close()
            db.close()
        }
        return response
    }
}
