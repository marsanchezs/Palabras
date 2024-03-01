package cl.mess.palabras.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import cl.mess.palabras.model.Quote;
import cl.mess.palabras.fragments.spanishword.data.model.SpanishWord;
import cl.mess.palabras.model.WordSpanish;
import cl.mess.palabras.model.WordEnglish;

public class Delegate {
    private static final String DB_NAME = Constants.DB_NAME;
    private static final int DB_VERSION = Constants.DB_VERSION;
    private final String QUERY = "QUERY";
    private final String ERROR = "ERROR";
    private final String CONTENT_VALUES = "CONTENT VALUES";
    private final String CONDITION = "CONDITION";
    private final String TABLE_WORD_SPANISH = "PALABRA";
    private final String TABLE_WORD_ENGLISH = "WORD";
    private final String TABLE_QUOTE = "CITA";
    private final String TABLE_AUTHOR = "AUTOR";

    public ArrayList<WordSpanish> getAllWordsSpanish(Context context) {
        ///TABLA PALABRA
        //db.execSQL("CREATE TABLE PALABRA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, PALABRA TEXT, SIGNIFICADO TEXT) ");
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = adm.getWritableDatabase();
        ArrayList<WordSpanish> words = new ArrayList<>();
        String query = "SELECT DISTINCT FECHA, PALABRA, SIGNIFICADO FROM PALABRA ORDER BY PALABRA";

        Cursor row = null;
        try {
            Log.e(QUERY, query);
            row = db.rawQuery(query, null);
            if (row.moveToFirst()) {
                do {
                    WordSpanish word = new WordSpanish();
                    word.setDate(row.getString(0));
                    word.setWord(row.getString(1));
                    word.setMeaning(row.getString(2));
                    words.add(word);
                } while (row.moveToNext());
            }
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
        } finally {
            assert row != null;
            row.close();
            db.close();
        }
        return words;
    }

    public Boolean validateWordSpanish(Context context, SpanishWord spanishWord) {
        ///TABLA PALABRA
        //db.execSQL("CREATE TABLE PALABRA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, PALABRA TEXT, SIGNIFICADO TEXT) ");
        boolean response = false;
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = adm.getWritableDatabase();

        String word = spanishWord.getWord().replaceAll("'", "''");
        String meaning = spanishWord.getMeaning().replaceAll("'", "''");

        String query = "SELECT * FROM PALABRA WHERE PALABRA = '" + word + "' AND SIGNIFICADO = '" + meaning + "'";

        Cursor row = null;
        try {
            Log.e(QUERY, query);
            row = db.rawQuery(query, null);
            if (row.getCount() > 0) {
                response = true;
            }
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
        } finally {
            assert row != null;
            row.close();
            db.close();
        }
        return response;
    }

    public String addWordSpanish(Context context, SpanishWord spanishWord) {
        //TABLA PALABRA
        //db.execSQL("CREATE TABLE PALABRA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, PALABRA TEXT, SIGNIFICADO TEXT) ");
        String response = "OK";
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String date = spanishWord.getDate().replaceAll("'", "\\'");
        String word = spanishWord.getWord().replaceAll("'", "''");
        String meaning = spanishWord.getMeaning().replaceAll("'", "''");

        try (SQLiteDatabase db = adm.getWritableDatabase()) {
            ContentValues cvWord = new ContentValues();
            cvWord.put("FECHA", date);
            cvWord.put("PALABRA", word);
            cvWord.put("SIGNIFICADO", meaning);
            db.insert(TABLE_WORD_SPANISH, null, cvWord);
            System.out.println(CONTENT_VALUES + cvWord);

        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    public String deleteWordSpanish(Context context, SpanishWord spanishWord) {
        //TABLA PALABRA
        //db.execSQL("CREATE TABLE PALABRA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, PALABRA TEXT, SIGNIFICADO TEXT) ");
        String response = "OK";
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String date = spanishWord.getDate().replaceAll("'", "\\'");
        String word = spanishWord.getWord().replaceAll("'", "''");
        String meaning = spanishWord.getMeaning().replaceAll("'", "''");
        String query = "DELETE FROM PALABRA WHERE FECHA = '" + date + "' AND PALABRA = '" + word + "' " +
                "AND SIGNIFICADO = '" + meaning + "'";
        try (SQLiteDatabase db = adm.getWritableDatabase()) {
            db.execSQL(query);
            Log.e(QUERY, query);
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    public String updateWordSpanish(Context context, SpanishWord spanishWord, SpanishWord newWordSpanish) {
        //TABLA PALABRA
        //db.execSQL("CREATE TABLE PALABRA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, PALABRA TEXT, SIGNIFICADO TEXT) ");
        String response = "OK";
        String date = spanishWord.getDate().replaceAll("'", "\\'");
        String word = spanishWord.getWord().replaceAll("'", "''");
        String meaning = spanishWord.getMeaning().replaceAll("'", "''");
        String condition = "FECHA = '" + date + "' AND PALABRA = '" + word + "' AND SIGNIFICADO = '" + meaning + "'";
        Log.e(CONDITION, condition);
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String newDate = newWordSpanish.getDate().replaceAll("'", "\\'");
        String newWord = newWordSpanish.getWord().replaceAll("'", "''");
        String newMeaning = newWordSpanish.getMeaning().replaceAll("'", "''");
        try (SQLiteDatabase bd = adm.getWritableDatabase()) {
            ContentValues cvWord = new ContentValues();
            cvWord.put("FECHA", newDate);
            cvWord.put("PALABRA", newWord);
            cvWord.put("SIGNIFICADO", newMeaning);
            System.out.println(CONTENT_VALUES + cvWord);
            bd.update(TABLE_WORD_SPANISH, cvWord, condition, null);
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    //WORDS
    public ArrayList<WordEnglish> getAllWordsEnglish(Context context) {
        //TABLA WORD
        //db.execSQL("CREATE TABLE WORD (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, WORD TEXT, TRADUCCION TEXT) ");
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = adm.getWritableDatabase();
        ArrayList<WordEnglish> words = new ArrayList<>();
        String query = "SELECT DISTINCT FECHA, WORD, TRADUCCION FROM WORD ORDER BY WORD";

        Cursor row = null;
        try {
            Log.e(QUERY, query);
            row = db.rawQuery(query, null);
            if (row.moveToFirst()) {
                do {
                    WordEnglish word = new WordEnglish();
                    word.setDate(row.getString(0));
                    word.setWord(row.getString(1));
                    word.setTranslation(row.getString(2));
                    words.add(word);
                } while (row.moveToNext());
            }
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
        } finally {
            assert row != null;
            row.close();
            db.close();
        }
        return words;
    }

    public Boolean validateWordEnglish(Context context, WordEnglish wordEnglish) {
        //TABLA WORD
        //db.execSQL("CREATE TABLE WORD (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, WORD TEXT, TRADUCCION TEXT) ");
        boolean response = false;
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = adm.getWritableDatabase();

        String word = wordEnglish.getWord().replaceAll("'", "''");
        String translation = wordEnglish.getTranslation().replaceAll("'", "''");

        String query = "SELECT * FROM WORD WHERE WORD = '" + word + "' AND TRADUCCION = '" + translation + "'";

        Cursor row = null;
        try {
            Log.e(QUERY, query);
            row = db.rawQuery(query, null);
            if (row.getCount() > 0) {
                response = true;
            }
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
        } finally {
            assert row != null;
            row.close();
            db.close();
        }
        return response;
    }

    public String addWordEnglish(Context context, WordEnglish wordEnglish) {
        //TABLA WORD
        //db.execSQL("CREATE TABLE WORD (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, WORD TEXT, TRADUCCION TEXT) ");
        String response = "OK";
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String date = wordEnglish.getDate().replaceAll("'", "\\'");
        String word = wordEnglish.getWord().replaceAll("'", "''");
        String translation = wordEnglish.getTranslation().replaceAll("'", "''");

        try (SQLiteDatabase db = adm.getWritableDatabase()) {
            ContentValues cvWord = new ContentValues();
            cvWord.put("FECHA", date);
            cvWord.put("WORD", word);
            cvWord.put("TRADUCCION", translation);
            db.insert(TABLE_WORD_ENGLISH, null, cvWord);
            System.out.println(CONTENT_VALUES + cvWord);

        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    public String deleteWordEnglish(Context context, WordEnglish wordEnglish) {
        //TABLA WORD
        //db.execSQL("CREATE TABLE WORD (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, WORD TEXT, TRADUCCION TEXT) ");
        String response = "OK";
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String date = wordEnglish.getDate().replaceAll("'", "\\'");
        String word = wordEnglish.getWord().replaceAll("'", "''");
        String translation = wordEnglish.getTranslation().replaceAll("'", "''");
        String query = "DELETE FROM WORD WHERE FECHA = '" + date + "' AND WORD = '" + word + "' " +
                "AND TRADUCCION = '" + translation + "'";
        try (SQLiteDatabase db = adm.getWritableDatabase()) {
            db.execSQL(query);
            Log.e(QUERY, query);
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    public String updateWordEnglish(Context context, WordEnglish wordEnglish, WordEnglish newWordEnglish) {
        //TABLA WORD
        //db.execSQL("CREATE TABLE WORD (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, WORD TEXT, TRADUCCION TEXT) ");
        String response = "OK";
        String date = wordEnglish.getDate().replaceAll("'", "\\'");
        String word = wordEnglish.getWord().replaceAll("'", "''");
        String translation = wordEnglish.getTranslation().replaceAll("'", "''");
        String condition = "FECHA = '" + date + "' AND WORD = '" + word + "' AND TRADUCCION = '" + translation + "'";
        Log.e(CONDITION, condition);
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String newDate = newWordEnglish.getDate().replaceAll("'", "\\'");
        String newWord = newWordEnglish.getWord().replaceAll("'", "''");
        String newTranslation = newWordEnglish.getTranslation().replaceAll("'", "''");
        try (SQLiteDatabase bd = adm.getWritableDatabase()) {
            ContentValues cvWord = new ContentValues();
            cvWord.put("FECHA", newDate);
            cvWord.put("WORD", newWord);
            cvWord.put("TRADUCCION", newTranslation);
            System.out.println(CONTENT_VALUES + cvWord);
            bd.update(TABLE_WORD_ENGLISH, cvWord, condition, null);
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    //CITAS
    public ArrayList<Quote> getAllQuotes(Context context) {
        //TABLA CITA
        //db.execSQL("CREATE TABLE CITA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, CITA TEXT, AUTOR TEXT) ");
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = adm.getWritableDatabase();
        ArrayList<Quote> quotes = new ArrayList<>();
        String query = "SELECT DISTINCT FECHA, CITA, AUTOR FROM CITA ORDER BY AUTOR";

        Cursor row = null;
        try {
            Log.e(QUERY, query);
            row = db.rawQuery(query, null);
            if (row.moveToFirst()) {
                do {
                    Quote quote = new Quote();
                    quote.setDate(row.getString(0));
                    quote.setQuote(row.getString(1));
                    quote.setAuthor(row.getString(2));
                    quotes.add(quote);
                } while (row.moveToNext());
            }
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
        } finally {
            assert row != null;
            row.close();
            db.close();
        }
        return quotes;
    }

    public Boolean validateQuote(Context context, Quote quote) {
        //TABLA CITA
        //db.execSQL("CREATE TABLE CITA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, CITA TEXT, AUTOR TEXT) ");
        boolean response = false;
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = adm.getWritableDatabase();

        String quote2 = quote.getQuote().replaceAll("'", "''");
        String author = quote.getAuthor().replaceAll("'", "''");

        String query = "SELECT * FROM CITA WHERE CITA = '" + quote2 + "' AND AUTOR = '" + author + "'";

        Cursor row = null;
        try {
            Log.e(QUERY, query);
            row = db.rawQuery(query, null);
            if (row.getCount() > 0) {
                response = true;
            }
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
        } finally {
            assert row != null;
            row.close();
            db.close();
        }
        return response;
    }

    public String addQuote(Context context, Quote quote) {
        //TABLA CITA
        //db.execSQL("CREATE TABLE CITA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, CITA TEXT, AUTOR TEXT) ");
        String response = "OK";
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String date = quote.getDate().replaceAll("'", "\\'");
        String quote2 = quote.getQuote().replaceAll("'", "''");
        String author = quote.getAuthor().replaceAll("'", "''");

        try (SQLiteDatabase db = adm.getWritableDatabase()) {
            ContentValues cvQuote = new ContentValues();
            cvQuote.put("FECHA", date);
            cvQuote.put("CITA", quote2);
            cvQuote.put("AUTOR", author);
            db.insert(TABLE_QUOTE, null, cvQuote);
            System.out.println(CONTENT_VALUES + cvQuote);

        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    public String deleteQuote(Context context, Quote quote) {
        //TABLA CITA
        //db.execSQL("CREATE TABLE CITA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, CITA TEXT, AUTOR TEXT) ");
        String response = "OK";
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String date = quote.getDate().replaceAll("'", "\\'");
        String quote2 = quote.getQuote().replaceAll("'", "''");
        String author = quote.getAuthor().replaceAll("'", "''");
        String query = "DELETE FROM CITA WHERE FECHA = '" + date + "' AND CITA = '" + quote2 + "' " +
                "AND AUTOR = '" + author + "'";
        try (SQLiteDatabase db = adm.getWritableDatabase()) {
            db.execSQL(query);
            Log.e(QUERY, query);
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    public String updateQuote(Context context, Quote quote, Quote newQuote) {
        //TABLA CITA
        //db.execSQL("CREATE TABLE CITA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, CITA TEXT, AUTOR TEXT) ");
        String response = "OK";
        String date = quote.getDate().replaceAll("'", "\\'");
        String quote2 = quote.getQuote().replaceAll("'", "''");
        String author = quote.getAuthor().replaceAll("'", "''");
        String condition = "FECHA = '" + date + "' AND CITA = '" + quote2 + "' AND AUTOR = '" + author + "'";
        Log.e(CONDITION, condition);
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        String newDate = newQuote.getDate().replaceAll("'", "\\'");
        String newQuote2 = newQuote.getQuote().replaceAll("'", "''");
        String newAuthor = newQuote.getAuthor().replaceAll("'", "''");
        try (SQLiteDatabase bd = adm.getWritableDatabase()) {
            ContentValues cvQuote = new ContentValues();
            cvQuote.put("FECHA", newDate);
            cvQuote.put("CITA", newQuote2);
            cvQuote.put("AUTOR", newAuthor);
            System.out.println(CONTENT_VALUES + cvQuote);
            bd.update(TABLE_QUOTE, cvQuote, condition, null);
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    //AUTORES
    public ArrayList<String> getAllAuthors(Context context) {
        //TABLA AUTOR
        //db.execSQL("CREATE TABLE AUTOR (Id INTEGER PRIMARY KEY AUTOINCREMENT, AUTOR TEXT) ");
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = adm.getWritableDatabase();
        ArrayList<String> authors = new ArrayList<>();
        String query = "SELECT AUTOR FROM AUTOR ORDER BY AUTOR";

        Cursor row = null;
        try {
            Log.e(QUERY, query);
            row = db.rawQuery(query, null);
            if (row.getCount() > 0){
                while (row.moveToNext()){
                    authors.add(row.getString(0));
                }
            }
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
        } finally {
            assert row != null;
            row.close();
            db.close();
        }
        return authors;
    }

    public boolean validateAuthor(Context context, String author) {
        //TABLA AUTOR
        //db.execSQL("CREATE TABLE AUTOR (Id INTEGER PRIMARY KEY AUTOINCREMENT, AUTOR TEXT) ");
        boolean response = false;
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = adm.getWritableDatabase();

        String author2 = author.replaceAll("'", "''");

        String query = "SELECT * FROM AUTOR WHERE AUTOR = '" + author2 + "'";

        Cursor row = null;
        try {
            Log.e(QUERY, query);
            row = db.rawQuery(query, null);
            if (row.getCount() > 0) {
                response = true;
            }
        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
        } finally {
            assert row != null;
            row.close();
            db.close();
        }
        return response;
    }

    public String addAuthor(Context context, String author) {
        //TABLA AUTOR
        //db.execSQL("CREATE TABLE AUTOR (Id INTEGER PRIMARY KEY AUTOINCREMENT, AUTOR TEXT) ");
        String response = "OK";
        OpenHelper adm = new OpenHelper(context, DB_NAME, null, DB_VERSION);

        String author2 = author.replaceAll("'", "''");

        try (SQLiteDatabase db = adm.getWritableDatabase()) {
            ContentValues cvAuthor = new ContentValues();
            cvAuthor.put("AUTOR", author2);
            db.insert(TABLE_AUTHOR, null, cvAuthor);
            System.out.println(CONTENT_VALUES + cvAuthor);

        } catch (Exception e) {
            Log.e(ERROR, e.getMessage());
            response = e.getMessage();
        }
        return response;
    }

    //REPORTES
    /*public ArrayList<Palabra> reportePalabras(Context contexto, String tipoReporte, String desde, String hasta) {
        ///TABLA PALABRA
        //db.execSQL("CREATE TABLE PALABRA (Id INTEGER PRIMARY KEY AUTOINCREMENT, FECHA TEXT, PALABRA TEXT, SIGNIFICADO TEXT) ");
        SQLiteOpenHelper adm = new SQLiteOpenHelper(contexto, BD_Nombre, null, BD_Version);
        SQLiteDatabase bd = adm.getWritableDatabase();
        ArrayList<Palabra> lista = new ArrayList<>();
        String consulta = "";
        if(tipoReporte.equals("General")){
            consulta = "SELECT DISTINCT FECHA, PALABRA, SIGNIFICADO FROM PALABRA ORDER BY FECHA";
        }else if(tipoReporte.equals("Por fecha")){
            consulta = "SELECT DISTINCT FECHA, PALABRA, SIGNIFICADO FROM PALABRA " +
                    "WHERE FECHA BETWEEN '" + desde + "' AND '" + hasta + "' ORDER BY FECHA";
        }

        Cursor fila = null;
        try {
            Log.e("CONSULTA: ", consulta);
            fila = bd.rawQuery(consulta, null);
            if (fila.moveToFirst()) {
                do {
                    Palabra palabra = new Palabra();
                    palabra.setFecha(fila.getString(0));
                    palabra.setPalabra(fila.getString(1));
                    palabra.setSignificado(fila.getString(2));
                    lista.add(palabra);
                } while (fila.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        } finally {
            assert fila != null;
            fila.close();
            bd.close();
        }
        return lista;
    }*/

}
