package cl.mess.palabras.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cl.mess.palabras.R;

public class Utilities {
    public String validation(EditText edtPalabra, EditText edtSignificado){
        String palabra = edtPalabra.getText().toString();
        String significado = edtSignificado.getText().toString();
        String respuesta = "";
        if((palabra.isEmpty()) && (significado.isEmpty())){
            respuesta = "1";
        }else if(palabra.isEmpty()){
            respuesta = "2";
        }else if(significado.isEmpty()){
            respuesta = "3";
        }else{
            respuesta = "OK";
        }
        return respuesta;
    }

    public void showMessages(Context contexto, String respuesta, EditText edtPalabra, EditText edtSignificado, String pantalla){
        System.out.println("RESPUESTA: "+respuesta+"|PANTALLA "+pantalla);
        String mensaje = "";
        switch(respuesta){
            case "1":

                switch(pantalla){
                    case "PALABRAS":
                        mensaje = "Ingresar una palabra y su significado";
                        break;
                    case "CITAS":
                        mensaje = "Ingresar una cita y su autor";
                        break;
                    case "WORDS":
                        mensaje = "Ingresar una palabra en inglés y su significado";
                        break;
                }

                //mensaje = "Ingresar una palabra y su significado";
                showMessage(contexto, mensaje, "NOK");
                edtPalabra.requestFocus();
                break;
            case "2":

                switch(pantalla){
                    case "PALABRAS":
                        mensaje = "Ingresar una palabra";
                        break;
                    case "CITAS":
                        mensaje = "Ingresar una cita";
                        break;
                    case "WORDS":
                        mensaje = "Ingresar una palabra en inglés";
                        break;
                }

                //mensaje = "Ingresar una palabra";
                showMessage(contexto, mensaje, "NOK");
                edtPalabra.requestFocus();
                break;
            case "3":

                switch(pantalla){
                    case "PALABRAS":
                        mensaje = "Ingresar significado de la palabra";
                        break;
                    case "CITAS":
                        mensaje = "Ingresar un autor";
                        break;
                    case "WORDS":
                        mensaje = "Ingresar traducción de la palabra";
                        break;
                }

                //mensaje = "Ingresar significado";
                showMessage(contexto, mensaje, "NOK");
                edtSignificado.requestFocus();
                break;
        }
    }
    
    public String getDate() {
        String sFecha = "SIN FECHA";
        Date date = new Date();
        SimpleDateFormat fechaF = new SimpleDateFormat("d-M-yyyy", java.util.Locale.getDefault());
        sFecha = fechaF.format(date);
        return sFecha;
    }

    public String traerSemanaAnterior(){
        String fecha = "";
        Date hoy = new Date();
        Date diezDiasMas = new Date(hoy.getTime() - (86400000 * 7));
        SimpleDateFormat fechaF = new SimpleDateFormat("d-M-yyyy", java.util.Locale.getDefault());
        fecha = fechaF.format(diezDiasMas);
        return fecha;
    }

    public String validarFechas(String desde, String hasta){
        String respuesta = "OK";
        SimpleDateFormat formato = new SimpleDateFormat("d-M-yyyy", java.util.Locale.getDefault());
        try {
            Date fechaDesde = formato.parse(desde);
            Date fechaHasta = formato.parse(hasta);
            Date hoy = formato.parse(getDate());
            if(hoy.before(fechaHasta)){
                respuesta = "Fecha final no puede ser posterior a "+getDate();
            }else if(fechaHasta.before(fechaDesde)){
                respuesta = "Fecha inicial no puede ser posterior a "+hasta;
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
        sHora =  horaF.format(date);
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

    public void showMessage(Context context, String message, String iconType) {
        Toast response = new Toast(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate( R.layout.message, null );
        response.setView(view);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.imgMP);
        if(iconType.equals("OK")){
            ivIcon.setImageResource(R.drawable.ic_accept);
        }else{
            ivIcon.setImageResource(R.drawable.ic_cancel);
        }
        TextView tvMessage = (TextView) view.findViewById(R.id.txtM1MP);
        tvMessage.setText(message);
        response.setDuration(Toast.LENGTH_LONG);
        response.show();
    }

    public void hideKeyboard(Context context, EditText edt){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }
}
