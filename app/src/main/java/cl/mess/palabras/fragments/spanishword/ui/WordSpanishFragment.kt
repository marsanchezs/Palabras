package cl.mess.palabras.fragments.spanishword.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cl.mess.palabras.R
import cl.mess.palabras.databinding.FragmentWordsSpanishBinding
import cl.mess.palabras.fragments.spanishword.data.Repository
import cl.mess.palabras.fragments.spanishword.data.model.SpanishWord
import cl.mess.palabras.fragments.spanishword.ui.adapter.WordSpanishAdapter
import cl.mess.palabras.utils.Utils

class WordsSpanishFragment : Fragment(), WordSpanishAdapter.OnClickWord {

    private lateinit var adapter: WordSpanishAdapter
    private lateinit var binding: FragmentWordsSpanishBinding
    private lateinit var context: Context
    private val effects: Effects = Effects()
    private var filteredList: ArrayList<SpanishWord> = ArrayList()
    private var isFiltered: Boolean = false
    private val repository: Repository = Repository()
    private val utils: Utils = Utils()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("WordSpanishFragment")
        binding = FragmentWordsSpanishBinding.inflate(inflater, container, false)
        val view: View = binding.root

        context = requireActivity()
        binding.rvSpanishWords.layoutManager = LinearLayoutManager(requireActivity())
        initRecyclerView()
        binding.ibAddSpanishWord.setOnClickListener {
            effects.showDialogAdd(context = requireActivity(), inflater = layoutInflater) { result ->
                if (result) {
                    initRecyclerView()
                }
            }
        }
        return view
    }

    private fun searchWord(edtSearch: AutoCompleteTextView) {
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                search(s.toString())
                if (s.toString().isNotEmpty()) {
                    isFiltered = true
                }
            }
        })
    }

    private fun search(text: String) {
        filteredList.clear()
        val words = repository.getAllSpanishWords(context = context)
        for (word in words) {
            if (word.word.contains(text)) {
                filteredList.add(word)
            }
        }
        adapter.filter(filteredList)
    }

    override fun clickListWords(clickedWord: Int) {
        val words: ArrayList<SpanishWord> = if (!isFiltered) {
            repository.getAllSpanishWords(context = context)
        } else {
            filteredList
        }

        val date = words[clickedWord].date
        val word = words[clickedWord].word
        val meaning = words[clickedWord].meaning
        //val wordSpanish = generateWordSpanish(date, word, meaning)
        val wordSpanish = utils.generateSpanishWord(date = date, word = word, meaning = meaning)
        effects.showDialogDetail(
            context = requireActivity(),
            inflater = layoutInflater,
            spanishWord = wordSpanish
        ) { result ->
            if (result) {
                initRecyclerView()
            }
        }
    }

    private fun initRecyclerView() {
        val words = repository.getAllSpanishWords(context = context)
        binding.actvSearch.text = null
        if (words.isEmpty()) {
            binding.emptyWordsList.layoutEmptyList.visibility = VISIBLE
            binding.emptyWordsList.tvEmptyList.text = context.getText(R.string.no_words_spanish)
            binding.llScreenWithWords.visibility = GONE
        } else {
            binding.emptyWordsList.layoutEmptyList.visibility = GONE
            binding.llScreenWithWords.visibility = VISIBLE
            adapter = WordSpanishAdapter(words = words, clickWordListener = this)
            binding.rvSpanishWords.adapter = adapter
            searchWord(binding.actvSearch)
        }
    }
}
