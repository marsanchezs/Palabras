package cl.mess.palabras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.Objects;

public class VisorPDF extends AppCompatActivity {

    private PDFView visorPDF;
    private File archivo;
    private Toolbar toolbar;
    private Context contexto;
    private TextView txtTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_pdf);

        contexto = getApplicationContext();
        visorPDF = (PDFView) findViewById(R.id.visorPDF);
        txtTitulo = (TextView) findViewById(R.id.tituloT);
        toolbar = (Toolbar) findViewById(R.id.toolbarVPDF);
        cargarToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                Intent main = new Intent(contexto, MainActivity.class);
                startActivity(main);
            }
        });

        Bundle datos = getIntent().getExtras();
        if(datos != null){
            archivo = new File(Objects.requireNonNull(datos.getString("Ruta")));
        }
        String dato = datos.getString("Ruta");
        System.out.println("RUTA ARCHIVO: "+dato);

        visorPDF.fromFile(archivo)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .load();
    }

    //MÉTODOS
    private void cargarToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        txtTitulo.setText(R.string.reports);
        ((TextView) txtTitulo).setTextSize(30);
    }

}