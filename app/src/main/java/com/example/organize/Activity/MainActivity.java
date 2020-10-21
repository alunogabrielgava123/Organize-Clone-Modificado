package com.example.organize.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.organize.R;
import com.example.organize.config.ConfigFairebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setFullscreen(true);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setButtonBackVisible(false);
        setButtonNextVisible(false);


        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );
        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );
        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );
        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        );
        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_slider)
                .canGoForward(false)
                .build()
        );


    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuario();
    }

    public void cadastrar(View view){
        startActivity(new Intent(MainActivity.this, CadastroActivity.class));
    }

    public void login(View view){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
}

    public void verificarUsuario(){
        autenticado = ConfigFairebase.getAutenticacao();
        //autenticado.signOut();
        if(autenticado.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }
    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }

}