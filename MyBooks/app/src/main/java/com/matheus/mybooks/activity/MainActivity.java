package com.matheus.mybooks.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.games.GamesMetadata;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.matheus.mybooks.R;
import com.matheus.mybooks.bookUtils.BookServices;
import com.matheus.mybooks.bookUtils.BooksAdapter;
import com.matheus.mybooks.bookUtils.RepoBook;
import com.matheus.mybooks.config.Base64Custom;
import com.matheus.mybooks.config.ConfiguracaoFirebase;
import com.matheus.mybooks.dao.BookDAO;
import com.matheus.mybooks.model.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabMain;
    private RecyclerView recyclerView;
    private ArrayList<Book> books;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerBooks;
    private BooksAdapter booksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabMain = (FloatingActionButton)findViewById(R.id.fabMain);
        recyclerView = (RecyclerView)findViewById(R.id.rvMain);

        books = new ArrayList<>();
        booksAdapter = new BooksAdapter(books, MainActivity.this);
        recyclerView.setAdapter(booksAdapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        inicializarDados();

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreBarCode();
            }
        });
    }

    private void inicializarDados(){
        String id_usuario = Base64Custom
                .codificarBase64(ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getEmail());

        databaseReference = ConfiguracaoFirebase.getDatabaseReference()
                .child("books").child(id_usuario);

        valueEventListenerBooks = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books.clear();
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Book book = dados.getValue(Book.class);
                    books.add(book);
                }

                booksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addValueEventListener(valueEventListenerBooks);
    }

    private void abreBarCode(){
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Escanear Codigo de Barras do Livro");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent in ) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, in);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            Log.i("ISBN", "scanContent " + scanContent);
            String scanFormat = scanningResult.getFormatName();
            Log.i("ISBN", "scanFormat " + scanFormat);
            if(scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13")) {
                parseBook(scanContent);
            }
        } else {
            Snackbar.make(findViewById(R.id.linearLayoutMain), "Não foi possivel encontrar o livro", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void parseBook(String isbn){
        Log.i("ISBN", isbn);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookServices bookServices = retrofit.create(BookServices.class);
        Call<RepoBook> bookcall = bookServices.listBook(isbn);
        bookcall.enqueue(new Callback<RepoBook>() {
            @Override
            public void onResponse(Call<RepoBook> call, Response<RepoBook> response) {
                try {
                    RepoBook repoBook = response.body();
                    if(repoBook.getTotalItems() == 0){
                        Toast.makeText(MainActivity.this, "Livro não encontrado", Toast.LENGTH_LONG).show();
                        return;
                    }
                    RepoBook.Item item = repoBook.getItems().get(0);
                    RepoBook.Item.VolumeInfo volume = item.getVolumeInfo();


                    Book book = new Book();
                    book.setId(item.getId());
                    book.setTitulo(volume.getTitle());
                    book.setSubTitulo(volume.getSubtitle());
                    book.setAutores(volume.getAuthors());
                    book.setDataPublicacao(volume.getPublishedDate());
                    book.setDescricao(volume.getDescription());
                    book.setQuantidadePaginas(volume.getPageCount());

                    String id_usuario = Base64Custom
                            .codificarBase64(ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getEmail());
                    BookDAO bookDAO = new BookDAO();
                    if(bookDAO.salvarBook(book, id_usuario)){
                        Toast.makeText(MainActivity.this, "Livro cadastrado com sucesso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Erro ao cadastrar livro", Toast.LENGTH_LONG).show();
                    }

                    Toast.makeText(MainActivity.this, repoBook.toString(), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    e.printStackTrace();
                    Log.i("HERE", "ONE");
                }
            }

            @Override
            public void onFailure(Call<RepoBook> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Erro ao chamar API", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
        ConfiguracaoFirebase.getFirebaseAuth().signOut();
        abrirLoginActivity();
    }

    private void abrirLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}