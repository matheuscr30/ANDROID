package com.example.beerapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.example.beerapp.R;
import com.example.beerapp.config.ConfiguracaoFirebase;
import com.example.beerapp.helper.Base64Custom;
import com.example.beerapp.helper.Preferencias;
import com.example.beerapp.model.Usuario;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button botaoCadastrarLogin;
    private Button botaojaUsoApp;
    private LoginButton botaoFacebook;
    private ProgressDialog progressDialog;
    private EditText textEmail;
    private EditText textSenha;
    private Usuario usuario;
    private static CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configuraProgressDialog();

        botaoCadastrarLogin = (Button)findViewById(R.id.botaoCadastrarLogin);
        botaojaUsoApp = (Button)findViewById(R.id.botao_ja_uso);
        botaoFacebook = (LoginButton)findViewById(R.id.botao_facebook);
        textEmail = (EditText)findViewById(R.id.editTextLoginEmail);
        textSenha = (EditText)findViewById(R.id.editTextLoginSenha);
        verificarUsuarioLogado();


        botaoCadastrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroUsuario();
            }
        });

        botaojaUsoApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    usuario = new Usuario();
                    usuario.setEmail(textEmail.getText().toString());
                    usuario.setSenha(textSenha.getText().toString());
                    validarLogin();
                } catch(IllegalArgumentException e){
                    Snackbar.make(v, "Preencha todos os campos", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //Initialize Facebook Login Button
        mCallbackManager = CallbackManager.Factory.create();
        botaoFacebook.setReadPermissions("email", "public_profile", "user_friends");
        botaoFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
                verificarUsuarioLogado();
                getDataFacebook();
                //progressDialog.dismiss();
            }

            @Override
            public void onCancel() {
                Log.i("FacebookLogin", "Login cancelado");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("FacebookLogin", "Erro no Login: " + error.toString());
            }
        });
    }

    private void getDataFacebook(){

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        try {
                            String nomeUsuario = object.has("name") ? object.getString("name") : "";
                            String descricaoUsuario = object.has("about") ? object.getString("about") : "";
                            String generoUsuario = object.has("gender") ?
                                    (object.getString("gender").equals("male") ? "Masculino" : "Feminino") : "";
                            String emailUsuario = object.has("email") ? object.getString("email") : "";
                            String facebookId = object.getString("id");
                            String identificadorUsuario = Base64Custom.codificarBase64(emailUsuario);

                            System.out.println(facebookId);

                            usuario =  new Usuario();
                            usuario.setId(identificadorUsuario);
                            usuario.setFb_id(facebookId);
                            usuario.setIdade(0);
                            usuario.setNome(nomeUsuario);
                            usuario.setDescricao(descricaoUsuario);
                            usuario.setEmail(emailUsuario);
                            usuario.setGenero(generoUsuario);
                            usuario.salvar();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, about,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void handleFacebookAccessToken(AccessToken token) {
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())  {
                            Log.i("FacebookLogin", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                            abrirTelaPrincipal();
                        }
                    }
                });
    }

    private void validarLogin(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    String identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());
                    preferencias.salvarDados(identificadorUsuarioLogado);

                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                    abrirTelaPrincipal();

                } else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirCadastroUsuario(){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        intent.putExtra("emailCadastrar", textEmail.getText().toString());
        startActivity(intent);
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void verificarUsuarioLogado(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    //Necessary to LoginButton works
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void configuraProgressDialog(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Beer App");
        progressDialog.setMessage("Logando...");
        progressDialog.setCancelable(false);
    }
}

// Get the Hash Key Facebook
/*
try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.beerapp",         PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                Toast.makeText(getApplicationContext(),sign,         Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
 */