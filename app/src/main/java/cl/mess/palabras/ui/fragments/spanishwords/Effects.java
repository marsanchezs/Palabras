package cl.mess.palabras.ui.fragments.spanishwords;

import static cl.mess.palabras.ui.fragments.Constants.NOK;
import static cl.mess.palabras.ui.fragments.Constants.OK;
import static cl.mess.palabras.ui.fragments.Constants.WORDS;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import cl.mess.palabras.R;
import cl.mess.palabras.bd.Delegate;
import cl.mess.palabras.databinding.DialogAddBinding;
import cl.mess.palabras.databinding.DialogDetailBinding;
import cl.mess.palabras.model.WordSpanish;
import cl.mess.palabras.utilities.Utilities;

public class Effects {
    private final Delegate delegate = new Delegate();
    private final Utilities utilities = new Utilities();

    public void showDialogAdd(Context context, LayoutInflater inflater, DialogCallback callback) {
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogAddBinding dialogBinding = DialogAddBinding.inflate(inflater);
        builder.setView(dialogBinding.getRoot());
        dialog = builder.create();
        ImageView ivTitle = dialogBinding.ivTitle;
        TextView tvTitle = dialogBinding.tvTitle;
        EditText etSpanishWord = dialogBinding.etSpanishWord;
        EditText etMeaning = dialogBinding.etMeaning;
        ImageButton ibAccept = dialogBinding.ibAccept;
        ImageButton ibCancel = dialogBinding.ibCancel;
        ivTitle.setImageResource(R.drawable.ic_words_spanish);
        tvTitle.setText(R.string.word_spanish);
        ibAccept.setOnClickListener(view -> {
            if (utilities.validation(etSpanishWord, etMeaning).equals(OK)) {
                String word = etSpanishWord.getText().toString();
                String meaning = etMeaning.getText().toString();
                WordSpanish wordSpanish = generateWordSpanish(utilities.getDate(), word, meaning);
                String message;
                if (delegate.validateWordSpanish(context, wordSpanish)) {
                    message = context.getString(R.string.the_word) + " " + wordSpanish.getWord()
                            + " " + context.getString(R.string.it_is_already_added);
                    utilities.showMessage(context, message, NOK);
                    etSpanishWord.requestFocus();
                } else {
                    if (delegate.addWordSpanish(context, wordSpanish).equals(OK)) {
                        callback.onDialogResult(true);
                        message = context.getString(R.string.the_word) + " " + wordSpanish.getWord()
                                + " " + context.getString(R.string.has_been_added);
                        utilities.showMessage(context, message, OK);
                        dialog.dismiss();
                    } else {
                        message = context.getString(R.string.could_not_add_the_word) + " " +
                                wordSpanish.getWord();
                        utilities.showMessage(context, message, NOK);
                        etSpanishWord.requestFocus();
                    }
                }
            } else {
                utilities.showMessages(
                        context,
                        utilities.validation(etSpanishWord, etMeaning),
                        etSpanishWord,
                        etMeaning,
                        WORDS
                );
            }
        });
        ibCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void showDialogDetail(
            Context context,
            LayoutInflater inflater,
            WordSpanish spanishWord,
            DialogCallback callback
    ) {
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogDetailBinding dialogBinding = DialogDetailBinding.inflate(inflater);
        builder.setView(dialogBinding.getRoot());
        dialog = builder.create();
        TextView tvSpanishWord = dialogBinding.tvTitle;
        TextView tvMeaning = dialogBinding.tvMeaning;
        ImageButton ibEdit = dialogBinding.ibEdit;
        ImageButton ibDelete = dialogBinding.ibDelete;
        tvSpanishWord.setText(spanishWord.getWord());
        tvMeaning.setText(spanishWord.getMeaning());
        ibEdit.setOnClickListener(view -> showDialogEdit(context, inflater, spanishWord, dialog, callback));
        ibDelete.setOnClickListener(view -> showDialogDelete(context, inflater, spanishWord, dialog, callback));
        dialog.show();
    }

    private void showDialogEdit(
            Context context,
            LayoutInflater inflater,
            WordSpanish wordSpanish,
            AlertDialog dialogDetail,
            DialogCallback callback
    ) {
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogAddBinding dialogBinding = DialogAddBinding.inflate(inflater);
        builder.setView(dialogBinding.getRoot());
        dialog = builder.create();
        ImageView ivTitle = dialogBinding.ivTitle;
        TextView tvTitle = dialogBinding.tvTitle;
        EditText etSpanishWord = dialogBinding.etSpanishWord;
        EditText etMeaning = dialogBinding.etMeaning;
        ImageButton ibAccept = dialogBinding.ibAccept;
        ImageButton ibCancel = dialogBinding.ibCancel;
        ivTitle.setImageResource(R.drawable.ic_words_spanish);
        ivTitle.setImageResource(R.drawable.edit);
        tvTitle.setText(R.string.edit);
        etSpanishWord.setText(wordSpanish.getWord());
        etMeaning.setText(wordSpanish.getMeaning());
        etSpanishWord.requestFocus();
        ibAccept.setOnClickListener(view -> {
            if (utilities.validation(etSpanishWord, etMeaning).equals(OK)) {
                String word = etSpanishWord.getText().toString();
                String meaning = etMeaning.getText().toString();
                WordSpanish newWordSpanish = generateWordSpanish(utilities.getDate(), word, meaning);
                String message;
                if (delegate.validateWordSpanish(context, newWordSpanish)) {
                    message = context.getString(R.string.the_word) + " " + wordSpanish.getWord()
                            + " " + context.getString(R.string.it_is_already_added);
                    utilities.showMessage(context, message, NOK);
                    etSpanishWord.requestFocus();
                } else {
                    if (delegate.updateWordSpanish(context, wordSpanish, newWordSpanish).equals(OK)) {
                        callback.onDialogResult(true);
                        message = context.getString(R.string.the_word) + " " + newWordSpanish.getWord()
                                + " " + context.getString(R.string.has_been_updated);
                        utilities.showMessage(context, message, OK);
                        dialog.dismiss();
                        dialogDetail.dismiss();
                    } else {
                        message = context.getString(R.string.could_not_update_the_word) + " " +
                                wordSpanish.getWord();
                        utilities.showMessage(context, message, NOK);
                        etSpanishWord.requestFocus();
                    }
                }
            } else {
                utilities.showMessages(
                        context,
                        utilities.validation(etSpanishWord, etMeaning),
                        etSpanishWord,
                        etMeaning,
                        WORDS
                );
            }
        });
        ibCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void showDialogDelete(
            Context context,
            LayoutInflater inflater,
            WordSpanish wordSpanish,
            AlertDialog dialogDetail,
            DialogCallback callback
    ) {
        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogDetailBinding dialogBinding = DialogDetailBinding.inflate(inflater);
        builder.setView(dialogBinding.getRoot());
        dialog = builder.create();
        ImageView ivTitle = dialogBinding.ivTitle;
        TextView tvTitle = dialogBinding.tvTitle;
        TextView tvMessage = dialogBinding.tvMeaning;
        ImageButton ibAccept = dialogBinding.ibEdit;
        ImageButton ibCancel = dialogBinding.ibDelete;
        ivTitle.setImageResource(R.drawable.delete);
        tvTitle.setText(R.string.delete);
        String ask = context.getString(R.string.safe_to_delete_the_word) + " " + wordSpanish.getWord() + "?";
        tvMessage.setText(ask);
        ibAccept.setImageResource(R.drawable.ic_accept);
        ibCancel.setImageResource(R.drawable.ic_cancel);
        ibAccept.setOnClickListener(view -> {
            String message;
            if (delegate.deleteWordSpanish(context, wordSpanish).equals(OK)) {
                callback.onDialogResult(true);
                message = context.getString(R.string.the_word) + " " + wordSpanish.getWord()
                        + " " + context.getString(R.string.has_been_removed);
                utilities.showMessage(context, message, OK);
                dialog.dismiss();
                dialogDetail.dismiss();
            } else {
                message = context.getString(R.string.could_not_delete_the_word) + " " + wordSpanish.getWord();
                utilities.showMessage(context, message, NOK);
            }
        });
        ibCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private WordSpanish generateWordSpanish(String date, String word, String meaning) {
        WordSpanish wordSpanish = new WordSpanish();
        wordSpanish.setDate(date);
        wordSpanish.setWord(word);
        wordSpanish.setMeaning(meaning);
        return wordSpanish;
    }
}
