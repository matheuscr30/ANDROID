package com.example.beerapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beerapp.R;
import com.example.beerapp.config.ConfiguracaoFirebase;
import com.example.beerapp.helper.Base64Custom;
import com.example.beerapp.model.Usuario;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.ls.LSOutput;

import java.nio.channels.SelectionKey;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private Float atualZoom;
    private Toolbar toolbar;
    private Usuario usuario;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private SeekBar barraAlcanceRaio;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        barraAlcanceRaio = (SeekBar)findViewById(R.id.barraAlcanceRaio);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        barraAlcanceRaio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atualizaZoom(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MainActivity.this, "apertou", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void configuraUsuarioNavigationDrawer(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        databaseReference = ConfiguracaoFirebase.getFirebase().child("usuarios");
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64(emailUsuario);

        databaseReference = databaseReference.child(identificadorUsuario);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //dataSnapshot.getValue()
                for(DataSnapshot dados : dataSnapshot.getChildren() ){
                    Log.v("LALALA", "USUARIO " + usuario.getNome());
                    usuario = dados.getValue(Usuario.class);
                }


                //Toast.makeText(MainActivity.this, usuario.toString(), Toast.LENGTH_LONG).show();
                /*
                View header = navigationView.getHeaderView(0);

                TextView nomeUsuario = (TextView)header.findViewById(R.id.tNome);
                TextView emailUsuario = (TextView)header.findViewById(R.id.tEmail);
                Log.i("DEU merda", usuario.toString());

                nomeUsuario.setText(usuario.getNome());
                emailUsuario.setText(usuario.getEmail()); */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcoes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                break;
        }
        return true;
    }

    private void deslogarUsuario(){
        ConfiguracaoFirebase.getFirebaseAuth().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void MakeBarsInvisible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public void atualizaZoom(int progress) {
        try{
            LatLng casa = new LatLng(-18.932088, -48.224179);
            Float novoProgresso = atualZoom - Float.parseFloat(String.valueOf(progress));
            Float zoomEscolhido = Float.parseFloat(String.valueOf(novoProgresso));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casa, zoomEscolhido));
        } catch (Exception e){
            Log.i("PROGRESSO", String.valueOf(progress));
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng casa = new LatLng(-18.932088, -48.224179);
        mMap.addMarker(new MarkerOptions().position(casa).title("My House"));
        atualZoom = Float.parseFloat("19.00");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(casa, atualZoom));
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        atualizaZoom(0);
        super.onConfigurationChanged(newConfig);
    }
}
