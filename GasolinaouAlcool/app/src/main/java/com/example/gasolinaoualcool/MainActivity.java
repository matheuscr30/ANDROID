package com.example.gasolinaoualcool;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText precoAlcool;
    private EditText precoGasolina;
    private TextView textoResultado;
    private Button btn_verificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        precoAlcool = (EditText)findViewById(R.id.precoAlcoolId);
        precoGasolina = (EditText)findViewById(R.id.precoGasolinaId);
        textoResultado = (TextView)findViewById(R.id.textoResultado);
        btn_verificar = (Button)findViewById(R.id.btn_verificar);

        btn_verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoPrecoAlcool = precoAlcool.getText().toString();
                String textoPrecoGasolina = precoGasolina.getText().toString();

                Double valorAlcool = Double.parseDouble(textoPrecoAlcool);
                Double valorGasolina = Double.parseDouble(textoPrecoGasolina);

                Double resultado = valorAlcool/valorGasolina;

                if(resultado >= 0.7){
                    textoResultado.setText("É melhor utilizar Gasolina");
                } else{
                    textoResultado.setText("É melhor utilizar Álcool");
                }

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(precoAlcool.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                textoResultado.requestFocus();
            }
        });
    }
}
