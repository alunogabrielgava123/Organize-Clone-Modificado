package com.example.organize.helper;

import android.util.Base64;

public class Java64Custom {

    public static  String nomeCodificarBase64(String texto){
        //codificar para uma string aplicando verificação
       return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }
    public static  String descotificarBase64(String textoCodificado){
        //decotificando em base 64
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }

}
