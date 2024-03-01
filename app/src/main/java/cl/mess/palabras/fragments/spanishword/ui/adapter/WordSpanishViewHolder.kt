package cl.mess.palabras.fragments.spanishword.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cl.mess.palabras.R
import cl.mess.palabras.fragments.spanishword.data.model.SpanishWord

class WordSpanishViewHolder(
    itemView: View,
    private val onClickWord: WordSpanishAdapter.OnClickWord
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val tvWord: TextView = itemView.findViewById(R.id.tvWord)
    private val tvMeaning: TextView = itemView.findViewById(R.id.tv_meaning)

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(spanishWord: SpanishWord) {
        tvWord.text = spanishWord.word
        tvMeaning.text = spanishWord.meaning
        setBackgroundColor()
    }

    override fun onClick(view: View) {
        val clickedWord = adapterPosition
        onClickWord.clickListWords(clickedWord)
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
