package com.matheus.instagram.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.matheus.instagram.R;
import com.matheus.instagram.util.ParseErrors;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextUsuarioCadastro;
    private EditText editTextEmailCadastro;
    private EditText editTextSenhaCadastro;
    private TextView textViewFacaLogin;
    private Button buttonCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CadastroActivity.this,R.color.colorStatusBar));

        editTextUsuarioCadastro = (EditText)findViewById(R.id.editTextUsuarioCadastro);
        editTextEmailCadastro = (EditText)findViewById(R.id.editTextEmailCadastro);
        editTextSenhaCadastro = (EditText)findViewById(R.id.editTextSenhaCadastro);
        textViewFacaLogin = (TextView)findViewById(R.id.textViewFacaLogin);
        buttonCadastrar = (Button)findViewById(R.id.buttonCadastrar);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });
        textViewFacaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirLoginUsuario();
            }
        });
    }

    private void cadastrarUsuario(){
        //Cria Objeto Usuario

        ParseUser usuario = new ParseUser();
        usuario.setUsername(editTextUsuarioCadastro.getText().toString());
        usuario.setEmail(editTextEmailCadastro.getText().toString());
        usuario.setPassword(editTextSenhaCadastro.getText().toString());
        Log.i("TUDO CERTO", "OK");
        usuario.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(CadastroActivity.this, "Usuário Cadastrado com Sucesso", Toast.LENGTH_LONG).show();
                    abrirLoginUsuario();
                } else {
                    ParseErrors parseErrors = new ParseErrors();
                    String erro = parseErrors.getError( e.getCode() );
                    Snackbar.make(buttonCadastrar,erro, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
