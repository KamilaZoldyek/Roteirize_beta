package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class PersonagemListaItemOpcoesFragment extends Fragment {

    boolean isOpen = false;
    Animation fabopen, fabclose, rotateforward, rotatebackward;
    FloatingActionButton fabexcluir, fabeditar, fabopcoes;
    LinearLayout editarLay, excluirLay;

    String tituloStr, nomeStr, apelidoStr, sexoStr, idadeStr, dataNascStr, descStr, pathStr;
    int position;

    View v;
    private Context context;

    PersonagemEditarFragment fragB;
    PersonagemListaFragment frag;
    TextView pers_nome, pers_info;

    public PersonagemListaItemOpcoesFragment() {
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tituloStr = getArguments().getString("TITULO");
        nomeStr = getArguments().getString("NOME");
        apelidoStr = getArguments().getString("APELIDO");
        sexoStr = getArguments().getString("SEXO");
        idadeStr = getArguments().getString("IDADE");
        dataNascStr = getArguments().getString("DATAN");
        descStr = getArguments().getString("DESC");
        pathStr = getArguments().getString("PATH");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        isOpen = false;

        v = inflater.inflate(R.layout.personagem_lista_opcoes_fragment, container, false);
        context = container.getContext();
        frag = new PersonagemListaFragment();
        getActivity().setTitle("Personagem");

        pers_nome = v.findViewById(R.id.pers_nome_op);
        pers_info = v.findViewById(R.id.personagem_desc_op);

        String stuff = "▪ Projeto: " + tituloStr + "\n\n"
                + "▪ Apelido: " + apelidoStr + "\n\n"
                + "▪ Gênero: " + sexoStr + "\n\n"
                + "▪ Idade: " + idadeStr + "\n\n"
                + "▪ Data e local de nascimento: " + dataNascStr + "\n\n"
                + "▪ Descrição: " + descStr + "\n\n"  ;
        pers_nome.setText(nomeStr);
        pers_info.setText(stuff);

        pers_info.setMovementMethod(new ScrollingMovementMethod());

        fabopcoes = v.findViewById(R.id.fab_opcoes_do_personagem);
        fabexcluir = v.findViewById(R.id.fab_excluir_personagem);
        fabeditar = v.findViewById(R.id.fab_editar_personagem);


        editarLay = v.findViewById(R.id.personagem_editar_layout);
        excluirLay = v.findViewById(R.id.personagem_excluir_layout);

        editarLay.setVisibility(View.GONE);
        excluirLay.setVisibility(View.GONE);

        fabopen = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_close);
        rotatebackward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_backward);
        rotateforward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_forward);

        fabopcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen == false){

                    excluirLay.setVisibility(View.VISIBLE);
                    editarLay.setVisibility(View.VISIBLE);
                    isOpen = true;

                }else{
                    excluirLay.setVisibility(View.GONE);
                    editarLay.setVisibility(View.GONE);
                    isOpen = false;
                }
            }
        });


        editarLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabopcoes.setVisibility(View.GONE);


                fragB = new PersonagemEditarFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("NOME", nomeStr);
                bundle.putString("APELIDO", apelidoStr);
                bundle.putString("SEXO", sexoStr);
                bundle.putString("IDADE", idadeStr);
                bundle.putString("DATAN", dataNascStr);
                bundle.putString("DESC", descStr);
                bundle.putString("PATH", pathStr);
                fragB.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()

                        .replace(R.id.personagem_item_opcoes_container, fragB)
                        .commit();



            }
        });

        excluirLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder =
                        new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.excluir_dialog, null);

                mBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (deleteFile(pathStr)){
                            Toast.makeText(getContext(), "Personagem apagado", Toast.LENGTH_SHORT).show();

                            fabopcoes.setVisibility(View.GONE);

                            PersonagemListaFragment fragC = new PersonagemListaFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("TITULO", tituloStr);
                            fragC.setArguments(bundle);

                            ((FragmentActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.personagem_item_opcoes_container, fragC)
                                    .commit();

                        }
                    }

                });
                mBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

        return v;

    }

    public boolean deleteFile(String theFile){
        File file = new File(theFile);
        boolean deleted = file.delete();
        return deleted;
    }




}
