package com.example.kamilazoldyek.roteirize;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

public class CenaRecyclerAdapter extends RecyclerView.Adapter<CenaRecyclerAdapter.CenasViewHolder> {

    Context mContext;
    private List<Cena> cenasLista;
    private FragmentCommunication mCommunicator;

    public CenaRecyclerAdapter(Context mContext, List<Cena> cenasLista,
                               FragmentCommunication mCommunicator) {
        this.mContext = mContext;
        this.cenasLista = cenasLista;
        this.mCommunicator = mCommunicator;
    }

    @NonNull
    @Override
    public CenasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View v;
       v = LayoutInflater.from(mContext)
               .inflate(R.layout.cenas_lista_content ,parent, false);
       final CenasViewHolder vHolder =
               new CenasViewHolder(v, mCommunicator);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CenasViewHolder holder, int position) {
        final Cena current = cenasLista.get(position);
        String nomeFormatado = formatTitle(current.getNome_cena());


        holder.cena_nome.setText(nomeFormatado);
        holder.cena_descricao.setText(current.getAcao_cena());

        CenaItemOpcoesFragment cenaItemOpcoesFragment =
                new CenaItemOpcoesFragment();

        Bundle bundle = new Bundle();

        bundle.putString("NOMEC", current.getNome_cena());
        bundle.putString("ACAO", current.getAcao_cena());
        bundle.putString("LOCAL", current.getLocal_cena());
        bundle.putString("PERSONAGENS", current.getPersonagens_cena());

        bundle.putString("TITULO", current.getLivro_cena());
        bundle.putString("CAPITULO", current.getCapitulo_cena());
                bundle.putString("PATH", current.getCaminho_cena());


        cenaItemOpcoesFragment.setArguments(bundle);

    }



    @Override
    public int getItemCount() {
        return cenasLista.size();
    }

    public class CenasViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
    {

        LinearLayout item;
        TextView cena_nome;
        TextView cena_descricao;
        Personagem cenaItem;
        FragmentCommunication comm;

        public CenasViewHolder(View itemView, FragmentCommunication communication){
            super(itemView);

            item = itemView.findViewById(R.id.cenas_item);
            cena_nome = itemView.findViewById(R.id.tituloCenas);
            cena_descricao = itemView.findViewById(R.id.descricaoCenas);

            comm = communication;
            item.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            comm.respostaCena(getAdapterPosition(),
                    cenasLista.get(getAdapterPosition()).getNome_cena(),
                    cenasLista.get(getAdapterPosition()).getAcao_cena(),
                    cenasLista.get(getAdapterPosition()).getLocal_cena(),
                    cenasLista.get(getAdapterPosition()).getPersonagens_cena(),
                    cenasLista.get(getAdapterPosition()).getCapitulo_cena(),
                    cenasLista.get(getAdapterPosition()).getLivro_cena(),
                    cenasLista.get(getAdapterPosition()).getCaminho_cena());

        }
    }

    public String formatTitle(String title){

        StringTokenizer tokens = new StringTokenizer(title, "«");
        String first = tokens.nextToken();
        return first;

    }

    public String formatDesc(String title){

        StringTokenizer tokens = new StringTokenizer(title, "«");
        String first = tokens.nextToken();
        String second = tokens.nextToken();
        String third = tokens.nextToken();
        return second;

    }

}
