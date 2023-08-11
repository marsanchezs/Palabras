package cl.mess.palabras.model.reportes;


public class ReportePalabras {
    /*private Context contexto;
    private String tipoReporte, desde, hasta;
    private File archivoPDF;
    private Document documento;
    private PdfWriter pdfEscritor;
    private Paragraph parrafo;
    private final Utilidades utlilidades = new Utilidades();
    private final Delegate delegar = new Delegate();

    public ReportePalabras(Context contexto_, String tipoReporte_, String desde_, String hasta_) {
        this.contexto = contexto_;
        this.tipoReporte = tipoReporte_;
        this.desde = desde_;
        this.hasta = hasta_;
    }

    private void cargarDatos(){
        String[] encabezadoReporte = {"FECHA", "PALABRA", "SIGNIFICADO"};
        agregarMetadatos("Reporte Palabras General", "Detalle Palabras", "+Palabras");
        agregarTitulos("Reporte Palabras General", "Detalle Palabras", utlilidades.traerFecha(),
                utlilidades.traerHora());
        //agregarParrafo(textoCorto);
        //agregarParrafo(textLargo);
        ArrayList<Palabra> lista = delegar.reportePalabras(contexto, tipoReporte, desde, hasta);
        System.out.println("LISTA PALABRAS GENERAL: "+lista.toString());
        crearTabla(encabezadoReporte, lista);
    }

    public void abrirDocumentoPDF(){
        crearArchivo();
        try{
            documento = new Document(PageSize.LETTER);
            pdfEscritor = PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
            documento.open();
            cargarDatos();

        }catch(Exception e){
            Log.e("abrirDocumentoPDF", e.toString());
        }
    }

    private void crearArchivo(){
        File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File carpeta = new File(ruta.getAbsolutePath(), "PDF");
        if(!carpeta.exists())
            carpeta.mkdirs();
        archivoPDF = new File(carpeta, "Reporte.pdf");
    }

    public void cerrarDocumento(){
        documento.close();
    }


    public void agregarMetadatos(String titulo, String tema, String autor){
        documento.addTitle(titulo);
        documento.addSubject(tema);
        documento.addAuthor(autor);
    }

    public void agregarTitulos(String titulo, String subTitulo, String fecha, String hora){
        try{
            parrafo = new Paragraph();
            parrafo.setIndentationLeft(10);
            PdfPTable tablaEncabezado = new PdfPTable(3);
            tablaEncabezado.setWidthPercentage(100);
            tablaEncabezado.setWidths(new int[]{30, 80, 40});
            PdfPCell celda;
            Image imagen = utlilidades.traerLogo(contexto);
            celda = new PdfPCell(imagen, true);
            //celda.setPadding(10);
            celda.setRowspan(2);
            celda.setFixedHeight(70);
            tablaEncabezado.addCell(celda);
            celda = new PdfPCell(new Phrase(titulo, LetrasReportes.letraTituloReporte));
            celda.setBackgroundColor(BaseColor.DARK_GRAY);
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setPaddingLeft(10f);
            tablaEncabezado.addCell(celda);
            celda = new PdfPCell(new Phrase(fecha, LetrasReportes.letraReporte));
            celda.setBackgroundColor(BaseColor.DARK_GRAY);
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_BOTTOM);
            celda.setPaddingRight(10f);
            tablaEncabezado.addCell(celda);
            celda = new PdfPCell(new Phrase(subTitulo, LetrasReportes.letraReporte));
            celda.setBackgroundColor(BaseColor.DARK_GRAY);
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setPaddingLeft(10f);
            tablaEncabezado.addCell(celda);
            celda = new PdfPCell(new Phrase(hora, LetrasReportes.letraReporte));
            celda.setBackgroundColor(BaseColor.DARK_GRAY);
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setPaddingRight(10f);
            tablaEncabezado.addCell(celda);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            parrafo.add(tablaEncabezado);
            parrafo.setSpacingAfter(30);
            documento.add(parrafo);
        }catch(Exception e){
            Log.e("agregarTitulo", e.toString());
        }
    }

    private void agregarParrafosHijos(Paragraph parrafoHijo){
        parrafoHijo.setAlignment(Element.ALIGN_CENTER);
        parrafo.add(parrafoHijo);
    }

    public void agregarParrafo(String texto){
        try{
            parrafo = new Paragraph(texto, LetrasReportes.letraNormal);
            parrafo.setSpacingAfter(5);
            parrafo.setSpacingBefore(5);
            documento.add(parrafo);
        }catch(Exception e){
            Log.e("agregarParrafo", e.toString());
        }
    }

    public void crearTabla(String[] encabezado, ArrayList<Palabra> lista){

        try{

            parrafo = new Paragraph();
            parrafo.setFont(LetrasReportes.letraNormal);
            parrafo.setIndentationLeft(10);
            PdfPTable tablaPDF = new PdfPTable(encabezado.length);
            tablaPDF.setWidths(new int[]{80, 50, 40});
            tablaPDF.setWidthPercentage(100);
            PdfPCell celda;
            int indiceColumna = 0;
            while(indiceColumna < encabezado.length){
                celda = new PdfPCell(new Phrase(encabezado[indiceColumna++], LetrasReportes.letraCabeceraTabla));
                //celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setBackgroundColor(BaseColor.GRAY);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setPaddingLeft(10f);
                celda.setFixedHeight(40);
                tablaPDF.addCell(celda);
            }

            int total = 0;
            for(int indiceFila = 0; indiceFila < lista.size(); indiceFila++){
                String fecha = lista.get(indiceFila).getFecha();
                String palabra = lista.get(indiceFila).getPalabra();
                String significado = lista.get(indiceFila).getSignificado();
                total = total + 1;
                String[] fila = {fecha, palabra.toUpperCase(), significado.toUpperCase()};
                for(int indiceColumnas = 0; indiceColumnas < encabezado.length; indiceColumnas++){
                    celda = new PdfPCell(new Phrase(fila[indiceColumnas], LetrasReportes.letraDatosTabla));
                    //celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    //celda.setUseAscender(true);
                    celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    celda.setPaddingLeft(10f);
                    celda.setFixedHeight(40);
                    tablaPDF.addCell(celda);

                }
            }

            parrafo.add(tablaPDF);
            documento.add(parrafo);

            Paragraph parrafoPie = new Paragraph();
            parrafoPie.setIndentationLeft(10);
            PdfPTable tablaPie = new PdfPTable(2);
            tablaPie.setWidths(new int[]{130, 40});
            tablaPie.setWidthPercentage(100);
            PdfPCell celdaPie;
            celdaPie = new PdfPCell(new Phrase("TOTAL PALABRAS", LetrasReportes.letraCabeceraTabla));
            celdaPie.setFixedHeight(40);
            celdaPie.setPaddingLeft(10f);
            celdaPie.setBackgroundColor(BaseColor.DARK_GRAY);
            celdaPie.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaPie.addCell(celdaPie);
            celdaPie = new PdfPCell(new Phrase(String.valueOf(total), LetrasReportes.letraCabeceraTabla));
            celdaPie.setFixedHeight(40);
            celdaPie.setPaddingRight(10f);
            celdaPie.setBackgroundColor(BaseColor.GRAY);
            celdaPie.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaPie.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tablaPie.addCell(celdaPie);
            parrafoPie.add(tablaPie);
            documento.add(parrafoPie);

        }catch(Exception e){
            Log.e("crearTabla", e.toString());
        }
    }

    public void verPDF(){
        Bundle datos = new Bundle();
        datos.putString("Ruta", archivoPDF.getAbsolutePath());
        Intent intento = new Intent(contexto, VisorPDF.class);
        //intento.putExtra("Ruta", archivoPDF.getAbsolutePath());
        intento.putExtras(datos);
        intento.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contexto.startActivity(intento);
    }*/
}
