package cl.mess.palabras;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Objects;

import cl.mess.palabras.model.reportes.ReportePalabras;
import cl.mess.palabras.ui.adapters.AdapterFragments;
import cl.mess.palabras.utilities.Utilities;

public class MainActivity extends AppCompatActivity {

    private Context contexto;
    private final Utilities utilities = new Utilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contexto = getApplicationContext();
        Toolbar barraHerramientas = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barraHerramientas);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TabLayout tabLayout = findViewById(R.id.tabLayoutMA);
        ViewPager visor = findViewById(R.id.visorMA);
        TabItem tabPalabras = findViewById(R.id.tabWordsSpanish);
        TabItem tabCitas = findViewById(R.id.tabQuotes);
        TabItem tabWords = findViewById(R.id.tabWordsEnglish);

        AdapterFragments miAdaptador = new AdapterFragments(getSupportFragmentManager(), tabLayout.getTabCount());
        visor.setAdapter(miAdaptador);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(visor));
        visor.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    //MÉTODOS
    @Override public boolean onCreateOptionsMenu(Menu miMenu){
        getMenuInflater().inflate(R.menu.mi_menu, miMenu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override public boolean onOptionsItemSelected(MenuItem opcionMenu){
        int id = opcionMenu.getItemId();
        switch(opcionMenu.getItemId()){
            case R.id.reportesM:
                mostrarVentanaReportes();
                return true;
            case R.id.contactoM:
                mostrarVentanaContacto();
                return true;
            case R.id.salirM:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(opcionMenu);
        }
    }

    private void mostrarVentanaReportes(){
        final AlertDialog ventana;
        final AlertDialog.Builder constructorDialogo = new AlertDialog.Builder(this);
        LayoutInflater inflador = getLayoutInflater();
        View vistaDialogo = inflador.inflate(R.layout.ventana_reportes,null);
        constructorDialogo.setView(vistaDialogo);
        ventana = constructorDialogo.create();
        Spinner spReporte = (Spinner) vistaDialogo.findViewById(R.id.spReportesVR);
        RadioButton rbGeneral = (RadioButton) vistaDialogo.findViewById(R.id.rbGeneralVR);
        RadioButton rbPorFecha = (RadioButton) vistaDialogo.findViewById(R.id.rbPorFechaVR);
        TableRow trPorFecha = (TableRow) vistaDialogo.findViewById(R.id.trPorFecha);
        LinearLayout llDesde = (LinearLayout) vistaDialogo.findViewById(R.id.llDesdeVR);
        TextView txtDesde = (TextView) vistaDialogo.findViewById(R.id.txtFechaDesdeVR);
        LinearLayout llHasta = (LinearLayout) vistaDialogo.findViewById(R.id.llHastaVR);
        TextView txtHasta = (TextView) vistaDialogo.findViewById(R.id.txtFechaHastaVR);
        ImageView imgPdf = (ImageView) vistaDialogo.findViewById(R.id.imgPdfVR);

        cargarSpinner(spReporte);
        mostraOcultarTrPorFecha(trPorFecha, rbGeneral, rbPorFecha, txtDesde, txtHasta, llDesde, llHasta);

        imgPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                String reporte = spReporte.getSelectedItem().toString();
                String mensaje = "";
                if(reporte.equals(getString(R.string.select))){
                    mensaje = "Seleccionar un reporte";
                    utilities.showMessage(contexto, mensaje, "NOK");
                }else{
                    String tipoReporte = traerTipoReorte(rbGeneral, rbPorFecha);
                    if(tipoReporte.equals(getString(R.string.general))){
                        mensaje = reporte+"-"+tipoReporte;
                        utilities.showMessage(contexto, mensaje, "OK");
                    }/*else if(tipoReporte.equals(getString(R.string.porFecha))){
                        String desde = txtDesde.getText().toString();
                        String hasta = txtHasta.getText().toString();
                        String validacionFechas = utilidades.validarFechas(desde, hasta);
                        if(validacionFechas.equals("OK")){
                            mensaje = reporte+"-"+tipoReporte+"-"+validacionFechas;
                            utilidades.mostrarMensaje(contexto, mensaje, "OK");
                            ReportePalabras reportePalbras = new ReportePalabras(contexto, tipoReporte, desde, hasta);
                            generarReporte(tipoReporte, reportePalbras);
                        }else{
                            utilidades.mostrarMensaje(contexto, validacionFechas, "NOK");
                        }
                    }*/

                }
            }
        });

        ventana.show();
    }

    private void generarReporte(String tipoReporte, ReportePalabras reporte){

        switch(tipoReporte){
            /*case "Por fecha":
                //cargarReportePalabras();
                reporte.abrirDocumentoPDF();
                reporte.cerrarDocumento();
                reporte.verPDF();
                break;*/
            /*case "Reporte Libros x Autor":
                cargarReporteAutor();
                reporteAutor.verPDF2();
                break;
            case "Reporte Libros x Género":
                cargarReporteGenero();
                reporteGenero.verPDF2();
                break;
            case "Reporte Libros x País":
                cargarReportePais();
                reportePais.verPDF2();
                break;*/
        }
    }

    /*private void cargarReportePalabras(){
        reporteGeneral = new ReporteGeneral(contexto);
        reporteGeneral.abrirDocumentoPDF();
        reporteGeneral.cerrarDocumento();
    }*/

    private String traerTipoReorte(RadioButton rbGeneral, RadioButton rbPorFecha){
        String respuesta = "";
        if(rbGeneral.isChecked()){
            respuesta = getString(R.string.general);
        }else if(rbPorFecha.isChecked()){
            respuesta = getString(R.string.by_date);
        }
        return respuesta;
    }

    private void mostraOcultarTrPorFecha(TableRow trPorFecha, RadioButton rbGeneral, RadioButton rbPorFecha,
                                         TextView txtDesde, TextView txtHasta, LinearLayout llDesde,
                                         LinearLayout llHasta){
        trPorFecha.setVisibility(View.GONE);
        rbGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                trPorFecha.setVisibility(View.GONE);
            }
        });

        rbPorFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                trPorFecha.setVisibility(View.VISIBLE);
                txtDesde.setText(utilities.traerSemanaAnterior());
                txtHasta.setText(utilities.getDate());
                cargarCalendario(llDesde, llHasta, txtDesde, txtHasta);
            }
        });
    }

    private void cargarCalendario(LinearLayout llDesde, LinearLayout llHasta, TextView txtDesde, TextView txtHasta){
        llDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                mostrarCalendario(txtDesde);
            }
        });

        llHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                mostrarCalendario(txtHasta);
            }
        });
    }

    private void mostrarCalendario(TextView txtFecha){
        DatePickerDialog selectorFecha;
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        selectorFecha = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker vista, int anio_, int mes_, int dia_) {
                        // set day of month , month and year value in the edit text
                        String fecha = dia_ + "-" + (mes_ + 1) + "-" + anio_;
                        txtFecha.setText(fecha);
                        //txtFecha2.setText(fecha);
                    }
                }, anio, mes, dia);
        selectorFecha.show();
    }

    private void cargarSpinner(Spinner spTipoReporte){
        String[] tipoReporte = new String[]{getString(R.string.select), getString(R.string.words_spanish),
                getString(R.string.quotes), getString(R.string.words_english)};
        ArrayAdapter<String> adaptador;
        adaptador = new ArrayAdapter<>(contexto, R.layout.lista, R.id.txt, tipoReporte);
        spTipoReporte.setAdapter(adaptador);
    }

    private void mostrarVentanaContacto(){
        final AlertDialog ventana;
        final AlertDialog.Builder constructorDialogo = new AlertDialog.Builder(this);
        LayoutInflater inflador = getLayoutInflater();
        View vistaDialogo = inflador.inflate(R.layout.ventana_contacto,null);
        constructorDialogo.setView(vistaDialogo);
        ventana = constructorDialogo.create();
        ImageView imgIcono = (ImageView) vistaDialogo.findViewById(R.id.imgVC);
        ImageView imgWhatsapp = (ImageView) vistaDialogo.findViewById(R.id.imgWhatAppVC);
        ImageView imgGmail = (ImageView) vistaDialogo.findViewById(R.id.imgGmailVC);
        ImageView imgLinkedin = (ImageView) vistaDialogo.findViewById(R.id.imgLinkedinVC);
        ImageView imgGithub = (ImageView) vistaDialogo.findViewById(R.id.imgGithubVC);

        imgWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                enviarWhatsApp();
            }
        });

        imgGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                enviarCorreo();
            }
        });

        imgLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                Uri direccion = Uri.parse(getString(R.string.linkedin));
                Intent linkedin = new Intent(Intent.ACTION_VIEW, direccion);
                startActivity(linkedin);
            }
        });

        imgGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                Uri direccion = Uri.parse(getString(R.string.github));
                Intent github = new Intent(Intent.ACTION_VIEW, direccion);
                startActivity(github);
            }
        });
        ventana.show();
    }

    private void enviarWhatsApp(){
        String mensaje = "Contacto "+getString(R.string.app_name);
        Intent whatsApp = new Intent(Intent.ACTION_VIEW);
        String uri = "whatsapp://send?phone=" + getString(R.string.whatsApp) + "&text=" + mensaje;
        whatsApp.setData(Uri.parse(uri));

        /*Intent whatsApp = new Intent(Intent.ACTION_SEND);
        whatsApp.setType("text/plain");
        whatsApp.setPackage("com.whatsapp");
        whatsApp.putExtra(Intent.EXTRA_TEXT, "Contacto "+getString(R.string.app_name));*/
        try {
            startActivity(whatsApp);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
            mensaje = "WhatsApp no está instalado";
            utilities.showMessage(contexto, mensaje, "NOK");
        }
    }

    @SuppressLint("IntentReset")
    private void enviarCorreo() {
        String[] TO = {getString(R.string.gmail)}; //aquí pon tu correo
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        //Esto podrás modificarlo si quieres, el asunto y el cuerpo del mensaje
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto "+getString(R.string.app_name));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe aquí tu mensaje");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            String mensaje = "Sin clientes de correo instalados";
            utilities.showMessage(contexto, mensaje, "NOK");
            /*Toast.makeText(MainActivity.this,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();*/
        }
    }

}