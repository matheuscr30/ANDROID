package br.com.whatsappandroid.cursoandroid.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.activity.ConversaActivity;
import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.ConversaAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Conversa> conversas;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //Instanciando Array
        conversas = new ArrayList<>();

        //Criando view
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //Monta ListView e Adapter
        listView = (ListView)view.findViewById(R.id.lv_conversas);
        adapter = new ConversaAdapter(getActivity(), conversas);
        listView.setAdapter(adapter);

        //Recuperar dados do usuario
        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdentificador();

        //Recuperar conversas
        databaseReference = ConfiguracaoFirebase.getFirebase()
                .child("conversas")
                .child(idUsuarioLogado);

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                conversas.clear();
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //Listener para click nos items da ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //Recupera dados a serem passados
                Conversa conversa = conversas.get(i);

                //Envia dados para ConversaActivity
                String emailContato = Base64Custom.decodificarBase64(conversa.getIdUsuario());
                intent.putExtra("nome", conversa.getNome());
                intent.putExtra("email", emailContato);

                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerConversas);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerConversas);
    }
}
