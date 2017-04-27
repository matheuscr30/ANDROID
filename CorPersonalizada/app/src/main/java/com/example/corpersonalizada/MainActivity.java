package com.example.corpersonalizada;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private static final String ARQUIVO_PREFERENCIAS = "ArquivoPreferencias";
    private RadioGroup radioGroup;
    private RadioButton radioButtonSelecionado;
    private Button botao;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        botao = (Button)findViewById(R.id.botaoId);
        relativeLayout = (RelativeLayout)findViewById(R.id.layoutId);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int idRadioButtonEscolhido = radioGroup.getCheckedRadioButtonId();

                if(idRadioButtonEscolhido > 0) {
                    radioButtonSelecionado = (RadioButton) findViewById(idRadioButtonEscolhido);

                    SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String corPreferida = radioButtonSelecionado.getText().toString();
                    editor.putString("corPreferida", corPreferida);
                    editor.commit();
                    setBackground(corPreferida);
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIAS, 0);
        String cor = sharedPreferences.getString("corPreferida", "Branco");
        setBackground(cor);

    }

    private void setBackground(String cor){

        switch (cor){
            case "Azul":
                relativeLayout.setBackgroundColor(Color.parseColor("#FF0D1B75"));
                break;
            case "Laranja":
                relativeLayout.setBackgroundColor(Color.parseColor("#FFE24D08"));
                break;
            case "Verde":
                relativeLayout.setBackgroundColor(Color.parseColor("#FF42A42C"));
                break;
            default:
                relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                break;
        }
    }
}
