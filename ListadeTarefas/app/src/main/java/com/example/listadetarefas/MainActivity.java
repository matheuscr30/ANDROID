package com.example.listadetarefas;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText textoTarefa;
    private Button botaoAdicionar;
    private ListView listaTarefas;
    private SQLiteDatabase bancoDados;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> itens;
    private ArrayList<Integer> ids;

    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            textoTarefa = (EditText) findViewById(R.id.textoId);
            botaoAdicionar = (Button) findViewById(R.id.botaoAdicionarId);
            listaTarefas = (ListView) findViewById(R.id.listViewId);

            bancoDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tarefa VARCHAR)");

            botaoAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textoDigitado = textoTarefa.getText().toString();
                    salvarTarefas(textoDigitado);
                }
            });

            listaTarefas.setLongClickable(true);
            listaTarefas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    final int posicaoEscolhida = position;
                    dialog = new AlertDialog.Builder(MainActivity.this);

                    //Configurar o titulo
                    dialog.setTitle("Importante");

                    //Configurar a mensagem
                    dialog.setMessage("Deseja excluir essa tarefa?");
                    dialog.setCancelable(false);
                    dialog.setIcon(android.R.drawable.btn_dialog);

                    dialog.setPositiveButton("Sim",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removerTarefa(ids.get(posicaoEscolhida));
                                }
                            });

                    dialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Tarefa nao deletada", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.create();
                    dialog.show();
                    return true;
                }
            });

            recuperarTarefas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void salvarTarefas(String texto) {
        try {
            if (texto.equals("")) {
                Toast.makeText(MainActivity.this, "Digite uma Tarefa", Toast.LENGTH_SHORT).show();
            } else {
                bancoDados.execSQL("INSERT INTO tarefas (tarefa) VALUES ('" + texto + "') ");
                Toast.makeText(MainActivity.this, "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show();
                recuperarTarefas();
                textoTarefa.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recuperarTarefas() {
        try {
            //Recupera as Tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);
            cursor.moveToFirst();

            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            //Criar Adaptador
            itens = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    itens);
            listaTarefas.setAdapter(arrayAdapter);

            while (cursor != null) {

                Log.i("Resultado - ", "Tarefa: " + cursor.getString(indiceColunaTarefa));
                itens.add(cursor.getString(indiceColunaTarefa));
                ids.add(Integer.parseInt(cursor.getString(indiceColunaId)));
                cursor.moveToNext();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removerTarefa(Integer id) {
        try {

            bancoDados.execSQL("DELETE FROM tarefas WHERE id=" + id);
            recuperarTarefas();
            Toast.makeText(MainActivity.this, "Tarefa deletada com sucesso", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
