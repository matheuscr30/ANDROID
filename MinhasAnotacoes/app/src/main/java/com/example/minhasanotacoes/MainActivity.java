package com.example.minhasanotacoes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private ImageView botaoSalvar;
    private EditText texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoSalvar = (ImageView) findViewById(R.id.botaoSalvarId);
        texto = (EditText)findViewById(R.id.textoId);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoDigitado = texto.getText().toString();
                gravarNoArquivo(textoDigitado);
                Toast.makeText(MainActivity.this, "Anotacao salva com sucesso", Toast.LENGTH_SHORT).show();
            }
        });

        if(lerArquivo() != null){
            texto.setText(lerArquivo());
        }
    }

    private void gravarNoArquivo(String texto){
        try{

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( openFileOutput("anotacao.txt", Context.MODE_PRIVATE) );
            outputStreamWriter.write( texto );
            outputStreamWriter.close();

        }catch(IOException e){
            Log.v("MainActivity", e.toString());
        }
    }

    private String lerArquivo(){
        String resultado = "";

        try{

            //Abrir o arquivo
            InputStream inputStream = openFileInput("anotacao.txt");
            if(inputStream != null){

                //ler o arquivo
                InputStreamReader inputStreamReader = new InputStreamReader( inputStream );

                //Gerar Buffer do arquivo lido
                BufferedReader bufferedReader = new BufferedReader( inputStreamReader );

                //Recuperar Textos do Arquivo
                String linhaArquivo = "";
                while( (linhaArquivo = bufferedReader.readLine()) != null){
                    resultado += linhaArquivo;
                }

                inputStream.close();
            }

        } catch(IOException e){
            Log.v("MainActivity", e.toString());
        }

        return resultado;
    }
}
