package cl.mess.palabras.fragments.quote;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cl.mess.palabras.R;
import cl.mess.palabras.bd.Delegate;
import cl.mess.palabras.model.Quote;
import cl.mess.palabras.fragments.quote.adapter.AdapterQuote;
import cl.mess.palabras.utilities.Utilities;

public class FragmentQuotes extends Fragment implements AdapterQuote.onClickQuote{

    private Context context;
    private AdapterQuote adapter;
    private ArrayList<Quote> filteredList;
    private boolean isFiltered = false;
    private AutoCompleteTextView edtSearch;
    private RecyclerView rvQuotes;
    private LinearLayout llv, llv2;
    private final Utilities utilities = new Utilities();
    private final Delegate delegate = new Delegate();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quotes, container, false);

        context = getActivity();
        llv = (LinearLayout) view.findViewById(R.id.llvFC);
        llv2 = (LinearLayout) view.findViewById(R.id.llvFC2);
        edtSearch = (AutoCompleteTextView) view.findViewById(R.id.actv_search);
        ImageButton btnAddQuote = (ImageButton) view.findViewById(R.id.btnAddQuote);
        rvQuotes = (RecyclerView) view.findViewById(R.id.rvQuotes);
        rvQuotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecyclerView();

        btnAddQuote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialogAdd();
            }
        });

        return view;
    }

    //MÉTODOS
    private void searchQuote(final AutoCompleteTextView edtSearch){
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
                if(s.toString().length() > 0){
                    isFiltered = true;
                }
            }
        });
    }

    private void search(String text) {
        filteredList = new ArrayList<>();
        ArrayList<Quote> quotes = delegate.getAllQuotes(context);

        for(Quote quote : quotes) {
            if(quote.getQuote().contains(text)) {
                filteredList.add(quote);
            }
        }
        adapter.filter(filteredList);
    }

    @Override
    public void clickListQuotes(int clickedQuote) {
        ArrayList<Quote> quotes;
        if(!isFiltered){
            quotes = delegate.getAllQuotes(context);
        }else{
            quotes = filteredList;
        }

        String date = quotes.get(clickedQuote).getDate();
        String quote = quotes.get(clickedQuote).getQuote();
        String author = quotes.get(clickedQuote).getAuthor();
        String message = date+"-"+quote+"-"+author;
        //utilities.mostrarMensaje(context, mensaje, "OK");
        Quote newQuote = generateQuote(date, quote, author);
        showDialogDetail(newQuote);
    }

    private void showDialogDetail(Quote quote){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_detail,null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = (ImageView) view.findViewById(R.id.iv_title);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvQuote = (TextView) view.findViewById(R.id.tv_meaning);
        TextView tvAuthor = (TextView) view.findViewById(R.id.tv_author);
        ImageButton btnEdit = (ImageButton) view.findViewById(R.id.ib_edit);
        ImageButton btnDelete = (ImageButton) view.findViewById(R.id.ib_delete);

        iv.setImageResource(R.drawable.ic_quotes);
        tvTitle.setText(R.string.quote);
        String text = "\""+quote.getQuote()+"\"";
        tvQuote.setText(text);
        tvAuthor.setVisibility(View.VISIBLE);
        tvAuthor.setText(quote.getAuthor());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEdit(quote, dialog);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelete(quote, dialog);
            }
        });
        dialog.show();
    }

    private void showDialogEdit(Quote quote, AlertDialog dialogDetail){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_add,null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = (ImageView) view.findViewById(R.id.iv_title);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        LinearLayout llvWordSpanish = (LinearLayout) view.findViewById(R.id.ll_spanish_word);
        LinearLayout llvQuote = (LinearLayout) view.findViewById(R.id.ll_quote);
        EditText edtQuote = (EditText) view.findViewById(R.id.et_quote);
        AutoCompleteTextView edtAuthor = (AutoCompleteTextView) view.findViewById(R.id.actv_author);
        ImageButton btnAddAuthor = (ImageButton) view.findViewById(R.id.ib_add_author);
        ImageButton btnAccept = (ImageButton) view.findViewById(R.id.ib_accept);
        ImageButton btnCancel = (ImageButton) view.findViewById(R.id.ib_cancel);

        iv.setImageResource(R.drawable.edit);
        tvTitle.setText(R.string.edit);
        llvWordSpanish.setVisibility(View.GONE);
        llvQuote.setVisibility(View.VISIBLE);
        edtQuote.setText(quote.getQuote());
        edtAuthor.setText(quote.getAuthor());
        edtQuote.requestFocus();

        btnAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddAuthor(edtAuthor);
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String responseValidation = utilities.validation(edtQuote, edtAuthor);
                if(responseValidation.equals("OK")){
                    //utilidades.mostrarMensaje(context, "OK", "OK");
                    String quote2 = edtQuote.getText().toString();
                    String author = edtAuthor.getText().toString();
                    Quote newQuote = generateQuote(utilities.getDate(), quote2, author);
                    boolean responseValidateQuote = delegate.validateQuote(context, newQuote);
                    String message = "";
                    if(responseValidateQuote){
                        message = "La cita \""+newQuote.getQuote()+"\" de "+newQuote.getAuthor()+" ya se encuentra agregada";
                        utilities.showMessage(context, message, "NOK");
                        edtQuote.requestFocus();
                    }else{
                        String responseUpdateQuote = delegate.updateQuote(context, quote, newQuote);
                        if(responseUpdateQuote.equals("OK")){
                            message = "La cita \""+newQuote.getQuote()+"\" de "+newQuote.getAuthor()+" ha sido actualizada";
                            utilities.showMessage(context, message, "OK");
                            dialog.dismiss();
                            dialogDetail.dismiss();
                            initRecyclerView();
                        }else{
                            message = "No se ha podido actualizar la cita \""+newQuote.getQuote()+"\" de "+newQuote.getAuthor();
                            utilities.showMessage(context, message, "NOK");
                            edtQuote.requestFocus();
                        }
                    }
                }else{
                    utilities.showMessages(context, responseValidation, edtQuote, edtAuthor, "CITAS");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogDelete(Quote quote, AlertDialog dialogDetail){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_detail,null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = (ImageView) view.findViewById(R.id.iv_title);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_meaning);
        ImageButton btnAccept = (ImageButton) view.findViewById(R.id.ib_edit);
        ImageButton btnCancel = (ImageButton) view.findViewById(R.id.ib_delete);

        iv.setImageResource(R.drawable.delete);
        tvTitle.setText(R.string.delete);
        String message = "¿Seguro de eliminar la cita \""+quote.getQuote()+"\" de "+quote.getAuthor()+"?";
        tvMessage.setText(message);
        btnAccept.setImageResource(R.drawable.ic_accept);
        btnCancel.setImageResource(R.drawable.ic_cancel);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "";
                String responseDeleteQuote = delegate.deleteQuote(context, quote);
                if(responseDeleteQuote.equals("OK")){
                    message = "La cita \""+quote.getQuote()+"\" de"+quote.getAuthor()+" ha sido eliminada";
                    utilities.showMessage(context, message, "OK");
                    dialog.dismiss();
                    dialogDetail.dismiss();
                    initRecyclerView();
                }else{
                    message = "No se ha podido eliminar la cita \""+quote.getQuote()+"\" de"+quote.getAuthor();
                    utilities.showMessage(context, message, "NOK");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogAdd(){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_add,null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = (ImageView) view.findViewById(R.id.iv_title);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        LinearLayout llvWordSpanish = (LinearLayout) view.findViewById(R.id.ll_spanish_word);
        LinearLayout llvQuote = (LinearLayout) view.findViewById(R.id.ll_quote);
        EditText edtQuote = (EditText) view.findViewById(R.id.et_quote);
        AutoCompleteTextView edtAuthor = (AutoCompleteTextView) view.findViewById(R.id.actv_author);
        ImageButton btnAddAuthor = (ImageButton) view.findViewById(R.id.ib_add_author);
        ImageButton btnAccept = (ImageButton) view.findViewById(R.id.ib_accept);
        ImageButton btnCancel = (ImageButton) view.findViewById(R.id.ib_cancel);

        iv.setImageResource(R.drawable.ic_quotes);
        tvTitle.setText(R.string.quotes);
        llvWordSpanish.setVisibility(View.GONE);
        llvQuote.setVisibility(View.VISIBLE);
        getAuthors(edtAuthor);

        btnAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddAuthor(edtAuthor);
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String responseValidation = utilities.validation(edtQuote, edtAuthor);
                if(responseValidation.equals("OK")){
                    //utilidades.mostrarMensaje(context, "OK", "OK");
                    String quote2 = edtQuote.getText().toString();
                    String author = edtAuthor.getText().toString();
                    Quote newQuote = generateQuote(utilities.getDate(), quote2, author);
                    boolean responseValidateQuote = delegate.validateQuote(context, newQuote);
                    String message = "";
                    if(responseValidateQuote){
                        message = "La cita \""+newQuote.getQuote()+"\" de "+newQuote.getAuthor()+" ya se encuentra agregada";
                        utilities.showMessage(context, message, "NOK");
                        edtQuote.requestFocus();
                    }else{
                        String responseAddQuote = delegate.addQuote(context, newQuote);
                        if(responseAddQuote.equals("OK")){
                            message = "La cita \""+newQuote.getQuote()+"\" de "+newQuote.getAuthor()+" ha sido agregada";
                            utilities.showMessage(context, message, "OK");
                            dialog.dismiss();
                            initRecyclerView();
                        }else{
                            message = "No se ha podido agregar la cita \""+newQuote.getQuote()+"\" de "+newQuote.getAuthor();
                            utilities.showMessage(context, message, "NOK");
                            edtQuote.requestFocus();
                        }
                    }
                }else{
                    utilities.showMessages(context, responseValidation, edtQuote, edtAuthor, "CITAS");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogAddAuthor(AutoCompleteTextView edtAuthors){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_author,null);
        builder.setView(view);
        dialog = builder.create();
        AutoCompleteTextView edtAuthor = (AutoCompleteTextView) view.findViewById(R.id.actv_author);
        ImageButton btnAccept = (ImageButton) view.findViewById(R.id.ib_accept);
        ImageButton btnCancel = (ImageButton) view.findViewById(R.id.ib_cancel);

        getAuthors(edtAuthor);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String author = edtAuthor.getText().toString();
                String message = "";
                if(author.isEmpty()){
                    message = "Ingresar un autor";
                    utilities.showMessage(context, message, "NOK");
                    edtAuthor.requestFocus();
                }else{
                    boolean responseValidateAuthor = delegate.validateAuthor(context, author);
                    if(responseValidateAuthor){
                        message = "El autor "+author+" ya se encuentra agregado";
                        utilities.showMessage(context, message, "NOK");
                        edtAuthor.requestFocus();
                    }else{
                        String responseAddAuthor = delegate.addAuthor(context, author);
                        if(responseAddAuthor.equals("OK")){
                            message = "El autor "+author+" ha sido agregado";
                            utilities.showMessage(context, message, "OK");
                            dialog.dismiss();
                            getAuthors(edtAuthors);
                            //initRecyclerView();
                        }else{
                            message = "No se ha podido agregar al autor "+author;
                            utilities.showMessage(context, message, "NOK");
                            edtAuthor.requestFocus();
                        }
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getAuthors(AutoCompleteTextView edtAuthor) {
        ArrayList<String> authors = delegate.getAllAuthors(context);
        System.out.println("LISTA AUTORES: " + authors.toString());
        String[] arrayAuthors = new String[authors.size()];
        arrayAuthors = authors.toArray(arrayAuthors);
        assert context != null;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.lista, R.id.txt, arrayAuthors);
        edtAuthor.setAdapter(adapter);

        edtAuthor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
        });
    }

    private Quote generateQuote(String date, String quote, String author){
        Quote newQuote = new Quote();
        newQuote.setDate(date);
        newQuote.setQuote(quote);
        newQuote.setAuthor(author);
        return newQuote;
    }

    private void initRecyclerView(){
        ArrayList<Quote> quotes = delegate.getAllQuotes(context);
        System.out.println("Nº DE CITAS : "+quotes.size());
        edtSearch.setText("");
        if(quotes.size() == 0){
            llv.setVisibility(View.VISIBLE);
            llv2.setVisibility(View.GONE);
        }else{
            llv.setVisibility(View.GONE);
            llv2.setVisibility(View.VISIBLE);
            System.out.println("LISTA DE CITAS RV: "+quotes.get(0));
            adapter = new AdapterQuote(quotes, this);
            rvQuotes.setAdapter(adapter);
            searchQuote(edtSearch);
        }
    }
}