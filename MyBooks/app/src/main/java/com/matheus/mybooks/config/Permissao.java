package com.matheus.mybooks.config;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheus on 23/04/17.
 */

public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes) {

        List<String> listaPermissoes = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= 23) {
            for (String permissao : permissoes) {

                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!validaPermissao) listaPermissoes.add(permissao);
            }

            if(listaPermissoes.isEmpty()) return true;

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //Solicita Permissao
            ActivityCompat.requestPermissions(activity,novasPermissoes, requestCode);
        }

        return true;
    }
}
