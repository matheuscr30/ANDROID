package com.matheus.instagram.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matheus.instagram.R;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by matheus on 13/09/17.
 */

public class FeedUsuariosAdapter extends RecyclerView.Adapter<FeedUsuariosViewHolder> {
    private ArrayList<ParseObject> postagens;
    private Context context;

    public FeedUsuariosAdapter(ArrayList<ParseObject> postagens, Context context) {
        this.postagens = postagens;
        this.context = context;
    }

    @Override
    public FeedUsuariosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.lista_postagem, parent, false);

        FeedUsuariosViewHolder holder = new FeedUsuariosViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(FeedUsuariosViewHolder holder, int position) {

        FeedUsuariosViewHolder viewHolder = (FeedUsuariosViewHolder) holder;
        ParseObject parseObject = postagens.get(position);

        Picasso.with( context )
                .load( parseObject.getParseFile("imagem").getUrl() )
                .fit()
                .into( viewHolder.imagem );
    }

    @Override
    public int getItemCount() {
        return postagens.size();
    }
}
