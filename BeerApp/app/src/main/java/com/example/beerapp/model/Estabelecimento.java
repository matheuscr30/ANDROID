package com.example.beerapp.model;

import com.example.beerapp.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by matheus on 08/09/17.
 */

public class Estabelecimento {

    private String nome;
    private String id;
    private HashMap<String, Double> cervejas;
    private Date horarioAberto;
    private Date horarioFechado;

    public Estabelecimento() {
    }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("estabelecimentos").child( getId() ).setValue(this);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Double> getCervejas() {
        return cervejas;
    }

    public void setCervejas(HashMap<String, Double> cervejas) {
        this.cervejas = cervejas;
    }

    public Date getHorarioAberto() {
        return horarioAberto;
    }

    public void setHorarioAberto(Date horarioAberto) {
        this.horarioAberto = horarioAberto;
    }

    public Date getHorarioFechado() {
        return horarioFechado;
    }

    public void setHorarioFechado(Date horarioFechado) {
        this.horarioFechado = horarioFechado;
    }
}
