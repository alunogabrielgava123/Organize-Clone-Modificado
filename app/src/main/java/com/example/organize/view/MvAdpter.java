package com.example.organize.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organize.Model.Movimentacao;
import com.example.organize.R;

import java.util.List;

public class MvAdpter extends RecyclerView.Adapter<MvAdpter.MovimentacaoViewHolder> {

    private List<Movimentacao> movimentacaos;
    private Context context;

    public  MvAdpter(List<Movimentacao> movimentacaos, Context context){
        this.movimentacaos = movimentacaos;
        this.context = context;
    }


    @NonNull
    @Override
    public MovimentacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()
        ).inflate(R.layout.movimentacao_item, parent,false);
        return new MovimentacaoViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimentacaoViewHolder holder, int position) {

        Movimentacao movimentacao =  movimentacaos.get(position);

        holder.textValor.setText(String.valueOf("R$ " + movimentacao.getValor()));
        holder.textDescricao.setText(movimentacao.getDescricao());
        holder.textCategoria.setText(movimentacao.getCategoria());
        if(movimentacao.getTipo().equals("d")){
            holder.textValor.setTextColor(context.getResources().getColor(R.color.colorAccentDespesa));
            holder.textValor.setText( "R$- " + movimentacao.getValor() );
        }
    }


    @Override
    public int getItemCount() {
        return movimentacaos.size();
    }

    public static  class  MovimentacaoViewHolder extends  RecyclerView.ViewHolder {

        TextView textDescricao , textCategoria, textValor;
        public MovimentacaoViewHolder(@NonNull View itemView) {
            super(itemView);

            textDescricao =  itemView.findViewById(R.id.textDescricao);
            textCategoria =  itemView.findViewById(R.id.textCategoria);
            textValor =  itemView.findViewById(R.id.textValor);

        }
    }


}
