package cl.mess.palabras.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cl.mess.palabras.R;
import cl.mess.palabras.bd.Delegate;
import cl.mess.palabras.model.WordSpanish;
import cl.mess.palabras.ui.adapters.AdapterWordSpanish;
import cl.mess.palabras.utilities.Utilities;

public class FragmentWordsSpanish extends Fragment implements AdapterWordSpanish.onClickWord{

    private Context context;
    private AdapterWordSpanish adapter;
    private ArrayList<WordSpanish> filteredList;
    private boolean isFiltered = false;
    private AutoCompleteTextView edtSearch;
    private RecyclerView rvWordsSpanish;
    private LinearLayout llv, llv2;
    private ConstraintLayout cl;
    private final Utilities utilities = new Utilities();
    private final Delegate delegate = new Delegate();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_words_spanish, container, false);

        context = getActivity();
        llv = (LinearLayout) view.findViewById(R.id.llvFP);
        llv2 = (LinearLayout) view.findViewById(R.id.llvFP2);
        cl = (ConstraintLayout) view.findViewById(R.id.clFP);
        edtSearch = (AutoCompleteTextView) view.findViewById(R.id.edtSearch);
        ImageButton btnAddWordSpanish = (ImageButton) view.findViewById(R.id.btnAddWordSpanish);
        rvWordsSpanish = (RecyclerView) view.findViewById(R.id.rvWordsSpanish);
        rvWordsSpanish.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecyclerView();

        btnAddWordSpanish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialogAdd();
            }
        });

        return view;
    }

    //M?ETODOS
    private void searchWord(final AutoCompleteTextView edtSearch){
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
        ArrayList<WordSpanish> words = delegate.getAllWordsSpanish(context);

        for(WordSpanish word : words) {
            if(word.getWord().contains(text)) {
                filteredList.add(word);
            }
        }
        adapter.filter(filteredList);
    }

    @Override
    public void clickListWords(int clickedPalabra) {
        ArrayList<WordSpanish> words;
        if(!isFiltered){
            words = delegate.getAllWordsSpanish(context);
        }else{
            words = filteredList;
        }

        String date = words.get(clickedPalabra).getDate();
        String word = words.get(clickedPalabra).getWord();
        String meaning = words.get(clickedPalabra).getMeaning();
        String message = date+"-"+word+"-"+meaning;
        //utilidades.mostrarMensaje(context, mensaje, "OK");
        WordSpanish wordSpanish = generateWordSpanish(date, word, meaning);
        showDialogDetail(wordSpanish);
    }

    private void showDialogDetail(WordSpanish wordSpanish){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_detail,null);
        builder.setView(view);
        dialog = builder.create();
        TextView tvWordSpanish = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvMeaning = (TextView) view.findViewById(R.id.tvMeaning);
        ImageButton btnEdit = (ImageButton) view.findViewById(R.id.btnEdit);
        ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);

        tvWordSpanish.setText(wordSpanish.getWord());
        tvMeaning.setText(wordSpanish.getMeaning());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEdit(wordSpanish, dialog);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelete(wordSpanish, dialog);
            }
        });
        dialog.show();
    }

    private void showDialogEdit(WordSpanish wordSpanish, AlertDialog dialogDetail){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_add,null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        EditText edtWordSpanish = (EditText) view.findViewById(R.id.edtWordSpanish);
        EditText edtMeaning = (EditText) view.findViewById(R.id.edtMeaning);
        ImageButton btnAccept = (ImageButton) view.findViewById(R.id.btnAccept);
        ImageButton btnCancel = (ImageButton) view.findViewById(R.id.btnCancel);

        iv.setImageResource(R.drawable.edit);
        tvTitle.setText(R.string.edit);
        edtWordSpanish.setText(wordSpanish.getWord());
        edtMeaning.setText(wordSpanish.getMeaning());
        edtWordSpanish.requestFocus();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String responseValidation = utilities.validation(edtWordSpanish, edtMeaning);
                if(responseValidation.equals("OK")){
                    //utilidades.mostrarMensaje(context, "OK", "OK");
                    String word = edtWordSpanish.getText().toString();
                    String meaning = edtMeaning.getText().toString();
                    WordSpanish newWordSpanish = generateWordSpanish(utilities.getDate(), word, meaning);
                    boolean responseValidateWordSpanish = delegate.validateWordSpanish(context, newWordSpanish);
                    String message = "";
                    if(responseValidateWordSpanish){
                        message = "La palabra "+wordSpanish.getWord()+" ya se encuentra agregada";
                        utilities.showMessage(context, message, "NOK");
                        edtWordSpanish.requestFocus();
                    }else{
                        String responseUpdateWordSpanish = delegate.updateWordSpanish(context, wordSpanish, newWordSpanish);
                        if(responseUpdateWordSpanish.equals("OK")){
                            message = "La palabra "+newWordSpanish.getWord()+" ha sido actualizada";
                            utilities.showMessage(context, message, "OK");
                            dialog.dismiss();
                            dialogDetail.dismiss();
                            initRecyclerView();
                        }else{
                            message = "No se ha podido actualizar la palabra "+wordSpanish.getWord();
                            utilities.showMessage(context, message, "NOK");
                            edtWordSpanish.requestFocus();
                        }
                    }
                }else{
                    utilities.showMessages(context, responseValidation, edtWordSpanish, edtMeaning, "PALABRAS");
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

    private void showDialogDelete(WordSpanish wordSpanish, AlertDialog dialogDetail){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_detail,null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvMessage = (TextView) view.findViewById(R.id.tvMeaning);
        ImageButton btnAccept = (ImageButton) view.findViewById(R.id.btnEdit);
        ImageButton btnCancel = (ImageButton) view.findViewById(R.id.btnDelete);

        iv.setImageResource(R.drawable.delete);
        tvTitle.setText(R.string.delete);
        String message = "¿Seguro de eliminar la palabra "+wordSpanish.getWord()+"?";
        tvMessage.setText(message);
        btnAccept.setImageResource(R.drawable.ic_accept);
        btnCancel.setImageResource(R.drawable.ic_cancel);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "";
                String responseDeleteWordSpanish = delegate.deleteWordSpanish(context, wordSpanish);
                if(responseDeleteWordSpanish.equals("OK")){
                    message = "La palabra "+wordSpanish.getWord()+" ha sido eliminada";
                    utilities.showMessage(context, message, "OK");
                    dialog.dismiss();
                    dialogDetail.dismiss();
                    initRecyclerView();
                }else{
                    message = "No se ha podido eliminar la palabra "+wordSpanish.getWord();
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

    private void initRecyclerView(){
        ArrayList<WordSpanish> words = delegate.getAllWordsSpanish(context);
        for(int i = 0; i < words.size(); i++){
            System.out.println("PALABRAS: "+words.get(i).getWord());
        }
        System.out.println("Nº DE PALABRAS : "+words.size());
        edtSearch.setText("");
        if(words.size() == 0){
            llv.setVisibility(View.VISIBLE);
            llv2.setVisibility(View.GONE);
        }else{
            llv.setVisibility(View.GONE);
            llv2.setVisibility(View.VISIBLE);
            System.out.println("LISTA DE PALABRAS RV: "+words.get(0));
            adapter = new AdapterWordSpanish(words, this);
            rvWordsSpanish.setAdapter(adapter);
            searchWord(edtSearch);
        }
    }

    private void showDialogAdd(){
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_add,null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        EditText edtWordSpanish = (EditText) view.findViewById(R.id.edtWordSpanish);
        EditText edtMeaning = (EditText) view.findViewById(R.id.edtMeaning);
        ImageButton btnAccept = (ImageButton) view.findViewById(R.id.btnAccept);
        ImageButton btnCancel = (ImageButton) view.findViewById(R.id.btnCancel);

        iv.setImageResource(R.drawable.ic_words_spanish);
        tvTitle.setText(R.string.word_spanish);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String responseValidation = utilities.validation(edtWordSpanish, edtMeaning);
                if(responseValidation.equals("OK")){
                    //utilidades.mostrarMensaje(context, "OK", "OK");
                    String word = edtWordSpanish.getText().toString();
                    String meaning = edtMeaning.getText().toString();
                    WordSpanish wordSpanish = generateWordSpanish(utilities.getDate(), word, meaning);
                    boolean responseValidateWordSpanish = delegate.validateWordSpanish(context, wordSpanish);
                    String message = "";
                    if(responseValidateWordSpanish){
                        message = "La palabra "+wordSpanish.getWord()+" ya se encuentra agregada";
                        utilities.showMessage(context, message, "NOK");
                        edtWordSpanish.requestFocus();
                    }else{
                        String responseAddWordSpanish = delegate.addWordSpanish(context, wordSpanish);
                        if(responseAddWordSpanish.equals("OK")){
                            message = "La palabra "+wordSpanish.getWord()+" ha sido agregada";
                            utilities.showMessage(context, message, "OK");
                            dialog.dismiss();
                            initRecyclerView();
                        }else{
                            message = "No se ha podido agregar la palabra "+wordSpanish.getWord();
                            utilities.showMessage(context, message, "NOK");
                            edtWordSpanish.requestFocus();
                        }
                    }
                }else{
                    utilities.showMessages(context, responseValidation, edtWordSpanish, edtMeaning, "PALABRAS");
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

    private WordSpanish generateWordSpanish(String date, String word, String meaning){
        WordSpanish wordSpanish = new WordSpanish();
        wordSpanish.setDate(date);
        wordSpanish.setWord(word);
        wordSpanish.setMeaning(meaning);
        return wordSpanish;
    }

    /*LISTA PALABRAS: alacena
    LISTA PALABRAS: apear
    LISTA PALABRAS: ballesta
    LISTA PALABRAS: banal
    LISTA PALABRAS: barahúnda, baraúnda
    LISTA PALABRAS: bodoque
    LISTA PALABRAS: burdégano
    LISTA PALABRAS: demagogia
    LISTA PALABRAS: dialéctica
    LISTA PALABRAS: efluvio
    LISTA PALABRAS: estrambótico
    LISTA PALABRAS: estupefacto
    LISTA PALABRAS: etéreo(a)
    LISTA PALABRAS: impío(a)
    LISTA PALABRAS: iridiscencia
    LISTA PALABRAS: mefítico
    LISTA PALABRAS: oligarquía
    LISTA PALABRAS: ominoso
    LISTA PALABRAS: onírico
    LISTA PALABRAS: palurdo(a)
    LISTA PALABRAS: peonza
    LISTA PALABRAS: promontorio
    LISTA PALABRAS: rufián
    LISTA PALABRAS: soporífero
    LISTA PALABRAS: subterfugio
    LISTA PALABRAS: taciturno
    LISTA PALABRAS: tahúr
    LISTA PALABRAS: tirria
    LISTA PALABRAS: tórrido
    LISTA PALABRAS: volatería
    LISTA PALABRAS: vítreo(a)*/
}