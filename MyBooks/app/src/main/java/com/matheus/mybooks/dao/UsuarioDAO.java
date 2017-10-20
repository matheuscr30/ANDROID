package com.matheus.mybooks.dao;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.matheus.mybooks.config.ConfiguracaoFirebase;
import com.matheus.mybooks.model.Usuario;

/**
 * Created by matheus on 09/10/17.
 */

public class UsuarioDAO {

    private DatabaseReference databaseReference;

    public UsuarioDAO() {
        this.databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    }

    public boolean salvarUsuario(Usuario usuario){
        try{
            Log.i("SHIT", "OQ");
            databaseReference = databaseReference.child("usuarios")
                    .child(usuario.getId());
            databaseReference.setValue(usuario);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }
}