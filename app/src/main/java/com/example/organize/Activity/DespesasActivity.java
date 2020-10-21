package com.example.organize.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organize.Model.Movimentacao;
import com.example.organize.Model.Usuario;
import com.example.organize.R;
import com.example.organize.config.ConfigFairebase;
import com.example.organize.helper.DataUtil;
import com.example.organize.helper.Java64Custom;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao = new Movimentacao();
    private DatabaseReference firebaseRef = ConfigFairebase.getFiraBaseDataBase();
    private FirebaseAuth autenticacao = ConfigFairebase.getAutenticacao();
    private Double despesaTotal;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.editTextDataDespesa);
        campoCategoria = findViewById(R.id.editTextSalarioDespesa);
        campoDescricao = findViewById(R.id.editTexSalariomensalDespesa);
        campoValor = findViewById(R.id.editTextValorReceita);

        //data atual
        campoData.setText(
                DataUtil.dataAtual()
        );
        recuperarDespesaTotal();

    }


    public  void salvarDespesa(View view){
        if(validarMovimentacao()){
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");

            Double despesaGerada = despesaTotal + valorRecuperado;
            atualizarDespesa(despesaGerada);
            movimentacao.salvarMovimentacao(data);

            //finalizando a atividade
            finish();
        }
    }

    public  boolean validarMovimentacao(){
        String cmValor = campoValor.getText().toString();
        String cmData = campoData.getText().toString();
        String cmCategoria = campoCategoria.getText().toString();
        String cmDescricao = campoDescricao.getText().toString();

        if(!cmValor.isEmpty()){
            if(!cmData.isEmpty()){
                if(!cmCategoria.isEmpty()){
                    if(!cmDescricao.isEmpty()){
                        return true;
                    }else {
                        Toast.makeText(this,"Campo Descrição invalido", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else{
                    Toast.makeText(this,"Campo Categoria invalido", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this,"Campo Data invalido", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this,"Campo valor invalido", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public  void recuperarDespesaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Java64Custom.nomeCodificarBase64(emailUsuario);
        DatabaseReference usuarioRef =  firebaseRef.child("usuarios")
                .child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario =  dataSnapshot.getValue(Usuario.class);
                despesaTotal =  usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public  void atualizarDespesa(Double despesa){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Java64Custom.nomeCodificarBase64(emailUsuario);
        DatabaseReference usuarioRef =  firebaseRef.child("usuarios")
                .child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }

}