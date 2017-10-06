/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;
import com.parse.starter.R;


public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      /*
      //Cadastro de Usuarios
      ParseUser usuario = new ParseUser();
      usuario.setUsername("matheuscr");
      usuario.setPassword("12345678");
      usuario.setEmail("matheuscunhareis30@gmail.com");

      //Cadastrar
      usuario.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
              if(e == null){
                  Log.i("LOGIN", "Usuario Logado com Sucesso");
              } else {
                  Log.i("LOGIN", "Erro no login do Usuario");
              }
          }
      });
      */


      //Deslogar Usuario
      ParseUser.logOut();

      /*
      //Verificar Usuario Logado
      if(ParseUser.getCurrentUser() != null){
          Log.i("LOGIN", "Usuario esta logado");
      } else {
          Log.i("LOGIN", "Usuario nao esta logado");
      }
      */

      //Fazer Login do Usuario
      /*ParseUser.logInInBackground("matheuscr", "12345678", new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
              if(e == null){
                  Log.i("LOGIN", "Usuario esta logado");
              } else {
                  Log.i("LOGIN", "Usuario nao esta logado");
              }
          }
      });
      */
  }
}
