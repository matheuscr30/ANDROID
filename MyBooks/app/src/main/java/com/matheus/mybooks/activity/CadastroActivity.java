package com.matheus.mybooks.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.matheus.mybooks.R;
import com.matheus.mybooks.config.Base64Custom;
import com.matheus.mybooks.config.ConfiguracaoFirebase;
import com.matheus.mybooks.dao.UsuarioDAO;
import com.matheus.mybooks.model.Usuario;

import java.util.Base64;

public class CadastroActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private TextInputLayout inputLayoutNome;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutSenha;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        fab = (FloatingActionButton)findViewById(R.id.fabCadastro);
        inputLayoutNome = (TextInputLayout)findViewById(R.id.inputLayoutNomeCadastro);
        inputLayoutEmail = (TextInputLayout)findViewById(R.id.inputLayoutEmailCadastro);
        inputLayoutSenha = (TextInputLayout)findViewById(R.id.inputLayoutSenhaCadastro);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario(){
        boolean flag = true;
        inputLayoutNome.setErrorEnabled(false);
        inputLayoutEmail.setErrorEnabled(false);
        inputLayoutSenha.setErrorEnabled(false);

        String nome = inputLayoutNome.getEditText().getText().toString();
        String email = inputLayoutEmail.getEditText().getText().toString();
        String senha = inputLayoutSenha.getEditText().getText().toString();

        if(nome.equals("")) {
            inputLayoutNome.setErrorEnabled(true);
            inputLayoutNome.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(email.equals("")) {
            inputLayoutEmail.setErrorEnabled(true);
            inputLayoutEmail.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(senha.equals("")) {
            inputLayoutSenha.setErrorEnabled(true);
            inputLayoutSenha.setError("Preenchimento Obrigatório");
            flag = false;
        }

        if(!flag) return;

        usuario = new Usuario();
        usuario.setId(Base64Custom.codificarBase64(email));
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(
                CadastroActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UsuarioDAO usuarioDAO = new UsuarioDAO();
                            if (usuarioDAO.salvarUsuario(usuario)) {
                                Toast.makeText(CadastroActivity.this, "Cadastro Realizado com Sucesso", Toast.LENGTH_SHORT).show();
                                abrirLoginActivity();
                            }
                        } else {
                            try{
                                Log.i("EXCECAO:", task.getException().toString());
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                inputLayoutSenha.setErrorEnabled(true);
                                inputLayoutSenha.setError("Digite uma senha mais forte, contendo mais caracteres e com letras e números!");
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                inputLayoutEmail.setErrorEnabled(true);
                                inputLayoutEmail.setError("O e-mail digitado é inválido, digite um novo e-mail!");
                            } catch (FirebaseAuthUserCollisionException e){
                                inputLayoutEmail.setErrorEnabled(true);
                                inputLayoutEmail.setError("Esse e-mail já está em uso!");
                            } catch (Exception e){
                                Toast.makeText(CadastroActivity.this, "Erro ao efetuar o cadastro!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
    }

    private void abrirLoginActivity(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
