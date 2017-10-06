package com.example.beerapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.beerapp.R;
import com.example.beerapp.config.ConfiguracaoFirebase;
import com.example.beerapp.helper.Base64Custom;
import com.example.beerapp.helper.Preferencias;
import com.example.beerapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private RadioGroup radioGroup;
    private EditText editTextCadastroNome;
    private EditText editTextCadastroDescricao;
    private EditText editTextCadastroIdade;
    private EditText editTextCadastroEmail;
    private EditText editTextCadastroSenha;
    private Button botaoCadastrar;
    private Usuario usuario;
    private View globalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        editTextCadastroNome = (EditText)findViewById(R.id.editTextCadastroNome);
        editTextCadastroDescricao = (EditText)findViewById(R.id.editTextCadastroDescricao);
        editTextCadastroIdade = (EditText)findViewById(R.id.editTextCadastroIdade);
        editTextCadastroEmail = (EditText)findViewById(R.id.editTextCadastroEmail);
        editTextCadastroSenha = (EditText)findViewById(R.id.editTextCadastroSenha);
        botaoCadastrar = (Button)findViewById(R.id.botaoCadastrar);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        Bundle bundle = getIntent().getExtras();
        if(bundle.get("emailCadastrar") != null){
            editTextCadastroEmail.setText(bundle.get("emailCadastrar").toString());
        }

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    usuario = new Usuario();
                    usuario.setNome(editTextCadastroNome.getText().toString());
                    usuario.setIdade(Integer.parseInt(editTextCadastroIdade.getText().toString()));
                    usuario.setEmail(editTextCadastroEmail.getText().toString());
                    usuario.setSenha(editTextCadastroSenha.getText().toString());
                    usuario.setDescricao(" ");

                    if(editTextCadastroDescricao.getText().toString() != ""){
                        usuario.setDescricao(editTextCadastroDescricao.getText().toString());
                    }

                    String generoUsuario = "";
                    //Toast.makeText(CadastroUsuarioActivity.this, String.valueOf(radioGroup.isActivated()), Toast.LENGTH_LONG).show();
                    if(radioGroup.getCheckedRadioButtonId() <= 0) throw new IllegalArgumentException();
                    else{
                        switch (radioGroup.getCheckedRadioButtonId()){
                            case R.id.radioButtonMasculino:
                                generoUsuario = "Masculino";
                                break;
                            case R.id.radioButtonFeminino:
                                generoUsuario = "Feminino";
                                break;
                        }
                    }
                    usuario.setGenero(generoUsuario);

                    globalView = v;
                    cadastrarUsuario();
                } catch (IllegalArgumentException e){
                    Snackbar.make(v, "Preencha todos os campos", Snackbar.LENGTH_LONG).show();
                }  catch (Exception e) {
                    Snackbar.make(v, "Preencha todos os campos corretamente", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarUsuario(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUsuarioActivity.this, "Usuário Cadastrado com Sucesso", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(identificadorUsuario); //Pega id do usuario no firebase
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDados(identificadorUsuario);

                    abrirLoginUsuario();
                } else {

                    String erroExcecao = "";

                    try{
                        Log.i("EXCECAO:", task.getException().toString());
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo mais caracteres e com letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail!";
                    } catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Esse e-mail já está em uso!";
                    } catch (Exception e){
                        erroExcecao = "Erro ao efetuar o cadastro!";
                    }

                    Snackbar.make(globalView, erroExcecao, Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
