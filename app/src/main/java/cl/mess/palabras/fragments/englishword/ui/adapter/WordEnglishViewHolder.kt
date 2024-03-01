package cl.mess.palabras.fragments.englishword.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cl.mess.palabras.R
import cl.mess.palabras.model.WordEnglish

class WordEnglishViewHolder(
    itemView: View,
    private val onClickWord: WordEnglishAdapter.OnClickEnglishWord
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val tvWord: TextView = itemView.findViewById(R.id.tvWord)
    private val tvTranslation: TextView = itemView.findViewById(R.id.tvTranslation)

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(englishWord: WordEnglish) {
        tvWord.text = englishWord.word
        tvTranslation.text = englishWord.translation
        setBackgroundColor()
    }

    override fun onClick(view: View) {
        val clickedWord = adapterPosition
        onClickWord.clickListEnglishWords(clickedEnglishWord = clickedWord)
    }

    private fun setBackgroundColor() {
        val backgroundColor = if (adapterPosition % 2 == 0) {
            ContextCompat.getColor(itemView.context, R.color.light_gray)
        } else {
            ContextCompat.getColor(itemView.context, R.color.dark_gray)
        }
        itemView.setBackgroundColor(backgroundColor)
    }
}