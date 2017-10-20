package com.matheus.mybooks.bookUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matheus.mybooks.R;
import com.matheus.mybooks.model.Book;

import java.util.ArrayList;

/**
 * Created by matheus on 19/10/17.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksViewHolder> {

    private ArrayList<Book> books;
    private Context context;

    public BooksAdapter(ArrayList<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.lista_livros, parent, false);

        BooksViewHolder holder = new BooksViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BooksViewHolder holder, int position) {
        holder.textViewTitulo.setText(books.get(position).getTitulo());
        holder.textViewDescricao.setText(books.get(position).getDescricao());
    }


    @Override
    public int getItemCount() {
        return books.size();
    }
}
