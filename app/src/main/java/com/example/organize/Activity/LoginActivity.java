package com.example.organize.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.organize.Model.Usuario;
import com.example.organize.R;
import com.example.organize.config.ConfigFairebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextSenha;
    private TextInputLayout inputLayoutEmail, inputLayoutSenha;
    private Button buttonEntrar;
    private Usuario usuario =  new Usuario();
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextNameLogin);
        editTextSenha = findViewById(R.id.editTextSenhaLogin);
        buttonEntrar = findViewById(R.id.buttonEntrarLogin);
        inputLayoutEmail = findViewById(R.id.textInputLayoutNameLogin);
        inputLayoutSenha = findViewById(R.id.textInputLayoutSenhaLogin);


        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (estaValido(Objects.requireNonNull(editTextEmail.getText()))) {
                    inputLayoutEmail.setError(null);
                    if (estaValido(Objects.requireNonNull(editTextSenha.getText()))) {
                        inputLayoutSenha.setError(null);
                        usuario.setNome(editTextEmail.getText().toString());
                        usuario.setEmail(editTextEmail.getText().toString());
                        usuario.setSenha(editTextSenha.getText().toString());
                        validarLogin();
                    } else {
                        error("Senha vazia", inputLayoutSenha);
                    }
                } else {
                    error("Email vazio", inputLayoutEmail);
                }
            }
        });

    }

    public void error(String e, TextInputLayout textInput) {
        textInput.setError(e);
        textInput.getError();
    }

    public boolean estaValido(Editable text) {
        return text.length() > 0;
    }

    public void validarLogin(){

        autenticacao = ConfigFairebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(
           usuario.getEmail(),
           usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                         excecao = "Usuario não esta cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                          excecao = "E-mail e senha incorretos";
                    }
                    catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário:" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
                }

        });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

}