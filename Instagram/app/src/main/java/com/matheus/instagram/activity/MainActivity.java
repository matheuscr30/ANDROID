package com.matheus.instagram.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.matheus.instagram.R;
import com.matheus.instagram.adapter.TabsAdapter;
import com.matheus.instagram.fragment.HomeFragment;
import com.matheus.instagram.util.SlidingTabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configura Toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbarPrincipal);
        toolbar.setLogo(R.drawable.instagramlogo);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Configura Abas
        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tab_main);
        viewPager = (ViewPager)findViewById(R.id.view_pager_main);

        //Configurar Adapter
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(tabsAdapter);
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.text_item_tab);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.cinzaEscuro));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_sair:
                deslogarUsuario();
                break;
            case R.id.action_settings:
                break;
            case R.id.action_compartilhar:
                compartilharFotos();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void compartilharFotos(){

        //Abrir Galeria de Imagens
        Intent intent = new Intent( Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            //Recuperar Local da Imagem
            Uri localImagemSelecionada = data.getData();

            //Recupera a imagem
            try {
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                //Comprimir no formato PNG
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagem.compress( Bitmap.CompressFormat.PNG, 75, stream);

                //Cria um Array de Bytes da Imagem
                byte[] byteArray = stream.toByteArray();

                //Criar um arquivo do formato proprio do Parse
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddmmaaaahhmmss");
                String nomeImagem = dateFormat.format( new Date() );
                ParseFile parseFile = new ParseFile(nomeImagem + ".png", byteArray);
                //Monta objeto para salvar no Parse
                ParseObject parseObject = new ParseObject("Imagem");
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                parseObject.put("imagem", parseFile);

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(MainActivity.this, "Sua foto foi postada!!", Toast.LENGTH_LONG).show();

                            //Atualizar Fragment
                            TabsAdapter adapterNovo = (TabsAdapter) viewPager.getAdapter();
                            HomeFragment homeFragmentNovo = (HomeFragment) adapterNovo.getFragment(0);
                            homeFragmentNovo.atualizarPostagens();

                        } else {
                            Toast.makeText(MainActivity.this, "Ocorreu um erro com sua postagem!!", Toast.LENGTH_LONG).show();
                            System.out.println(e.toString());
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void deslogarUsuario() {
        ParseUser.logOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        try {
            wait(3);
        } catch (Exception e){
            System.out.println(e.toString());
        }
        finish();
    }
}
