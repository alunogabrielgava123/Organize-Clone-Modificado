package com.example.organize.config;

import com.example.organize.helper.Java64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ConfigUsuarioHelper {
    private static  DatabaseReference firebaseRef = ConfigFairebase.getFiraBaseDataBase();
    private static  FirebaseAuth autenticado = ConfigFairebase.getAutenticacao();

    public ConfigUsuarioHelper() {

    }

    public static DatabaseReference usuarioFireBase(DatabaseReference usuarioRef, String dataBaseName){

        String emailUsuario = autenticado.getCurrentUser().getEmail();
        String idUsuario = Java64Custom.nomeCodificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child(dataBaseName)
                .child(idUsuario);

        return usuarioRef;
    }


}
