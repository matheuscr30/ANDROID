package br.com.whatsappandroid.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Mensagem;

/**
 * Created by matheus on 18/08/17.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(@NonNull Context c, @NonNull ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //Verifica se a lista esta preenchida
        if( mensagens != null ){

            //Recupera dados do remetente
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Recupera mensagem
            Mensagem mensagem = mensagens.get(position);

            //Monta view a partir do xml
            if(idUsuarioRemetente.equals(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
            } else {
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }


            //Recupera elemento para exibicao
            TextView textoMensagem = (TextView)view.findViewById(R.id.tv_mensagem);
            textoMensagem.setText(mensagem.getMensagem());
        }

        return view;
    }
}
