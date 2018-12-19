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
import java.util.StringTokenizer;

public class ProjetosRecyclerViewAdapter extends
        RecyclerView.Adapter<ProjetosRecyclerViewAdapter.ProjetosViewHolder> {


    Context mContext;
    List<String> projetosLista;
    private FragmentCommunication mCommunicator;


    public ProjetosRecyclerViewAdapter(Context mContext, List<String> projetosLista, FragmentCommunication comm) {
        this.mContext = mContext;
        this.projetosLista = projetosLista;
        this.mCommunicator = comm;
    }

    @NonNull
    @Override
    public ProjetosViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.projetos_lista_content, parent, false);
        final ProjetosViewHolder vHolder = new ProjetosViewHolder(v, mCommunicator);

        return vHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProjetosViewHolder holder, int position) {

        final String current = projetosLista.get(position);

        holder.p_titulo.setText(projetosLista.get(position));

        ProjetosListaItemOpcoesFragment fragB = new ProjetosListaItemOpcoesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        bundle.putString("TITULO", current);
        fragB.setArguments(bundle);

    }


    @Override
    public int getItemCount() {
        return projetosLista.size();
    }

    public class ProjetosViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener

    {
        private LinearLayout item;
        private TextView p_titulo, p_descricao;
        private Projeto projItem;
        FragmentCommunication comm;

        public ProjetosViewHolder(View itemView, FragmentCommunication communicator) {
            super(itemView);
            item = itemView.findViewById(R.id.projetos_item );

            p_titulo = itemView.findViewById(R.id.tituloProjeto);
            comm = communicator;

            item.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            comm.respostaProjetos(getAdapterPosition(),
                    projetosLista.get(getAdapterPosition()));

            //wait, no button gone?
        }
    }

    public String formatTitle(String title){

        StringTokenizer tokens = new StringTokenizer(title, "«");
        String first = tokens.nextToken();
        return first;

    }



}
