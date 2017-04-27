package com.example.beerapp;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton imageButtonNavigationDrawer;
    private DrawerLayout drawerLayout;
    private SeekBar barraAlcanceRaio;
    private Float atualZoom;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        imageButtonNavigationDrawer = (ImageButton) findViewById(R.id.imageButtonNavigationDrawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        barraAlcanceRaio = (SeekBar) findViewById(R.id.barraAlcanceRaio);

        MakeBarsInvisible();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imageButtonNavigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        barraAlcanceRaio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atualizaZoom(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MapsActivity.this, , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng casa = new LatLng(-18.932088, -48.224179);
        mMap.addMarker(new MarkerOptions().position(casa).title("My House"));
        atualZoom = Float.parseFloat("19.00");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(casa, atualZoom));
        mMap.getUiSettings().setCompassEnabled(false);

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
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
    public void onConfigurationChanged(Configuration newConfig) {

        atualizaZoom(0);
        super.onConfigurationChanged(newConfig);
    }
}
