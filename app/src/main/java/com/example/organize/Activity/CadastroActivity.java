package com.example.organize.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.organize.Model.Usuario;
import com.example.organize.R;
import com.example.organize.config.ConfigFairebase;
import com.example.organize.helper.Java64Custom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private Button buttonCadastrar;
    private TextInputLayout textInputLayoutNome, textInputLayoutEmail, textInputLayoutSenha;
    private TextInputEditText editTextNome, editTextSenha, editTextEmail;
    private FirebaseAuth autenticacao;
    private Usuario usuario = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        textInputLayoutNome = findViewById(R.id.textInputLayoutNome);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutSenha = findViewById(R.id.textInputLayoutSenha);

        editTextNome = findViewById(R.id.ediTextNome);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextEmail = findViewById(R.id.ediTextEmail);

        buttonCadastrar = findViewById(R.id.button_cadastrar);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (estaValido(Objects.requireNonNull(editTextNome.getText()))) {
                    textInputLayoutNome.setError(null);
                    if (estaValido(Objects.requireNonNull(editTextEmail.getText()))) {
                        textInputLayoutEmail.setError(null);
                        if (estaValido(Objects.requireNonNull(editTextSenha.getText()))) {
                            textInputLayoutSenha.setError(null);
                            usuario.setNome(editTextNome.getText().toString());
                            usuario.setEmail(editTextEmail.getText().toString());
                            usuario.setSenha(editTextSenha.getText().toString());
                            cadastratUsuario();
                        } else {
                            error("Senha vazia", textInputLayoutSenha);
                        }
                    } else {
                        error("Email vazio", textInputLayoutEmail);
                    }
                } else {
                    error("Nome vazio", textInputLayoutNome);
                }
            }
        });

    }

    public boolean estaValido(Editable text) {
        return text.length() > 0;
    }

    public void error(String e, TextInputLayout textInput) {
        textInput.setError(e);
        textInput.getError();
    }

    public void cadastratUsuario() {
        autenticacao = ConfigFairebase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //salvar dados
                    String idUsuario = Java64Custom.nomeCodificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvarUsuario();
                    finish();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor, digite um e-mail válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Esta conta já foi cadastrada";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário:" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}