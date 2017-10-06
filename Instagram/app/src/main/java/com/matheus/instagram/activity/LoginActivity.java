package com.matheus.instagram.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.matheus.instagram.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsuarioLogin;
    private EditText editTextSenhaLogin;
    private Button buttonLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.colorStatusBar));

        editTextUsuarioLogin = (EditText)findViewById(R.id.editTextUsuarioLogin);
        editTextSenhaLogin = (EditText)findViewById(R.id.editTextSenhaLogin);
        buttonLogar = (Button)findViewById(R.id.buttonLogar);

        //Verificar se o usuario esta logadozz
        verificarUsuarioLogado();

        buttonLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = editTextUsuarioLogin.getText().toString();
                String senha = editTextSenhaLogin.getText().toString();
                verificarLogin(usuario, senha);
            }
        });
    }

    private void verificarLogin(String usuario, String senha){
        ParseUser.logInInBackground(usuario, senha, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Toast.makeText(LoginActivity.this, "Login Realizado com Sucesso", Toast.LENGTH_LONG).show();
                    abrirTelaPrincipal();
                } else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer Login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void verificarUsuarioLogado(){
        if(ParseUser.getCurrentUser() != null)
            abrirTelaPrincipal();
    }

    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}
