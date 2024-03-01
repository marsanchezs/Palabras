package cl.mess.palabras.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cl.mess.palabras.R
import cl.mess.palabras.fragments.spanishword.data.model.SpanishWord
import cl.mess.palabras.utils.Utils.Constants.NOK
import cl.mess.palabras.utils.Utils.Constants.OK
import cl.mess.palabras.utils.Utils.Constants.ONE
import cl.mess.palabras.utils.Utils.Constants.THREE
import cl.mess.palabras.utils.Utils.Constants.TWO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utils {
    fun generateSpanishWord(date: String, word: String, meaning: String) =
        SpanishWord(
            date = date,
            word = word,
            meaning = meaning
        )

    fun getDate(): String {
        val date = Date()
        val formatDate = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
        return formatDate.format(date)
    }

    fun validation(etWord: EditText, etMeaning: EditText): String {
        val word = etWord.text.toString()
        val meaning = etMeaning.text.toString()
        return when {
            word.isEmpty() && meaning.isEmpty() -> ONE
            word.isEmpty() -> TWO
            meaning.isEmpty() -> THREE
            else -> OK
        }
    }

    fun showMessages(context: Context, response: String, etWord: EditText, etMeaning: EditText, screen: String) {
        val message: String
        when (response) {
            ONE -> {
                message = when (screen) {
                    Constants.SPANISH_WORDS -> context.getString(R.string.enter_a_word_and_its_meaning)
                    Constants.SPANISH_QUOTES -> context.getString(R.string.enter_a_quote_and_its_author)
                    Constants.ENGLISH_WORDS -> context.getString(R.string.enter_an_word_or_phrase_in_english_and_its_meaning)
                    else -> ""
                }
                showCustomToast(context = context, message = message, iconType = NOK)
                etWord.requestFocus()
            }
            TWO -> {
                message = when (screen) {
                    Constants.SPANISH_WORDS -> context.getString(R.string.enter_a_word)
                    Constants.SPANISH_QUOTES -> context.getString(R.string.enter_a_quote)
                    Constants.ENGLISH_WORDS -> context.getString(R.string.enter_a_word_or_phrase_in_english)
                    else -> ""
                }
                showCustomToast(context = context, message = message, iconType = NOK)
                etWord.requestFocus()
            }
            THREE -> {
                message = when (screen) {
                    Constants.SPANISH_WORDS -> context.getString(R.string.enter_word_meaning)
                    Constants.SPANISH_QUOTES -> context.getString(R.string.enter_an_author)
                    Constants.ENGLISH_WORDS -> context.getString(R.string.enter_translation_of_the_word_or_phrase)
                    else -> ""
                }
                showCustomToast(context = context, message = message, iconType = NOK)
                etMeaning.requestFocus()
            }
        }
    }

    fun getPreviousWeek(): String {
        val today = Date()
        val oneMoreWeek = Date(today.time - (86400000 * 7))
        val formatDate = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
        return formatDate.format(oneMoreWeek)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("InflateParams")
    fun showCustomToast(context: Context, message: String, iconType: String) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.message, null)

        val ivIcon: ImageView = view.findViewById(R.id.imgMP)
        val tvMessage: TextView = view.findViewById(R.id.txtM1MP)

        if (iconType == OK) {
            ivIcon.setImageResource(R.drawable.ic_accept)
        } else {
            ivIcon.setImageResource(R.drawable.ic_cancel)
        }

        tvMessage.text = message

        Toast(context).apply {
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_LONG
            setView(view)
            show()
        }
    }

    object Constants {
        const val SPANISH_WORDS = "PALABRAS"
        const val SPANISH_QUOTES = "CITAS"
        const val ENGLISH_WORDS = "WORDS"
        const val OK = "OK"
        const val NOK = "NOK"
        const val ONE = "1"
        const val TWO = "2"
        const val THREE = "3"
    }
}
