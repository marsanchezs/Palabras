package cl.mess.palabras.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cl.mess.palabras.R;

public class Utilities {
    public static final String SPANISH_WORDS = "PALABRAS";
    public static final String SPANISH_QUOTES = "CITAS";
    public static final String ENGLISH_WORDS = "WORDS";
    public static final String OK = "OK";
    public static final String NOK = "NOK";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";

    public String validation(EditText etWord, EditText etMeaning) {
        String word = etWord.getText().toString();
        String meaning = etMeaning.getText().toString();
        String response;
        if ((word.isEmpty()) && (meaning.isEmpty())) {
            response = ONE;
        } else if (word.isEmpty()) {
            response = TWO;
        } else if (meaning.isEmpty()) {
            response = THREE;
        } else {
            response = OK;
        }
        return response;
    }

    public void showMessages(
            Context context,
            String response,
            EditText etWord,
            EditText etMeaning,
            String screen
    ) {
        String message = "";
        switch (response) {
            case ONE:
                switch (screen) {
                    case SPANISH_WORDS:
                        message = context.getString(R.string.enter_a_word_and_its_meaning);
                        break;
                    case SPANISH_QUOTES:
                        message = context.getString(R.string.enter_a_quote_and_its_author);
                        break;
                    case ENGLISH_WORDS:
                        message = context.getString(R.string.enter_an_word_or_phrase_in_english_and_its_meaning);
                        break;
                }
                showMessage(context, message, NOK);
                etWord.requestFocus();
                break;
            case TWO:
                switch (screen) {
                    case SPANISH_WORDS:
                        message = context.getString(R.string.enter_a_word);
                        break;
                    case SPANISH_QUOTES:
                        message = context.getString(R.string.enter_a_quote);
                        break;
                    case ENGLISH_WORDS:
                        message = context.getString(R.string.enter_a_word_or_phrase_in_english);
                        break;
                }
                showMessage(context, message, NOK);
                etWord.requestFocus();
                break;
            case THREE:
                switch (screen) {
                    case SPANISH_WORDS:
                        message = context.getString(R.string.enter_word_meaning);
                        break;
                    case SPANISH_QUOTES:
                        message = context.getString(R.string.enter_an_author);
                        break;
                    case ENGLISH_WORDS:
                        message = context.getString(R.string.enter_translation_of_the_word_or_phrase);
                        break;
                }
                showMessage(context, message, NOK);
                etMeaning.requestFocus();
                break;
        }
    }

    public String getDate() {
        String stringDate;
        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("d-M-yyyy", java.util.Locale.getDefault());
        stringDate = formatDate.format(date);
        return stringDate;
    }

    public String getPreviousWeek() {
        String date;
        Date today = new Date();
        Date oneMoreWeek = new Date(today.getTime() - (86400000 * 7));
        SimpleDateFormat formatDate = new SimpleDateFormat("d-M-yyyy", java.util.Locale.getDefault());
        date = formatDate.format(oneMoreWeek);
        return date;
    }

    public void showMessage(Context context, String message, String iconType) {
        Toast response = new Toast(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.message, null);
        response.setView(view);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.imgMP);
        if (iconType.equals(OK)) {
            ivIcon.setImageResource(R.drawable.ic_accept);
        } else {
            ivIcon.setImageResource(R.drawable.ic_cancel);
        }
        TextView tvMessage = (TextView) view.findViewById(R.id.txtM1MP);
        tvMessage.setText(message);
        response.setDuration(Toast.LENGTH_LONG);
        response.show();
    }

    /*public String validarFechas(String desde, String hasta) {
        String respuesta = "OK";
        SimpleDateFormat formato = new SimpleDateFormat("d-M-yyyy", java.util.Locale.getDefault());
        try {
            Date fechaDesde = formato.parse(desde);
            Date fechaHasta = formato.parse(hasta);
            Date hoy = formato.parse(getDate());
            if (hoy.before(fechaHasta)) {
                respuesta = "Fecha final no puede ser posterior a " + getDate();
            } else if (fechaHasta.before(fechaDesde)) {
                respuesta = "Fecha inicial no puede ser posterior a " + hasta;
            }
        } catch (ParseException e) {
            Log.e("ERROR", e.getMessage());
        }
        return respuesta;
    }

    public String traerHora() {
        String sHora = "SIN HORA";
        Date date = new Date();
        SimpleDateFormat horaF = new SimpleDateFormat("H:mm a", java.util.Locale.getDefault());
        sHora = horaF.format(date);
        return sHora;
    }

    public Image traerLogo(Context contexto) throws IOException, BadElementException {
        Drawable dibujo = ContextCompat.getDrawable(contexto, R.drawable.ic_logo);
        BitmapDrawable bitmapDibujo = ((BitmapDrawable) dibujo);
        assert bitmapDibujo != null;
        Bitmap bmp = bitmapDibujo.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return Image.getInstance(stream.toByteArray());
    }

    public void hideKeyboard(Context context, EditText edt) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }*/
}
