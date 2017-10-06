package com.matheus.instagram.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matheus.instagram.R;
import com.matheus.instagram.util.OnItemClicked;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by matheus on 10/09/17.
 */

public class UsuariosAdapter extends RecyclerView.Adapter {

    private ArrayList<ParseUser> usuarios;
    private Context context;
    private OnItemClicked onClick;

    public UsuariosAdapter(ArrayList<ParseUser> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.lista_usuario, parent, false);

        UsuariosViewHolder holder = new UsuariosViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        UsuariosViewHolder viewHolder = (UsuariosViewHolder) holder;
        ParseUser parseUser = usuarios.get(position);

        viewHolder.tv_username.setText(parseUser.getUsername());

        viewHolder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick=onClick;
    }
}
