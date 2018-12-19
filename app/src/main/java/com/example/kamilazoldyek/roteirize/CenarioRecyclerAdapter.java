package com.example.kamilazoldyek.roteirize;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CenarioRecyclerAdapter extends
        RecyclerView.Adapter<CenarioRecyclerAdapter.CenarioViewHolder> {

    Context mContext;
    List<Cenario> cenarioLista;
    private FragmentCommunication mCommunicator;

    public CenarioRecyclerAdapter(Context mContext, List<Cenario> cenarioLista, FragmentCommunication mCommunicator) {
        this.mContext = mContext;
        this.cenarioLista = cenarioLista;
        this.mCommunicator = mCommunicator;
    }

    @NonNull
    @Override
    public CenarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.cenario_lista_content, parent, false);
        final CenarioRecyclerAdapter.CenarioViewHolder vHolder = new CenarioRecyclerAdapter.CenarioViewHolder(v, mCommunicator);


        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CenarioRecyclerAdapter.CenarioViewHolder holder, int position) {

        final Cenario current = cenarioLista.get(position);
        holder.c_nome.setText(cenarioLista.get(position).getNome_cenario());

        CenarioListaItemOpcoesFragment fragB = new CenarioListaItemOpcoesFragment();

        Bundle bundle = new Bundle();
        bundle.putString("TITULO", current.getProjeto_cenario());
        bundle.putString("NOME", current.getNome_cenario());
        bundle.putString("DESC", current.getDescricao_cenario());
        bundle.putString("HISTORIA", current.getHistoria_cenario());
        bundle.putString("IMPORTANCIA", current.getImportancia_cenario());
        bundle.putString("PATH", current.getCaminho_cenario());
        fragB.setArguments(bundle);

    }

    @Override
    public int getItemCount() {
        return cenarioLista.size();
    }

    public class CenarioViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {


        private LinearLayout item;
        private TextView c_nome;
        private Cenario cenItem;
        FragmentCommunication comm;


        public CenarioViewHolder(View itemView, FragmentCommunication communication) {
            super(itemView);
            item = itemView.findViewById(R.id.cenario_item);
            c_nome = itemView.findViewById(R.id.nomeCenario);

            comm = communication;
            item.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            comm.respostaCenario(getAdapterPosition(),
                    cenarioLista.get(getAdapterPosition()).getProjeto_cenario(),
                    cenarioLista.get(getAdapterPosition()).getNome_cenario(),
                    cenarioLista.get(getAdapterPosition()).getDescricao_cenario(),
                    cenarioLista.get(getAdapterPosition()).getHistoria_cenario(),
                    cenarioLista.get(getAdapterPosition()).getImportancia_cenario(),
                    cenarioLista.get(getAdapterPosition()).getCaminho_cenario());

        }
    }
}
