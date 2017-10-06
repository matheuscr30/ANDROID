package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.MensagemAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Conversa;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference databaseReference;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    //dados do destinatario
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    //dados do remetente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar)findViewById(R.id.tb_conversa);
        editMensagem = (EditText)findViewById(R.id.edit_mensagem);
        btMensagem = (ImageButton)findViewById(R.id.bt_enviar);
        listView = (ListView)findViewById(R.id.lv_conversas);

        //dados do usuario logado
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetente = preferencias.getNome();

        Bundle extra = getIntent().getExtras();

        if(extra != null){
            nomeUsuarioDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.codificarBase64(emailDestinatario);
        }

        //Configura Toolbar
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        //Monta listview e adapter
        mensagens = new ArrayList<>();
        /*
        adapter = new ArrayAdapter(
                ConversaActivity.this,
                android.R.layout.simple_list_item_1,
                mensagens
        );*/
        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);
        listView.setAdapter(adapter);

        //Recuperar mensagens do firebase
        databaseReference = ConfiguracaoFirebase.getFirebase()
                                .child("mensagens")
                                .child(idUsuarioRemetente)
                                .child(idUsuarioDestinatario);

        //Cria listener para mensagens
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensagens.clear();

                //Recupera mensagens
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListenerMensagem);

        //Enviar mensagem
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoMensagem = editMensagem.getText().toString();

                if(textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar", Toast.LENGTH_LONG).show();
                } else {

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario( idUsuarioRemetente );
                    mensagem.setMensagem( textoMensagem );

                    //Salvar mensagem para o remetente
                    Boolean retornoMensagemRemetente =
                            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                    if(!retornoMensagemRemetente){
                        Snackbar.make(
                                view,
                                "Problema ao Enviar Mensagem ao Remetente!!",
                                Snackbar.LENGTH_LONG).show();
                    } else {

                        //Salvar mensagem para o destinatario
                        Boolean retornoMensagemDestinatario =
                                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                        if(!retornoMensagemDestinatario){
                            Snackbar.make(
                                    view,
                                    "Problema ao Enviar Mensagem ao Destinatario!!",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }

                    //Salvar conversa para o remetente
                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioDestinatario);
                    conversa.setNome(nomeUsuarioDestinatario);
                    conversa.setMensagem(textoMensagem);
                    Boolean retornoConversaRemetente
                        = salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, conversa);

                    if(!retornoConversaRemetente){
                        Snackbar.make(
                                view,
                                "Problema ao Salvar Conversa!!",
                                Snackbar.LENGTH_LONG
                        ).show();
                    } else {

                        //Salvar conversa para o destinatario
                        conversa.setIdUsuario(idUsuarioRemetente);
                        conversa.setNome(nomeUsuarioRemetente);
                        conversa.setMensagem(textoMensagem);
                        Boolean retornoConversaDestinatario
                                = salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, conversa);

                        if(!retornoConversaDestinatario){
                            Snackbar.make(
                                    view,
                                    "Problema ao Salvar Conversa!!",
                                    Snackbar.LENGTH_LONG
                            ).show();
                        }
                    }

                    editMensagem.setText("");
                }
            }
        });
    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try{

            databaseReference = ConfiguracaoFirebase.getFirebase().child("mensagens");

            databaseReference.child(idRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa){
        try{

            databaseReference = ConfiguracaoFirebase.getFirebase().child("conversas");
            databaseReference.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerMensagem);
    }
}
