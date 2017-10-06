package com.example.beerapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.beerapp.R;
import com.example.beerapp.config.ConfiguracaoFirebase;
import com.example.beerapp.helper.Base64Custom;
import com.example.beerapp.helper.DownloadImageTask;
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
import com.google.firebase.database.ValueEventListener;

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
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        barraAlcanceRaio = (SeekBar)findViewById(R.id.barraAlcanceRaio);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configuraUsuarioNavigationDrawer();

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
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64(emailUsuario);

        barraAlcanceRaio.setProgress(4);

        databaseReference = ConfiguracaoFirebase.getFirebase()
                .child("usuarios")
                .child(identificadorUsuario);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usuario = dataSnapshot.getValue(Usuario.class);

                View header = navigationView.getHeaderView(0);

                TextView nomeUsuario = (TextView)header.findViewById(R.id.tNome);
                TextView emailUsuario = (TextView)header.findViewById(R.id.tEmail);
                ImageView imagemPerfil = (ImageView)header.findViewById(R.id.img);

                String urlFace = "https://graph.facebook.com/" + usuario.getFb_id() + "/picture?type=large";

                new DownloadImageTask(imagemPerfil).execute(urlFace);

                nomeUsuario.setText(usuario.getNome());
                emailUsuario.setText(usuario.getEmail());
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
        switch (item.getItemId()){
            case R.id.nav_item_add_estabelecimento:
                abrirCadastroEstabelecimento();
                break;
        }
        return true;
    }

    public void abrirCadastroEstabelecimento(){
        Intent intent = new Intent(MainActivity.this, CadastroEstabelecimentoActivity.class);
        startActivity(intent);
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

        atualZoom = Float.parseFloat("18.00");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(casa, atualZoom));
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        atualizaZoom(0);
        super.onConfigurationChanged(newConfig);
    }

    public void fabActionLocalizacao(View v){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        try{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null) {
                LatLng act = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(act, atualZoom));
            } else {
                Snackbar.make(v, "Serviço de Localização Desligado", Snackbar.LENGTH_SHORT).show();
            }
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
