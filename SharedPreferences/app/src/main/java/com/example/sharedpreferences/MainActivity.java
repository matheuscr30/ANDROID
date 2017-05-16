package com.example.sharedpreferences;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText textoNome;
    private TextView textoRes;
    private Button botao;

    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoNome = (EditText)findViewById(R.id.textoNomeId);
        textoRes = (TextView)findViewById(R.id.textoResId);
        botao = (Button)findViewById(R.id.botaoId);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(textoNome.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Por favor, preencher o nome", Toast.LENGTH_SHORT).show();
                } else{
                    editor.putString("nome", textoNome.getText().toString());
                    editor.commit();
                    textoRes.setText("Olá, " + textoNome.getText().toString());
                }
            }
        });

        //Recuperar os dados salvos
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        String nomeUsuario = sharedPreferences.getString("nome", "Usuario nao Definido");
        textoRes.setText("Olá, " + nomeUsuario);
    }
}
