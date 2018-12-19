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

public class PersonagemRecyclerAdapter extends RecyclerView.Adapter<PersonagemRecyclerAdapter.PersonagemViewHolder> {

    Context mContext;
    List<Personagem> personagemLista;
    private FragmentCommunication mCommunicator;


    public PersonagemRecyclerAdapter(Context mContext, List<Personagem> personagemLista, FragmentCommunication mCommunicator) {
        this.mContext = mContext;
        this.personagemLista = personagemLista;
        this.mCommunicator = mCommunicator;
    }

    @NonNull
    @Override
    public PersonagemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout
                .personagem_lista_content, parent, false);
        final PersonagemViewHolder vHolder =
                new PersonagemViewHolder(v, mCommunicator);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonagemViewHolder holder, int position) {

        final Personagem current = personagemLista.get(position);
        holder.p_nome.setText(personagemLista.get(position).getNome_personagem());

        PersonagemListaItemOpcoesFragment fragB = new PersonagemListaItemOpcoesFragment();

        Bundle bundle = new Bundle();
//        bundle.putInt("POSITION", position);
        bundle.putString("TITULO", current.getLivro_personagem());
        bundle.putString("NOME", current.getNome_personagem());
        bundle.putString("APELIDO", current.getApelido_personagem());
        bundle.putString("SEXO", current.getSexo_personagem());
        bundle.putString("IDADE", current.getIdade_personagem());
        bundle.putString("DATAN", current.getDatan_personagem());
        bundle.putString("DESC", current.getDesc_personagem());
        bundle.putString("ID", current.getId_personagem());
        bundle.putString("PATH", current.getCaminho_personagem());
        fragB.setArguments(bundle);
    }

    @Override
    public int getItemCount() {
        return personagemLista.size();
    }




    public class PersonagemViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
    {


        private LinearLayout item;
        private TextView p_nome;
        private Personagem persItem;
        FragmentCommunication comm;


        public PersonagemViewHolder(View itemView,
                                    FragmentCommunication communication) {
            super(itemView);
            item = itemView.findViewById(R.id.personagens_item);
            p_nome = itemView.findViewById(R.id.nomePers);

            comm = communication;
            item.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            comm.respostaPersonagens(getAdapterPosition(),
                    personagemLista.get(getAdapterPosition()).getLivro_personagem(),
                    personagemLista.get(getAdapterPosition()).getNome_personagem(),
                    personagemLista.get(getAdapterPosition()).getApelido_personagem(),
                    personagemLista.get(getAdapterPosition()).getSexo_personagem(),
                    personagemLista.get(getAdapterPosition()).getIdade_personagem(),
                    personagemLista.get(getAdapterPosition()).getDatan_personagem(),
                    personagemLista.get(getAdapterPosition()).getDesc_personagem(),
                    personagemLista.get(getAdapterPosition()).getCaminho_personagem());

        }
    }
}
