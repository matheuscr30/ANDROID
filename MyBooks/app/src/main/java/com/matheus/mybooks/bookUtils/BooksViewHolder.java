package com.matheus.mybooks.bookUtils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matheus.mybooks.R;

/**
 * Created by matheus on 19/10/17.
 */

public class BooksViewHolder extends RecyclerView.ViewHolder {

    final ImageView imagemLivro;
    final TextView textViewTitulo;
    final TextView textViewDescricao;

    public BooksViewHolder(View itemView) {
        super(itemView);
        imagemLivro = itemView.findViewById(R.id.imagem_livro);
        textViewTitulo = itemView.findViewById(R.id.titulo_livro);
        textViewDescricao = itemView.findViewById(R.id.descricao_livro);
    }
}
