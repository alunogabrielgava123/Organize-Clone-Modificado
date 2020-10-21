package com.example.organize.Model;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.organize.config.ConfigFairebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {
    private String idUsuario;
    private String nome;
    private String Email;
    private String senha;
    private Double receitaTotal = 0.00;
    private Double despesaTotal = 0.00;
    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void salvarUsuario(){
        DatabaseReference referencia = ConfigFairebase.getFiraBaseDataBase();
        referencia.child("usuarios").
                child(this.idUsuario).
                setValue(this);
    }

    @NonNull
    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    @NonNull
    public Double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(Double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    public  Usuario(){

    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return Email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
