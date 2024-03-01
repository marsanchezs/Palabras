package cl.mess.palabras.fragments.englishword.ui;

import android.content.Context;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.mess.palabras.R;
import cl.mess.palabras.bd.Delegate;
import cl.mess.palabras.model.WordEnglish;
import cl.mess.palabras.fragments.englishword.ui.adapter.WordEnglishAdapter;
import cl.mess.palabras.utilities.Utilities;

public class FragmentWordsEnglish extends Fragment implements WordEnglishAdapter.OnClickEnglishWord {

    private Context context;
    private WordEnglishAdapter adapter;
    private ArrayList<WordEnglish> filteredList;
    private boolean isFiltered = false;
    private AutoCompleteTextView edtSearch;
    private RecyclerView rvWordEnglish;
    private LinearLayout llv, llv2;
    private final Utilities utilities = new Utilities();
    private final Delegate delegate = new Delegate();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_words_english, container, false);
        context = getActivity();
        llv = view.findViewById(R.id.llvFW);
        llv2 = view.findViewById(R.id.llvFW2);
        edtSearch = view.findViewById(R.id.actv_search);
        ImageButton btnAddWordEnglish = view.findViewById(R.id.btnAddWordEnglish);
        rvWordEnglish = view.findViewById(R.id.rvWordsEnglish);
        rvWordEnglish.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecyclerView();

        btnAddWordEnglish.setOnClickListener(vista -> showDialogAdd());

        return view;
    }

    private void searchWord(final AutoCompleteTextView edtSearch) {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
                if (s.toString().length() > 0) {
                    isFiltered = true;
                }
            }
        });
    }

    private void search(String text) {
        filteredList = new ArrayList<>();
        ArrayList<WordEnglish> words = delegate.getAllWordsEnglish(context);

        for (WordEnglish word : words) {
            if (word.getWord().contains(text)) {
                filteredList.add(word);
            }
        }
        adapter.filter(filteredList);
    }

    @Override
    public void clickListEnglishWords(int clickedWordEnglish) {
        ArrayList<WordEnglish> words;
        if (!isFiltered) {
            words = delegate.getAllWordsEnglish(context);
        } else {
            words = filteredList;
        }

        String date = words.get(clickedWordEnglish).getDate();
        String word = words.get(clickedWordEnglish).getWord();
        String translation = words.get(clickedWordEnglish).getTranslation();
        WordEnglish wordEnglish = generateWordEnglish(date, word, translation);
        showDialogDetail(wordEnglish);
    }

    private void showDialogDetail(WordEnglish wordEnglish) {
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_detail, null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = view.findViewById(R.id.iv_title);
        TextView tvWordEnglish = view.findViewById(R.id.tv_title);
        TextView tvTranslation = view.findViewById(R.id.tv_meaning);
        ImageButton btnEdit = view.findViewById(R.id.ib_edit);
        ImageButton btnDelete = view.findViewById(R.id.ib_delete);

        iv.setImageResource(R.drawable.ic_words_english);
        tvWordEnglish.setText(wordEnglish.getWord());
        tvTranslation.setText(wordEnglish.getTranslation());

        btnEdit.setOnClickListener(view1 -> showDialogEdit(wordEnglish, dialog));

        btnDelete.setOnClickListener(view12 -> showDialogDelete(wordEnglish, dialog));
        dialog.show();
    }

    private void showDialogEdit(WordEnglish wordEnglish, AlertDialog dialogDetail) {
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_add, null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = view.findViewById(R.id.iv_title);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        LinearLayout llvWordSpanish = view.findViewById(R.id.ll_spanish_word);
        LinearLayout llvWordEnglish = view.findViewById(R.id.ll_english_word);
        EditText edtWordEnglish = view.findViewById(R.id.et_english_word);
        EditText edtTranslation = view.findViewById(R.id.et_translation);
        ImageButton btnAccept = view.findViewById(R.id.ib_accept);
        ImageButton btnCancel = view.findViewById(R.id.ib_cancel);

        iv.setImageResource(R.drawable.edit);
        tvTitle.setText(R.string.edit);
        llvWordSpanish.setVisibility(View.GONE);
        llvWordEnglish.setVisibility(View.VISIBLE);
        edtWordEnglish.setText(wordEnglish.getWord());
        edtTranslation.setText(wordEnglish.getTranslation());
        edtWordEnglish.requestFocus();

        btnAccept.setOnClickListener(view1 -> {
            String responseValidation = utilities.validation(edtWordEnglish, edtTranslation);
            if (responseValidation.equals("OK")) {
                String word = edtWordEnglish.getText().toString();
                String translation = edtTranslation.getText().toString();
                WordEnglish newWordEnglish = generateWordEnglish(utilities.getDate(), word, translation);
                boolean responseValidateWordEnglish = delegate.validateWordEnglish(context, newWordEnglish);
                String message;
                if (responseValidateWordEnglish) {
                    message = "La palabra " + wordEnglish.getWord() + " ya se encuentra agregada";
                    utilities.showMessage(context, message, "NOK");
                    edtWordEnglish.requestFocus();
                } else {
                    String responseUpdateWordSpanish = delegate.updateWordEnglish(context, wordEnglish, newWordEnglish);
                    if (responseUpdateWordSpanish.equals("OK")) {
                        message = "La palabra " + newWordEnglish.getWord() + " ha sido actualizada";
                        utilities.showMessage(context, message, "OK");
                        dialog.dismiss();
                        dialogDetail.dismiss();
                        initRecyclerView();
                    } else {
                        message = "No se ha podido actualizar la palabra " + wordEnglish.getWord();
                        utilities.showMessage(context, message, "NOK");
                        edtWordEnglish.requestFocus();
                    }
                }
            } else {
                utilities.showMessages(context, responseValidation, edtWordEnglish, edtTranslation, "WORDS");
            }
        });

        btnCancel.setOnClickListener(view12 -> dialog.dismiss());
        dialog.show();
    }

    private void showDialogDelete(WordEnglish wordEnglish, AlertDialog dialogDetail) {
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_detail, null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = view.findViewById(R.id.iv_title);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvMessage = view.findViewById(R.id.tv_meaning);
        ImageButton btnAccept = view.findViewById(R.id.ib_edit);
        ImageButton btnCancel = view.findViewById(R.id.ib_delete);

        iv.setImageResource(R.drawable.delete);
        tvTitle.setText(R.string.delete);
        String message = "¿Seguro de eliminar la palabra " + wordEnglish.getWord() + "?";
        tvMessage.setText(message);
        btnAccept.setImageResource(R.drawable.ic_accept);
        btnCancel.setImageResource(R.drawable.ic_cancel);

        btnAccept.setOnClickListener(view1 -> {
            String message1;
            String responseDeleteWordEnglish = delegate.deleteWordEnglish(context, wordEnglish);
            if (responseDeleteWordEnglish.equals("OK")) {
                message1 = "La palabra " + wordEnglish.getWord() + " ha sido eliminada";
                utilities.showMessage(context, message1, "OK");
                dialog.dismiss();
                dialogDetail.dismiss();
                initRecyclerView();
            } else {
                message1 = "No se ha podido eliminar la palabra " + wordEnglish.getWord();
                utilities.showMessage(context, message1, "NOK");
            }
        });

        btnCancel.setOnClickListener(view12 -> dialog.dismiss());
        dialog.show();
    }

    private void showDialogAdd() {
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflate = getLayoutInflater();
        View view = inflate.inflate(R.layout.dialog_add, null);
        builder.setView(view);
        dialog = builder.create();
        ImageView iv = view.findViewById(R.id.iv_title);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        LinearLayout llvWordSpanish = view.findViewById(R.id.ll_spanish_word);
        LinearLayout llvWordEnglish = view.findViewById(R.id.ll_english_word);
        EditText edtWordEnglish = view.findViewById(R.id.et_english_word);
        EditText edtTranslation = view.findViewById(R.id.et_translation);
        ImageButton btnAccept = view.findViewById(R.id.ib_accept);
        ImageButton btnCancel = view.findViewById(R.id.ib_cancel);

        iv.setImageResource(R.drawable.ic_words_english);
        tvTitle.setText(R.string.word_english);
        llvWordSpanish.setVisibility(View.GONE);
        llvWordEnglish.setVisibility(View.VISIBLE);

        btnAccept.setOnClickListener(view1 -> {
            String responseValidation = utilities.validation(edtWordEnglish, edtTranslation);
            if (responseValidation.equals("OK")) {
                //utilidades.mostrarMensaje(context, "OK", "OK");
                String word = edtWordEnglish.getText().toString();
                String translation = edtTranslation.getText().toString();
                WordEnglish wordEnglish = generateWordEnglish(utilities.getDate(), word, translation);
                boolean responseValidateWordEnglish = delegate.validateWordEnglish(context, wordEnglish);
                String message;
                if (responseValidateWordEnglish) {
                    message = "La palabra " + wordEnglish.getWord() + " ya se encuentra agregada";
                    utilities.showMessage(context, message, "NOK");
                    edtWordEnglish.requestFocus();
                } else {
                    String responseAddWordEnglish = delegate.addWordEnglish(context, wordEnglish);
                    if (responseAddWordEnglish.equals("OK")) {
                        message = "La palabra " + wordEnglish.getWord() + " ha sido agregada";
                        utilities.showMessage(context, message, "OK");
                        dialog.dismiss();
                        initRecyclerView();
                    } else {
                        message = "No se ha podido agregar la palabra " + wordEnglish.getWord();
                        utilities.showMessage(context, message, "NOK");
                        edtWordEnglish.requestFocus();
                    }
                }
            } else {
                utilities.showMessages(context, responseValidation, edtWordEnglish, edtTranslation, "WORDS");
            }
        });

        btnCancel.setOnClickListener(view12 -> dialog.dismiss());
        dialog.show();
    }

    private WordEnglish generateWordEnglish(String date, String word, String translation) {
        WordEnglish wordEnglish = new WordEnglish();
        wordEnglish.setDate(date);
        wordEnglish.setWord(word);
        wordEnglish.setTranslation(translation);
        return wordEnglish;
    }

    private void initRecyclerView() {
        ArrayList<WordEnglish> words = delegate.getAllWordsEnglish(context);
        System.out.println("Nº DE WORDS : " + words.size());
        edtSearch.setText("");
        if (words.size() == 0) {
            llv.setVisibility(View.VISIBLE);
            llv2.setVisibility(View.GONE);
        } else {
            llv.setVisibility(View.GONE);
            llv2.setVisibility(View.VISIBLE);
            System.out.println("LISTA DE WORDS RV: " + words.get(0));
            adapter = new WordEnglishAdapter(words, this);
            rvWordEnglish.setAdapter(adapter);
            searchWord(edtSearch);
        }
    }
}