package com.example.signos;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ListView listaSignos;
    private String[] signos = {
        "Àries", "Touro", "Gêmeos", "Câncer", "Leão",
        "Virgem", "Libra", "Escorpião", "Sagitário", "Capricórnio",
        "Aquário", "Peixes"
    };

    private String[] perfis = {
            "Arianos sao animados, independentes, individualistas, dinâmicos, corajosos e aventureiros",
            "Taurinos sao positivos, pacientes, determinados, noturnos, leais e românticos",
            "Geminianos são positivos, mutáveis, racionais e bastante voláteis",
            "...", "...", "...", "...", "...", "...", "...", "...", "..."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaSignos = (ListView)findViewById(R.id.listViewId);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                signos
        );

        listaSignos.setAdapter(adaptador);

        listaSignos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int codigoPosicao = position;
                //Toast.makeText(getApplicationContext(), perfis[codigoPosicao], Toast.LENGTH_SHORT).show();
                Snackbar bar = Snackbar.make(view, "Signos não existem", Snackbar.LENGTH_LONG)
                        .setAction("Existe Sim", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(v, "Você é burro?", Snackbar.LENGTH_LONG).show();
                            }
                        });
                bar.show();
            }
        });
    }
}
