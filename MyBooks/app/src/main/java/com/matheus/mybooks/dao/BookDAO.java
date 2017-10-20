package com.matheus.mybooks.dao;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.matheus.mybooks.config.ConfiguracaoFirebase;
import com.matheus.mybooks.model.Book;

/**
 * Created by matheus on 19/10/17.
 */

public class BookDAO {
    private DatabaseReference databaseReference;

    public BookDAO() {
        this.databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    }

    public boolean salvarBook(Book book, String id_usuario){
        try{
            databaseReference = databaseReference
                    .child("books")
                    .child(id_usuario)
                    .child(book.getId());
            databaseReference.setValue(book);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }
}
