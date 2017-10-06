package com.matheus.instagram.util;

import java.util.HashMap;

/**
 * Created by matheus on 07/09/17.
 */

public class ParseErrors {

    private HashMap<Integer, String> errors;

    public ParseErrors() {
        this.errors = new HashMap<>();
        this.errors.put(201, "A senha não foi preenchida!");
        this.errors.put(202, "Usuário já existe! Escolha outro nome de usuário!!");
    }

    public String getError(int codeError){
        return this.errors.get(codeError);
    }
}
