package com.matheus.mybooks.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.matheus.mybooks.R;
import com.matheus.mybooks.config.ConfiguracaoFirebase;
import com.matheus.mybooks.config.Permissao;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutSenha;
    private Button botaoLogin;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1, this, permissoesNecessarias);
        inputLayoutEmail = (TextInputLayout)findViewById(R.id.inputLayoutEmailLogin);
        inputLayoutSenha = (TextInputLayout)findViewById(R.id.inputLayoutSenhaLogin);
        botaoLogin = (Button)findViewById(R.id.botaoLogin);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        verificarUsuarioLogado();

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logarUsuario();
            }
        });
    }

    private void logarUsuario(){
        inputLayoutEmail.setErrorEnabled(false);
        inputLayoutSenha.setErrorEnabled(false);

        String email = inputLayoutEmail.getEditText().getText().toString();
        String senha = inputLayoutSenha.getEditText().getText().toString();

        if(email.equals("")) {
            inputLayoutEmail.setErrorEnabled(true);
            inputLayoutEmail.setError("Preenchimento Obrigatório");
        }

        if(senha.equals("")) {
            inputLayoutSenha.setErrorEnabled(true);
            inputLayoutSenha.setError("Preenchimento Obrigatório");
        }

        firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login realizado com sucesso!!", Toast.LENGTH_SHORT).show();
                            verificarUsuarioLogado();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email/Senha incorretos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void verificarUsuarioLogado(){
        if(firebaseAuth.getCurrentUser() != null){
            abrirMainActivity();
        }
    }

    private void abrirMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroActivity(View v){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultado : grantResults) {

            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }

    }

    public void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar esse app é necessário aceitar as permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
