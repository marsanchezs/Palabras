package cl.mess.palabras.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.mess.palabras.R;
import cl.mess.palabras.model.Quote;

public class AdapterQuote extends RecyclerView.Adapter<AdapterQuote.ViewHolder>{
    private ArrayList<Quote> quotes;
    final private AdapterQuote.onClickQuote onClickQuote;

    public AdapterQuote(ArrayList<Quote> quotes_, AdapterQuote.onClickQuote onClickQuote_) {
        this.quotes = quotes_;
        this.onClickQuote = onClickQuote_;
    }

    public interface onClickQuote{
        void clickListQuotes(int clickedQuote);
    }

    @Override
    public AdapterQuote.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_quote, parent, false);
        AdapterQuote.ViewHolder viewHolder = new AdapterQuote.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterQuote.ViewHolder holder, int position) {
        String quote = "\""+quotes.get(position).getQuote()+"\"";
        String author = quotes.get(position).getAuthor();
        holder.tvQuote.setText(quote);
        holder.tvAuthor.setText(author);
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tvQuote;
        private final TextView tvAuthor;
        public ViewHolder(View v) {
            super(v);
            tvQuote = (TextView) v.findViewById(R.id.tvQuote);
            tvAuthor = (TextView) v.findViewById(R.id.tv_author);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedQuote = getAdapterPosition();
            onClickQuote.clickListQuotes(clickedQuote);
        }
    }

    public void filter(ArrayList<Quote> list) {
        //System.out.println("LISTA FILTRADA: "+lista.toString());
        for(int i = 0; i < list.size(); i++){
            System.out.println("LISTA FILTRADA: "+list.get(i).getQuote());
        }
        this.quotes = list;
        notifyDataSetChanged();
    }
}
