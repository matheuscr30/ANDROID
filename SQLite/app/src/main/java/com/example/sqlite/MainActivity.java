package com.example.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);

            //Criar Tabela
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS pessoas(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome VARCHAR, idade INT(3) )");

            //Deletar Tabela
            //bancoDados.execSQL("DROP TABLE pessoas");

            //Inserir Dados
            bancoDados.execSQL("INSERT INTO pessoas (nome, idade) VALUES ('Mariana', 18)");
            bancoDados.execSQL("INSERT INTO pessoas (nome, idade) VALUES ('Tiago', 50)");


            //Atualizar Dados
            //bancoDados.execSQL("UPDATE pessoas SET nome = 'Marcelo' WHERE nome = 'Marcos'");

            //Deletar Dados
            //bancoDados.execSQL("DELETE FROM pessoas WHERE nome = 'Marcelo'");

            //Recuperar Dados
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM pessoas", null);

            int indiceColunaNome = cursor.getColumnIndex("nome");
            int indiceColunaIdade = cursor.getColumnIndex("idade");
            int indiceColunaId = cursor.getColumnIndex("id");

            cursor.moveToFirst();

            while (cursor != null) {

                Log.i("RESULTADO - id: ", cursor.getString(indiceColunaId));
                Log.i("RESULTADO - nome: ", cursor.getString(indiceColunaNome));
                Log.i("RESULTADO - idade: ", cursor.getString(indiceColunaIdade));
                cursor.moveToNext();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}