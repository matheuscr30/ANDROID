package com.example.caraoucoroa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView btn_jogar;
    private String[] opcao = {"cara", "coroa"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_jogar = (ImageView)findViewById(R.id.botaoJogarId);

        btn_jogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random randomico = new Random();
                int num = randomico.nextInt(2);

                Intent intent = new Intent(MainActivity.this, SegundaActivity.class);
                intent.putExtra("opcao", opcao[num]);
                startActivity(intent);
            }
        });
    }
}
