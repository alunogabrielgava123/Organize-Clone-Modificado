package com.example.organize.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFairebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firabse;

    public static DatabaseReference getFiraBaseDataBase(){
        if(firabse == null){
             firabse = FirebaseDatabase.getInstance().getReference();
        }
        return firabse;
    }

    public static FirebaseAuth getAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
