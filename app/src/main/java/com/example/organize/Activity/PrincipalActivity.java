package com.example.organize.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.organize.Model.Movimentacao;
import com.example.organize.Model.Usuario;
import com.example.organize.R;
import com.example.organize.config.ConfigFairebase;
import com.example.organize.config.ConfigUsuarioHelper;
import com.example.organize.helper.Java64Custom;
import com.example.organize.view.MvAdpter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textNome,textSaldo,textSaldoTotal;
    private FirebaseAuth autenticado = ConfigFairebase.getAutenticacao();
    private DatabaseReference firebaseRef = ConfigFairebase.getFiraBaseDataBase();
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoTotal = 0.0;
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListener;
    private RecyclerView recyclerView;
    private MvAdpter adapterMovimentacao;
    private List<Movimentacao> movimentacaos = new ArrayList<>();
    private DatabaseReference movimentacaoRef;
    private String mesano;
    private ValueEventListener valueEventListenerMovimentacao;
    private Movimentacao movimentacao;
    private DatabaseReference usuarioDataBase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        textNome = findViewById(R.id.nomeDoUsuario);
        textSaldo = findViewById(R.id.textSaldo);
        textSaldoTotal = findViewById(R.id.textSaldoGeral);

        calendarView = findViewById(R.id.calendarView);
        configuraCalendarView();
        createRecyclerView();
        swipe();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuSair){
            autenticado.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesa(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }
    public void adicionarReceita(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void recuperarMovimentacao(){
        String emailUsuario = autenticado.getCurrentUser().getEmail();
        String idUsuario = Java64Custom.nomeCodificarBase64(emailUsuario);
        movimentacaoRef =  firebaseRef.child("movimentacao")
                .child(idUsuario)
                .child(mesano);

        valueEventListenerMovimentacao = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movimentacaos.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Movimentacao movimentacao =  data.getValue(Movimentacao.class);
                    movimentacao.setKey(data.getKey());
                    movimentacaos.add(movimentacao);

                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void configuraCalendarView(){
        CharSequence meses[] = {"Janeiro","Fevereiro", "Março", "Abril", "Maio","Junho", "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendarView.setTitleMonths(meses);
        final CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d",(dataAtual.getMonth() + 1));
        mesano = mesSelecionado + "" + dataAtual.getYear();
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d",(date.getMonth() + 1));
                mesano = mesSelecionado + "" + date.getYear();


                movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
                recuperarMovimentacao();
            }
        });
    }

    public void recuperarResumo(){
        usuarioDataBase = ConfigUsuarioHelper.usuarioFireBase(usuarioRef, "usuarios");
        valueEventListener = usuarioDataBase.
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario =  dataSnapshot.getValue(Usuario.class);
                assert usuario != null;
                despesaTotal =  usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoTotal =  receitaTotal - despesaTotal;
                DecimalFormat decimalFormat =  new DecimalFormat("0.##");

                textSaldo.setText("R$: " +
                        decimalFormat.
                                format(resumoTotal));

                textNome.setText("Olá, " + usuario.getNome());
                textSaldoTotal.setText("Seu saldo total!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void swipe(){
        ItemTouchHelper.Callback intemTouch =  new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags =  ItemTouchHelper.ACTION_STATE_IDLE;
                int swiprFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags,swiprFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(intemTouch).attachToRecyclerView(recyclerView);
    }

    public void atualizarSaldo(){
        String emailUsuario = autenticado.getCurrentUser().getEmail();
        String idUsuario = Java64Custom.nomeCodificarBase64(emailUsuario);
        usuarioRef =  firebaseRef.child("usuarios")
                .child(idUsuario);
        if(movimentacao.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }
        if(movimentacao.getTipo().equals("d")){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder){

        //Configura AlertDialog
        AlertDialog.Builder alertDialog =  new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Movimentação da conta");
        alertDialog.setMessage("Voce tem certeza que desja realmente excluir esse item?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               int position =  viewHolder.getAdapterPosition();
               movimentacao =  movimentacaos.get(position);
                String emailUsuario = autenticado.getCurrentUser().getEmail();
                String idUsuario = Java64Custom.nomeCodificarBase64(emailUsuario);
                movimentacaoRef =  firebaseRef.child("movimentacao")
                        .child(idUsuario)
                        .child(mesano);
                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();


            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PrincipalActivity.this,"Negado", Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
       usuarioDataBase.removeEventListener( valueEventListener);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacao();
    }

    public void createRecyclerView(){
        recyclerView = findViewById(R.id.recyclerMovimentacao);
        recyclerView.setHasFixedSize(true);
        adapterMovimentacao = new MvAdpter(movimentacaos, this);
        recyclerView.setAdapter(adapterMovimentacao);
        RecyclerView.LayoutManager layoutManager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

}