package cl.mess.palabras.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.mess.palabras.R;
import cl.mess.palabras.model.WordSpanish;

public class AdapterWordSpanish extends RecyclerView.Adapter<AdapterWordSpanish.ViewHolder>{
    private ArrayList<WordSpanish> words;
    final private AdapterWordSpanish.onClickWord onClickWord;

    public AdapterWordSpanish(ArrayList<WordSpanish> words_, AdapterWordSpanish.onClickWord onClickWord_) {
        this.words = words_;
        this.onClickWord = onClickWord_;
    }

    public interface onClickWord{
        void clickListWords(int clickedWord);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_word_spanish, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String word = words.get(position).getWord();
        String meaning = words.get(position).getMeaning();
        holder.tvWord.setText(word);
        holder.tvMeaning.setText(meaning);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tvWord;
        private final TextView tvMeaning;
        public ViewHolder(View v) {
            super(v);
            tvWord = (TextView) v.findViewById(R.id.tvWord);
            tvMeaning = (TextView) v.findViewById(R.id.tvMeaning);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedWord = getAdapterPosition();
            onClickWord.clickListWords(clickedWord);
        }
    }

    public void filter(ArrayList<WordSpanish> list) {
        //System.out.println("LISTA FILTRADA: "+lista.toString());
        for(int i = 0; i < list.size(); i++){
            System.out.println("LISTA FILTRADA: "+list.get(i).getWord());
        }
        this.words = list;
        notifyDataSetChanged();
    }
}
