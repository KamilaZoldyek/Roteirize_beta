package com.example.kamilazoldyek.roteirize;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

public class NotasRecyclerAdapter extends RecyclerView.Adapter<NotasRecyclerAdapter.NotasViewHolder> {

    List<Nota> notasLista;
    Context mContext;
    private FragmentCommunication mCommunicator;



    public NotasRecyclerAdapter(Context mContext, List<Nota> notasLista, FragmentCommunication comm) {
        this.mContext = mContext;
        this.notasLista = notasLista;
        this.mCommunicator = comm;
    }


    @NonNull
    @Override
    public NotasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//parent.getContext() no lugar de mContext
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notas_lista_content, parent, false);
        final NotasViewHolder vHolder = new NotasViewHolder(v, mCommunicator);
        return vHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final NotasViewHolder holder, int position) {

        final Nota current = notasLista.get(position);
        String tituloF = formatTitle(notasLista.get(position).getTitulo_nota());
        holder.titulo.setText(tituloF);
        holder.corpo.setText(notasLista.get(position).getCorpo_nota());

        NotaItemOpcoesFragment fragB = new NotaItemOpcoesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITULO", current.getTitulo_nota());
        bundle.putString("CORPO", current.getCorpo_nota());
        bundle.putString("ID", current.getId_nota());
        bundle.putInt("POSITION", position);
        fragB.setArguments(bundle);
    }


    public Object getItemPosition(int position){
        return notasLista.get(position);
    }

    @Override
    public int getItemCount() {
            return notasLista.size();
    }

public class NotasViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
{
        public TextView titulo, corpo;
        private LinearLayout item;
        private Nota notaItem;
        FragmentCommunication comm;

        public NotasViewHolder(View itemView, FragmentCommunication communicator) {
            super(itemView);
            item = itemView.findViewById(R.id.notas_item);
            titulo = itemView.findViewById(R.id.titulo_nota_content);
            corpo = itemView.findViewById(R.id.corpo_nota_content);
            comm = communicator;
            item.setOnClickListener(this);
            titulo.setOnClickListener(this);
            corpo.setOnClickListener(this);
        }


        public Nota getNota(){
            return notaItem;
        }

        public void setNota(Nota nota){
            notaItem = nota;
            titulo.setText(nota.getTitulo_nota());
            corpo.setText(nota.getCorpo_nota());
        }

        @Override
        public void onClick(View v) {
            comm.respostaNotas(getAdapterPosition(),
                    notasLista.get(getAdapterPosition()).getTitulo_nota(),
                    notasLista.get(getAdapterPosition()).getCorpo_nota(),
                    notasLista.get(getAdapterPosition()).getId_nota());
        }

    }

    public String formatTitle(String title){

        StringTokenizer tokens = new StringTokenizer(title, "«");
        String first = tokens.nextToken();
        return first;

    }

















}
