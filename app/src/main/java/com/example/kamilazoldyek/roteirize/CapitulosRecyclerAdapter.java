package com.example.kamilazoldyek.roteirize;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CapitulosRecyclerAdapter extends
        RecyclerView.Adapter<CapitulosRecyclerAdapter.CapitulosViewHolder> {

    Context mContext;
    List<Capitulo> capitulosLista;
    private FragmentCommunication mCommunicator;
    LocalData localData;
    CenaListaFragment fragB;

    public CapitulosRecyclerAdapter(Context mContext,
                                    List<Capitulo> capituloLista,
                                    FragmentCommunication mCommunicator) {
        this.mContext = mContext;
        this.capitulosLista = capituloLista;
        this.mCommunicator = mCommunicator;
    }


    @NonNull
    @Override
    public CapitulosViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                       int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate
                (R.layout.capitulos_lista_content, parent, false);
        final CapitulosViewHolder vHolder =
                new CapitulosViewHolder(v, mCommunicator);

        return vHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CapitulosViewHolder holder,
                                 int position) {

        final Capitulo current = capitulosLista.get(position);
        localData = new LocalData(mContext);
        fragB = new CenaListaFragment();

        holder.set_escrito.setVisibility(View.GONE);
        holder.p_titulo.setText(current.getTitulo_capitulo());
        String escrito = localData.getEscrito(current.getTitulo_capitulo());
        if (escrito.equals("1")){
            holder.set_escrito.setVisibility(View.VISIBLE);
        }

        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        bundle.putString("CAPITULO", current.getTitulo_capitulo());
        bundle.putString("ESCRITO", escrito);
        bundle.putString("TITULO", current.getProjeto_capitulo());
        bundle.putString("PATH", current.getCaminho_capitulo());

        fragB.setArguments(bundle);
    }


    @Override
    public int getItemCount() {
        return capitulosLista.size();
    }

    public class CapitulosViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener

    {
        private LinearLayout item;
        private TextView p_titulo;
        private ImageView set_escrito;
        FragmentCommunication comm;

        public CapitulosViewHolder(View itemView, FragmentCommunication communicator) {
            super(itemView);
            item = itemView.findViewById(R.id.capitulo_item );

            p_titulo = itemView.findViewById(R.id.nomeCapitulo);
            set_escrito = itemView.findViewById(R.id.capitulo_escrito);
            comm = communicator;

            item.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            comm.respostaCapitulos(getAdapterPosition(),
                    capitulosLista.get(getAdapterPosition()).getTitulo_capitulo(),
                    capitulosLista.get(getAdapterPosition()).getProjeto_capitulo(),
                    set_escrito.toString());


        }
    }
}
