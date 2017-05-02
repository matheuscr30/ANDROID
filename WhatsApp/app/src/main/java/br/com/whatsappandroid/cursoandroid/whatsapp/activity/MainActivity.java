package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.TabAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager)findViewById(R.id.vp_pagina);
        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.stl_tabs);

        toolbar = (Toolbar)findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        //Configurar SlidingTabLayouts
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configurar Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_adicionar:
                return true;
            case R.id.item_configuracoes:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void deslogarUsuario(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
