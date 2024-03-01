package cl.mess.palabras.fragments.spanishword.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import cl.mess.palabras.R
import cl.mess.palabras.databinding.DialogAddBinding
import cl.mess.palabras.databinding.DialogDetailBinding
import cl.mess.palabras.fragments.spanishword.data.Repository
import cl.mess.palabras.fragments.spanishword.data.model.SpanishWord
import cl.mess.palabras.fragments.spanishword.ui.Effects.Constants.NOK
import cl.mess.palabras.fragments.spanishword.ui.Effects.Constants.OK
import cl.mess.palabras.fragments.spanishword.ui.Effects.Constants.WORDS
import cl.mess.palabras.utils.Utils

class Effects {
    private val repository = Repository()
    private val utils = Utils()

    fun showDialogAdd(context: Context, inflater: LayoutInflater, callback: DialogCallback) {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        val dialogBinding = DialogAddBinding.inflate(inflater)
        builder.setView(dialogBinding.root)
        dialog = builder.create()

        val ivTitle = dialogBinding.ivTitle
        val tvTitle = dialogBinding.tvTitle
        val etSpanishWord = dialogBinding.etSpanishWord
        val etMeaning = dialogBinding.etMeaning
        val ibAccept = dialogBinding.ibAccept
        val ibCancel = dialogBinding.ibCancel

        ivTitle.setImageResource(R.drawable.ic_words_spanish)
        tvTitle.setText(R.string.word_spanish)

        ibAccept.setOnClickListener {
            if (utils.validation(etWord = etSpanishWord, etMeaning = etMeaning) == OK) {
                val word = etSpanishWord.text.toString()
                val meaning = etMeaning.text.toString()
                val wordSpanish = utils.generateSpanishWord(
                    date = utils.getDate(),
                    word = word,
                    meaning = meaning
                )
                val message: String
                if (repository.validateSpanishWord(context = context, spanishWord = wordSpanish)) {
                    message =
                        context.getString(R.string.the_word) + " " + wordSpanish.word + " " + context.getString(
                            R.string.it_is_already_added
                        )
                    utils.showCustomToast(context = context, message = message, iconType = NOK)
                    etSpanishWord.requestFocus()
                } else {
                    if (repository.addSpanishWord(context = context, spanishWord = wordSpanish) == OK) {
                        callback.onDialogResult(true)
                        message =
                            context.getString(R.string.the_word) + " " + wordSpanish.word + " " + context.getString(
                                R.string.has_been_added
                            )
                        utils.showCustomToast(context = context, message = message, iconType = OK)
                        dialog.dismiss()
                    } else {
                        message =
                            context.getString(R.string.could_not_add_the_word) + " " + wordSpanish.word
                        utils.showCustomToast(context = context, message = message, iconType = NOK)
                        etSpanishWord.requestFocus()
                    }
                }
            } else {
                utils.showMessages(
                    context = context,
                    response = utils.validation(etWord = etSpanishWord, etMeaning = etMeaning),
                    etWord = etSpanishWord,
                    etMeaning = etMeaning,
                    screen = WORDS
                )
            }
        }

        ibCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    fun showDialogDetail(
        context: Context,
        inflater: LayoutInflater,
        spanishWord: SpanishWord,
        callback: DialogCallback
    ) {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        val dialogBinding = DialogDetailBinding.inflate(inflater)
        builder.setView(dialogBinding.root)
        dialog = builder.create()

        val tvSpanishWord = dialogBinding.tvTitle
        val tvMeaning = dialogBinding.tvMeaning
        val ibEdit = dialogBinding.ibEdit
        val ibDelete = dialogBinding.ibDelete

        tvSpanishWord.text = spanishWord.word
        tvMeaning.text = spanishWord.meaning

        ibEdit.setOnClickListener { showDialogEdit(
            context = context,
            inflater = inflater,
            spanishWord = spanishWord,
            dialogDetail = dialog,
            callback = callback
        ) }
        ibDelete.setOnClickListener { showDialogDelete(
            context = context,
            inflater = inflater,
            spanishWord = spanishWord,
            dialogDetail = dialog,
            callback = callback
        ) }

        dialog.show()
    }

    private fun showDialogEdit(
        context: Context,
        inflater: LayoutInflater,
        spanishWord: SpanishWord,
        dialogDetail: AlertDialog,
        callback: DialogCallback
    ) {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        val dialogBinding = DialogAddBinding.inflate(inflater)
        builder.setView(dialogBinding.root)
        dialog = builder.create()

        val ivTitle = dialogBinding.ivTitle
        val tvTitle = dialogBinding.tvTitle
        val etSpanishWord = dialogBinding.etSpanishWord
        val etMeaning = dialogBinding.etMeaning
        val ibAccept = dialogBinding.ibAccept
        val ibCancel = dialogBinding.ibCancel

        ivTitle.setImageResource(R.drawable.ic_words_spanish)
        tvTitle.setText(R.string.edit)
        etSpanishWord.setText(spanishWord.word)
        etMeaning.setText(spanishWord.meaning)
        etSpanishWord.requestFocus()

        ibAccept.setOnClickListener {
            if (utils.validation(etWord = etSpanishWord, etMeaning = etMeaning) == OK) {
                val word = etSpanishWord.text.toString()
                val meaning = etMeaning.text.toString()
                val newWordSpanish = utils.generateSpanishWord(
                    date = utils.getDate(),
                    word = word,
                    meaning = meaning
                )
                val message: String
                if (repository.validateSpanishWord(context = context, spanishWord = newWordSpanish)) {
                    message =
                        context.getString(R.string.the_word) + " " + spanishWord.word + " " + context.getString(
                            R.string.it_is_already_added
                        )
                    utils.showCustomToast(context = context, message = message, iconType = NOK)
                    etSpanishWord.requestFocus()
                } else {
                    if (repository.updateSpanishWord(
                            context = context,
                            spanishWord = spanishWord,
                            newSpanishWord = newWordSpanish
                        ) == OK) {
                        callback.onDialogResult(true)
                        message =
                            context.getString(R.string.the_word) + " " + newWordSpanish.word + " " + context.getString(
                                R.string.has_been_updated
                            )
                        utils.showCustomToast(context = context, message = message, iconType = OK)
                        dialog.dismiss()
                        dialogDetail.dismiss()
                    } else {
                        message =
                            context.getString(R.string.could_not_update_the_word) + " " + spanishWord.word
                        utils.showCustomToast(context = context, message = message, iconType = NOK)
                        etSpanishWord.requestFocus()
                    }
                }
            } else {
                utils.showMessages(
                    context = context,
                    response = utils.validation(etWord = etSpanishWord, etMeaning = etMeaning),
                    etWord = etSpanishWord,
                    etMeaning = etMeaning,
                    screen = WORDS
                )
            }
        }

        ibCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun showDialogDelete(
        context: Context,
        inflater: LayoutInflater,
        spanishWord: SpanishWord,
        dialogDetail: AlertDialog,
        callback: DialogCallback
    ) {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        val dialogBinding = DialogDetailBinding.inflate(inflater)
        builder.setView(dialogBinding.root)
        dialog = builder.create()

        val ivTitle = dialogBinding.ivTitle
        val tvTitle = dialogBinding.tvTitle
        val tvMessage = dialogBinding.tvMeaning
        val ibAccept = dialogBinding.ibEdit
        val ibCancel = dialogBinding.ibDelete

        ivTitle.setImageResource(R.drawable.delete)
        tvTitle.setText(R.string.delete)

        val ask =
            context.getString(R.string.safe_to_delete_the_word) + " " + spanishWord.word + "?"
        tvMessage.text = ask

        ibAccept.setImageResource(R.drawable.ic_accept)
        ibCancel.setImageResource(R.drawable.ic_cancel)

        ibAccept.setOnClickListener {
            val message: String
            if (repository.deleteSpanishWord(context = context, spanishWord = spanishWord) == OK) {
                callback.onDialogResult(true)
                message =
                    context.getString(R.string.the_word) + " " + spanishWord.word + " " + context.getString(
                        R.string.has_been_removed
                    )
                utils.showCustomToast(context = context, message = message, iconType = OK)
                dialog.dismiss()
                dialogDetail.dismiss()
            } else {
                message =
                    context.getString(R.string.could_not_delete_the_word) + " " + spanishWord.word
                utils.showCustomToast(context = context, message = message, iconType = NOK)
            }
        }

        ibCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    object Constants {
        const val NOK = "NOK"
        const val OK = "OK"
        const val WORDS = "PALABRAS"
    }
}
