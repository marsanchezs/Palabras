package cl.mess.palabras.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.mess.palabras.R;
import cl.mess.palabras.model.WordEnglish;

public class AdapterWordEnglish extends RecyclerView.Adapter<AdapterWordEnglish.ViewHolder>{
    private ArrayList<WordEnglish> words;
    final private AdapterWordEnglish.onClickWord onClickWord;

    public AdapterWordEnglish(ArrayList<WordEnglish> words_, AdapterWordEnglish.onClickWord onClickWord_) {
        this.words = words_;
        this.onClickWord = onClickWord_;
    }

    public interface onClickWord{
        void clickListWords(int clickedWord);
    }

    @Override
    public AdapterWordEnglish.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_word_english, parent, false);
        AdapterWordEnglish.ViewHolder viewHolder = new AdapterWordEnglish.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterWordEnglish.ViewHolder holder, int position) {
        String word = words.get(position).getWord();
        String translation = words.get(position).getTranslation();
        holder.tvWord.setText(word);
        holder.tvTranslation.setText(translation);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tvWord;
        private final TextView tvTranslation;
        public ViewHolder(View v) {
            super(v);
            tvWord = (TextView) v.findViewById(R.id.tvWord);
            tvTranslation = (TextView) v.findViewById(R.id.tvTranslation);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedWord = getAdapterPosition();
            onClickWord.clickListWords(clickedWord);
        }
    }

    public void filter(ArrayList<WordEnglish> list) {
        //System.out.println("LISTA FILTRADA: "+lista.toString());
        for(int i = 0; i < list.size(); i++){
            System.out.println("LISTA FILTRADA: "+list.get(i).getWord());
        }
        this.words = list;
        notifyDataSetChanged();
    }
}
