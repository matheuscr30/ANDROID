package com.example.calculadorasimples;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.R.color.black;

public class MainActivity extends AppCompatActivity {
    private EditText editTextValor1;
    private EditText editTextValor2;
    private EditText editTextResultado;
    private Button buttonSoma;
    private Button buttonSubtracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextValor1 = (EditText)findViewById(R.id.editTextValor1);
        editTextValor2 = (EditText)findViewById(R.id.editTextValor2);
        editTextResultado = (EditText)findViewById(R.id.editTextResultado);
        buttonSoma = (Button)findViewById(R.id.buttonSoma);
        buttonSubtracao = (Button)findViewById(R.id.buttonSubtracao);

        buttonSoma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double v1, v2, res;

                try {
                    v1 = Double.parseDouble(editTextValor1.getText().toString());
                    v2 = Double.parseDouble(editTextValor2.getText().toString());
                    res = v1 + v2;
                    editTextResultado.setText(res.toString());
                }catch (Exception excep) {
                    editTextResultado.setText("ERROR");
                    Snackbar.make(view, "Valores Incorretos", Snackbar.LENGTH_LONG).show();
                }
                finally {
                    editTextResultado.requestFocus();
                }
            }
        });

        buttonSubtracao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Double v1, v2, res;
                try {
                    v1 = Double.parseDouble(editTextValor1.getText().toString());
                    v2 = Double.parseDouble(editTextValor2.getText().toString());
                    res = v1 - v2;
                    editTextResultado.setText(res.toString());
                }catch (Exception excep) {
                    editTextResultado.setText("ERROR");
                    Snackbar.make(view, "Valores Incorretos", Snackbar.LENGTH_LONG).show();
                }
                finally {
                    editTextResultado.requestFocus();
                }

            }
        });
    }

    public void broadcastIntent(View view)
    {
        Intent intent = new Intent();
        intent.setAction("com.example.calculadorasimples.CUSTOM_INTENT");
        sendBroadcast(intent);
    }
}
