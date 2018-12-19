package com.example.kamilazoldyek.roteirize;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import java.io.File;

public class CenarioListaItemOpcoesFragment extends Fragment {

    boolean isOpen = false;
    private Animation fabopen, fabclose, rotateforward, rotatebackward;
    private FloatingActionButton fabexcluir, fabeditar, fabopcoes;
    private LinearLayout editarLay, excluirLay;

    private String tituloStr, nomeStr, descricaoStr, historiaStr, importanciaStr, pathStr;
    int position;

    private View v;
    private Context context;

    private CenarioEditarFragment fragB;
    private CenarioListaFragment frag;
    private TextView cen_nome, cen_info;

    public CenarioListaItemOpcoesFragment() {    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tituloStr = getArguments().getString("TITULO");
        nomeStr = getArguments().getString("NOME");
        descricaoStr = getArguments().getString("DESC");
        historiaStr = getArguments().getString("HISTORIA");
        importanciaStr = getArguments().getString("IMPORTANCIA");
        pathStr = getArguments().getString("PATH");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        isOpen = false;

        v = inflater.inflate(R.layout.cenario_lista_opcoes_fragment, container, false);
        context = container.getContext();

        frag = new CenarioListaFragment();

        getActivity().setTitle("Cenário");

        cen_nome = v.findViewById(R.id.cen_nome_op);
        cen_info = v.findViewById(R.id.cen_desc_op);


        cen_nome.setText(nomeStr);
        cen_info.setText(
                "▪ Projeto: " + tituloStr + "\n\n"
                        + "▪ Descrição: " + descricaoStr + "\n\n"
                        + "▪ História do lugar: " + historiaStr + "\n\n"
                        + "▪ Importância do local para o projeto: " + importanciaStr + "\n\n"

        );

        cen_info.setMovementMethod(new ScrollingMovementMethod());

        fabopcoes = v.findViewById(R.id.fab_opcoes_do_cenario);
        fabeditar = v.findViewById(R.id.fab_editar_cenario);

        editarLay = v.findViewById(R.id.cenario_editar_layout);
        excluirLay = v.findViewById(R.id.cenario_excluir_layout);
        fabexcluir= v.findViewById(R.id.fab_excluir_cenario);

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


                fragB = new CenarioEditarFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("NOME", nomeStr);
                bundle.putString("DESC", descricaoStr);
                bundle.putString("HISTORIA", historiaStr);
                bundle.putString("IMPORTANCIA", importanciaStr);
                bundle.putString("PATH", pathStr);
                fragB.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cenario_item_opcoes_container, fragB)
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
                            Toast.makeText(getContext(), "Cenario apagado", Toast.LENGTH_SHORT).show();

                            CenarioListaFragment fragC = new CenarioListaFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("TITULO", tituloStr);
                            fragC.setArguments(bundle);

                            ((FragmentActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.cenario_item_opcoes_container, fragC)
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


//        ========================================



        fabeditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fragB = new CenarioEditarFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("NOME", nomeStr);
                bundle.putString("DESC", descricaoStr);
                bundle.putString("HISTORIA", historiaStr);
                bundle.putString("IMPORTANCIA", importanciaStr);
                bundle.putString("PATH", pathStr);
                fragB.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cenario_item_opcoes_container, fragB)
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
                            Toast.makeText(getContext(), "Cenario apagado", Toast.LENGTH_SHORT).show();

                            CenarioListaFragment fragC = new CenarioListaFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("TITULO", tituloStr);
                            fragC.setArguments(bundle);

                            ((FragmentActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.cenario_item_opcoes_container, fragC)
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
}
