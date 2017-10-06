package com.matheus.instagram.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.matheus.instagram.R;

/**
 * Created by matheus on 13/09/17.
 */

public class FeedUsuariosViewHolder extends RecyclerView.ViewHolder {

    final ImageView imagem;

    public FeedUsuariosViewHolder(View itemView) {
        super(itemView);
        imagem = itemView.findViewById(R.id.image_lista_postagem);
    }

}
