package com.cursoandroid.firebaseapp.firebaseapp;

/**
 * Created by matheus on 21/04/17.
 */

public class Produto {

    private String descricao;
    private String cor;
    private String fabricante;

    public Produto(){
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
}
