package com.example.kamilazoldyek.roteirize;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.StringTokenizer;

public class CenaItemOpcoesFragment extends Fragment {

    boolean isOpen = false;
    Animation fabopen, fabclose, rotateforward, rotatebackward;
    FloatingActionButton fabexcluir, fabeditar, fabopcoes;
    LinearLayout editarLay, excluirLay;

    String tituloStr, capituloStr, nomecenaStr, acaoStr,
            localStr, personagensStr, pathStr,tituloFormatado, capituloREAL;

    CenaListaFragment cenaListaFragment;
    CenaEditarFragment cenaEditarFragment;
    TextView cena_nome, cena_descricao;
    File dirCapitulos;

    Context context;

    public CenaItemOpcoesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nomecenaStr = getArguments().getString("NOMEC");
        acaoStr = getArguments().getString("ACAO");
        localStr = getArguments().getString("LOCAL");
        personagensStr = getArguments().getString("PERSONAGENS");
        pathStr = getArguments().getString("PATH");
        tituloStr = getArguments().getString("TITULO");
        capituloStr = getArguments().getString("CAPITULO"); //tituloprojeto e titulocapitulo estao iguais
                                                                        //path é o titulo do capitulo

//        capituloREAL = pathStr;
//        pathStr = "";

        dirCapitulos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + "/" + Constantes.CAPITULOS + "/" + capituloStr);




        cenaListaFragment = new CenaListaFragment();
        cenaEditarFragment = new CenaEditarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        isOpen = false;

        View v = inflater.inflate(R.layout.cena_lista_opcoes_fragment, container, false);
        context = container.getContext();
        tituloFormatado = formattitle(nomecenaStr);
        getActivity().setTitle(capituloStr + " - " + tituloFormatado);

        String stuff = "▪ Projeto: " + tituloStr + "\n\n"
                + "▪ Capítulo: " + capituloStr + "\n\n"
                + "▪ O que acontece nessa cena: " + acaoStr + "\n\n"
                + "▪ Personagens: " + personagensStr + "\n\n"
                + "▪ Cenário: " + localStr + "\n\n";

        cena_nome = v.findViewById(R.id.cena_nome_op);
        cena_descricao = v.findViewById(R.id.cena_desc_op);



        cena_nome.setText(tituloFormatado);
        cena_descricao.setText(stuff);

        fabopcoes = v.findViewById(R.id.fab_opcoes_da_cena);
        fabexcluir = v.findViewById(R.id.fab_excluir_cena);
        fabeditar = v.findViewById(R.id.fab_cena_editar);

        editarLay = v.findViewById(R.id.cena_editar_layout);
        excluirLay = v.findViewById(R.id.cena_excluir_layout);

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

                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("CAPITULO", capituloStr);
                bundle.putString("NOMEC", tituloFormatado);
                bundle.putString("ACAO", acaoStr);
                bundle.putString("LOCAL", localStr);
                bundle.putString("PERSONAGENS", personagensStr);
                bundle.putString("PATH", pathStr);
                cenaEditarFragment.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cena_item_opcoes_container, cenaEditarFragment)
                        .commit();
                fabopcoes.setVisibility(View.GONE);


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
                            Toast.makeText(getContext(), "Cena apagada", Toast.LENGTH_SHORT).show();


                            Bundle bundle = new Bundle();
                            bundle.putString("TITULO", tituloStr);
                            bundle.putString("CAPITULO", capituloStr);
                            cenaListaFragment.setArguments(bundle);

                            ((FragmentActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.cena_item_opcoes_container, cenaListaFragment)
                                    .commit();
                            fabopcoes.setVisibility(View.GONE);

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

        fabeditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("CAPITULO", capituloStr);
                bundle.putString("NOMEC", tituloFormatado);
                bundle.putString("ACAO", acaoStr);
                bundle.putString("LOCAL", localStr);
                bundle.putString("PERSONAGENS", personagensStr);
                bundle.putString("PATH", pathStr);
                cenaEditarFragment.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cena_item_opcoes_container, cenaEditarFragment)
                        .commit();
                fabopcoes.setVisibility(View.GONE);


            }
        });

        fabexcluir.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(getContext(), "Cena apagada", Toast.LENGTH_SHORT).show();


                            Bundle bundle = new Bundle();
                            bundle.putString("TITULO", tituloStr);
                            bundle.putString("CAPITULO", capituloStr);
                            cenaListaFragment.setArguments(bundle);

                            ((FragmentActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.cena_item_opcoes_container, cenaListaFragment)
                                    .commit();
                            fabopcoes.setVisibility(View.GONE);

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

    public String formattitle(String nomeCapitulo){

        StringTokenizer tokens = new StringTokenizer(nomeCapitulo, "«");
        String first = tokens.nextToken();

        return first;


    }


}
