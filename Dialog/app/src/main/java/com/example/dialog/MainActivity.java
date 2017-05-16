package com.example.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button botao;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botao = (Button)findViewById(R.id.botaoId);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Criar Alert Dialog;
                dialog = new AlertDialog.Builder(MainActivity.this);

                //Configurar o titulo
                dialog.setTitle("Titulo da dialog");

                //Configurar a mensagem
                dialog.setMessage("Mensagem");
                dialog.setCancelable(false);
                dialog.setIcon(android.R.drawable.btn_star_big_on);

                //Botão negativo
                dialog.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Pressionado botao não", Toast.LENGTH_LONG).show();
                            }
                        });

                dialog.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Pressionado botao sim", Toast.LENGTH_LONG).show();
                            }
                        });

                dialog.create();
                dialog.show();

            }
        });
    }
}
