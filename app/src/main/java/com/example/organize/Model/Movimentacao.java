package com.example.organize.Model;

import com.example.organize.config.ConfigFairebase;
import com.example.organize.helper.DataUtil;
import com.example.organize.helper.Java64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {
    private String data;
    private String categoria;
    private String descricao;
    private  String tipo;
    private double valor;
    private String key;

    public Movimentacao(){

    }

    public void salvarMovimentacao(String data){
        FirebaseAuth auth = ConfigFairebase.getAutenticacao();
        String idUsuario = Java64Custom.nomeCodificarBase64(auth.getCurrentUser().getEmail());
        String mesAno = DataUtil.mesAnoDataEscolhida(data);
        DatabaseReference firabase = ConfigFairebase.getFiraBaseDataBase();
        firabase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
