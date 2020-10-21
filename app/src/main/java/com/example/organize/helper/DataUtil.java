package com.example.organize.helper;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class DataUtil {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  static String dataAtual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public  static  String mesAnoDataEscolhida(String data){
        String retornodata[] = data.split("/");
        String dia = retornodata[0];
        String mes = retornodata[1];
        String ano = retornodata[2];
        return  mes + ano;
    }

}
