package com.example.idadedecachorro;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText textoIdade;
    private TextView textoResultadoIdade;
    private Button btnDescobrirIdade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoIdade = (EditText)findViewById(R.id.textoIdade);
        textoResultadoIdade = (TextView)findViewById(R.id.textoResultadoIdade);
        btnDescobrirIdade = (Button)findViewById(R.id.btnDescobrirIdade);

        btnDescobrirIdade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoDigitado = textoIdade.getText().toString();

                if(textoDigitado.isEmpty()){
                    textoResultadoIdade.setText("Nenhum número foi digitado");
                } else{
                    int valor = Integer.parseInt(textoDigitado);
                    int resultadoFinal = valor*7;

                    textoResultadoIdade.setText("A idade do cachorro em anos humanos é: " + resultadoFinal + " anos");
                }
                //Fecha o teclado
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(textoIdade.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                textoResultadoIdade.requestFocus();
            }
        });
    }
}
