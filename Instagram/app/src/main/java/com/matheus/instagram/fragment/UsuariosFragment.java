package com.matheus.instagram.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.matheus.instagram.R;
import com.matheus.instagram.activity.FeedUsuariosActivity;
import com.matheus.instagram.adapter.UsuariosAdapter;
import com.matheus.instagram.util.OnItemClicked;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuariosFragment extends Fragment implements OnItemClicked {

    private RecyclerView recyclerView;
    private ArrayList<ParseUser> usuarios;
    private RecyclerView.Adapter adapter;
    private ParseQuery parseQuery;

    public UsuariosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        usuarios = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new UsuariosAdapter(usuarios, getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        ((UsuariosAdapter)adapter).setOnClick(this);
        getUsuarios();
        return view;
    }

    public void getUsuarios() {

        parseQuery = ParseUser.getQuery();
        //parseQuery.orderByDescending("username");

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    String usernameLogado = ParseUser.getCurrentUser().getUsername();
                    //Toast.makeText(getActivity(), String.valueOf(objects.size()), Toast.LENGTH_LONG).show();

                    if (objects.size() > 0) {
                        usuarios.clear();
                        for (ParseObject parseUser : objects) {
                            if(!((ParseUser)parseUser).getUsername().equals(usernameLogado))
                                usuarios.add((ParseUser) parseUser);
                        }

                        adapter.notifyDataSetChanged();
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        ParseUser parseUser = usuarios.get(position);

        Intent intent = new Intent(getActivity(), FeedUsuariosActivity.class);
        intent.putExtra("username", parseUser.getUsername());
        startActivity(intent);
    }
}