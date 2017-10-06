package com.matheus.instagram.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.matheus.instagram.R;

/**
 * Created by matheus on 10/09/17.
 */

public class UsuariosViewHolder extends RecyclerView.ViewHolder {

    final TextView tv_username;

    public UsuariosViewHolder(View itemView) {
        super(itemView);
        tv_username = itemView.findViewById(R.id.tv_username);
    }
}
