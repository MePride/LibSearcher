package libsearcher.mepride.android.librarysearcher.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import libsearcher.mepride.android.librarysearcher.R;
import libsearcher.mepride.android.librarysearcher.model.Book;
import libsearcher.mepride.android.librarysearcher.model.UrlModel;
import libsearcher.mepride.android.librarysearcher.view.activity.DetailActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private Context mContext;
    private List<Book> mBooksList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View BookView;
        @BindView(R.id.bookArticle)
        TextView article;

        public ViewHolder(View view){
            super(view);
            BookView = view;
            ButterKnife.bind(this,view);
        }
    }
    public SearchAdapter(List<Book> booksList){
        mBooksList = booksList;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_books,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Book books = mBooksList.get(holder.getAdapterPosition());
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("href", UrlModel.ChaoXing + books.getHref());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Book books = mBooksList.get(position);
        holder.article.setText(books.getTitle());
    }
    @Override
    public int getItemCount(){
        return mBooksList.size();
    }
}
