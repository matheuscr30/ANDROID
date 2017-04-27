package com.example.checkboxaula;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBoxCao;
    private CheckBox checkBoxGato;
    private CheckBox checkBoxPapagaio;
    private Button botaoMostrar;
    private TextView textoExibicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBoxCao = (CheckBox)findViewById(R.id.checkBoxCaoId);
        checkBoxGato = (CheckBox)findViewById(R.id.checkBoxGatoId);
        checkBoxPapagaio = (CheckBox)findViewById(R.id.checkBoxPapagaioId);
        botaoMostrar = (Button)findViewById(R.id.botaoId);
        textoExibicao = (TextView)findViewById(R.id.textoResultadoId);

        botaoMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itensSelecionados = "";

                itensSelecionados = "Item: " + checkBoxCao.getText() + " Status: " + checkBoxCao.isChecked() + "\n";
                itensSelecionados += "Item: " + checkBoxGato.getText() + " Status: " + checkBoxGato.isChecked() + "\n";
                itensSelecionados += "Item: " + checkBoxPapagaio.getText() + " Status: " + checkBoxPapagaio.isChecked() + "\n";

                textoExibicao.setText(itensSelecionados);
            }
        });

    }
}
