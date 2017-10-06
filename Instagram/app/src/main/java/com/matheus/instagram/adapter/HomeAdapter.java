package com.matheus.instagram.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.matheus.instagram.R;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 10/09/17.
 */

public class HomeAdapter extends ArrayAdapter<ParseObject> {

    private ArrayList<ParseObject> postagens;
    private Context context;

    public HomeAdapter(@NonNull Context c, @NonNull ArrayList<ParseObject> objects) {
        super(c, 0, objects);
        this.context = c;
        this.postagens = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){

            //Inicializa objetos para montagem da view
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );

            //Monta view a partir do xml
            view = layoutInflater.inflate(R.layout.lista_postagem, parent, false);
        }

        //Verificar se existem Postagens
        if(postagens.size() > 0){

            ImageView imagemPostagem = view.findViewById(R.id.image_lista_postagem);
            ParseObject parseObject = postagens.get(position);

            Picasso.with( context )
                    .load( parseObject.getParseFile("imagem").getUrl() )
                    .fit()
                    .into(imagemPostagem);
        }

        return view;
    }
}