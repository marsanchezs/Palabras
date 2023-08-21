package cl.mess.palabras.ui.fragments.spanishwords;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import cl.mess.palabras.bd.Delegate;
import cl.mess.palabras.databinding.FragmentWordsSpanishBinding;
import cl.mess.palabras.model.WordSpanish;
import cl.mess.palabras.ui.adapters.AdapterWordSpanish;

public class FragmentWordsSpanish extends Fragment implements AdapterWordSpanish.onClickWord {

    private Context context;
    private AdapterWordSpanish adapter;
    private ArrayList<WordSpanish> filteredList;
    private boolean isFiltered = false;
    private final Delegate delegate = new Delegate();
    private FragmentWordsSpanishBinding binding;

    private final Effects effects = new Effects();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWordsSpanishBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        context = getActivity();
        binding.rvSpanishWords.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecyclerView();
        binding.ibAddSpanishWord.setOnClickListener(view1 -> effects.showDialogAdd(getActivity(), getLayoutInflater(), result -> {
            if (result) {
                initRecyclerView();
            }
        }));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        ArrayList<WordSpanish> words = delegate.getAllWordsSpanish(context);
        for (WordSpanish word : words) {
            if (word.getWord().contains(text)) {
                filteredList.add(word);
            }
        }
        adapter.filter(filteredList);
    }

    @Override
    public void clickListWords(int clickedWord) {
        ArrayList<WordSpanish> words;
        if (!isFiltered) {
            words = delegate.getAllWordsSpanish(context);
        } else {
            words = filteredList;
        }

        String date = words.get(clickedWord).getDate();
        String word = words.get(clickedWord).getWord();
        String meaning = words.get(clickedWord).getMeaning();
        WordSpanish wordSpanish = generateWordSpanish(date, word, meaning);
        effects.showDialogDetail(getActivity(), getLayoutInflater(), wordSpanish, result -> {
            if (result) {
                initRecyclerView();
            }
        });
    }

    private void initRecyclerView() {
        ArrayList<WordSpanish> words = delegate.getAllWordsSpanish(context);
        binding.actvSearch.setText("");
        if (words.size() == 0) {
            binding.llEmptyScreen.setVisibility(View.VISIBLE);
            binding.llScreenWithWords.setVisibility(View.GONE);
        } else {
            binding.llEmptyScreen.setVisibility(View.GONE);
            binding.llScreenWithWords.setVisibility(View.VISIBLE);
            adapter = new AdapterWordSpanish(words, this);
            binding.rvSpanishWords.setAdapter(adapter);
            searchWord(binding.actvSearch);
        }
    }

    private WordSpanish generateWordSpanish(String date, String word, String meaning) {
        WordSpanish wordSpanish = new WordSpanish();
        wordSpanish.setDate(date);
        wordSpanish.setWord(word);
        wordSpanish.setMeaning(meaning);
        return wordSpanish;
    }
}
