package cl.mess.palabras.fragments.englishword.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cl.mess.palabras.BuildConfig
import cl.mess.palabras.R
import cl.mess.palabras.model.WordEnglish

class WordEnglishAdapter(
    private val words: ArrayList<WordEnglish>,
    private val clickWordListener: OnClickEnglishWord
) :
    RecyclerView.Adapter<WordEnglishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordEnglishViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.view_word_english, parent, false)
        return WordEnglishViewHolder(itemView = v, onClickWord = clickWordListener)
    }

    override fun onBindViewHolder(holder: WordEnglishViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    interface OnClickEnglishWord {
        fun clickListEnglishWords(clickedEnglishWord: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(list: ArrayList<WordEnglish>) {
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