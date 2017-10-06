package com.matheus.instagram.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.matheus.instagram.R;
import com.matheus.instagram.adapter.FeedUsuariosAdapter;
import com.matheus.instagram.adapter.UsuariosAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedUsuariosActivity extends AppCompatActivity {

    private ParseQuery parseQuery;
    private ArrayList<ParseObject> postagens;
    private FeedUsuariosAdapter adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_usuarios);

        toolbar = (Toolbar) findViewById(R.id.toolbarFeedUsuarios);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_feed_usuarios);

        Bundle bundle =  getIntent().getExtras();
        username = bundle.get("username").toString();
        toolbar.setTitle(username);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postagens = new ArrayList<>();

        adapter = new FeedUsuariosAdapter(postagens, FeedUsuariosActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(FeedUsuariosActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(FeedUsuariosActivity.this, DividerItemDecoration.VERTICAL));

        getPostagens();
    }

    private void getPostagens(){

        parseQuery = ParseQuery.getQuery("Imagem");
        parseQuery.whereEqualTo("username", username);
        parseQuery.orderByDescending("createdAt");

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    if(objects.size() > 0){
                        postagens.clear();
                        for(ParseObject parseObject : objects)
                            postagens.add(parseObject);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Snackbar.make(toolbar, "Usuario ainda nao fez nenhuma postagem", Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });

    }
}