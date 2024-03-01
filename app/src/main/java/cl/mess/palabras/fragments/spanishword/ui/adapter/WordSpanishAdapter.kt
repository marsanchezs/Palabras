package cl.mess.palabras.fragments.spanishword.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cl.mess.palabras.BuildConfig
import cl.mess.palabras.R
import cl.mess.palabras.fragments.spanishword.data.model.SpanishWord
import cl.mess.palabras.model.WordSpanish

class WordSpanishAdapter(
    private val words: ArrayList<SpanishWord>,
    private val clickWordListener: OnClickWord
) :
    RecyclerView.Adapter<WordSpanishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordSpanishViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.view_word_spanish, parent, false)
        return WordSpanishViewHolder(itemView = v, onClickWord = clickWordListener)
    }

    override fun onBindViewHolder(holder: WordSpanishViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    interface OnClickWord {
        fun clickListWords(clickedWord: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(list: ArrayList<SpanishWord>) {
        if (BuildConfig.DEBUG) {
            for (i in 0 until list.size) {
                println("Filter list -> " + list[i].word)
            }
        }
        words.clear()
        words.addAll(list)
        notifyDataSetChanged()
    }
}
