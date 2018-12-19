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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CapituloListaOpcoesFragment extends Fragment {

    private FloatingActionButton fab, fabexcluir, fabeditar;
    private TextView nome_do_capitulo;
    private Switch capitulo_concluido;
    private LinearLayout renomear_capitulo_layout, cenas_layout, delete_capitulo_layout;

    private CenaListaFragment cenaListaFragment;
    private CapituloListaFragment capituloListaFragment;
    private String tituloStr, capituloStr, escritoStr;
    private LocalData localData;
    private Context context;
    private List<Capitulo> list;
    private File dir;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloStr = getArguments().getString("TITULO");
        capituloStr = getArguments().getString("CAPITULO");
        dir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS +
                "/" + tituloStr + Constantes.CAPITULOS + "/"
                + capituloStr);
        cenaListaFragment = new CenaListaFragment();
        capituloListaFragment = new CapituloListaFragment();
        list = new ArrayList<>();
        localData = new LocalData(getContext());
        context = getContext();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.capitulo_lista_opcoes_fragment, container,
                false
        );

        fab = v.findViewById(R.id.fab_voltar_para_a_lista);
        nome_do_capitulo = v.findViewById(R.id.nome_do_capitulo_rename);
        capitulo_concluido = v.findViewById(R.id.switch_concluido);

        renomear_capitulo_layout = v.findViewById(R.id.renomear_capitulo_layout);
        cenas_layout = v.findViewById(R.id.cenas_layout);
        delete_capitulo_layout = v.findViewById(R.id.delete_capitulo_layout);


        nome_do_capitulo.setText(capituloStr);
        if (localData.getEscrito(capituloStr).equalsIgnoreCase("1")) {
            capitulo_concluido.setChecked(true);
        }else {
            capitulo_concluido.setChecked(false);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("CAPITULO", capituloStr);
                cenaListaFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace
                        (R.id.capitulo_item_opcoes_container, cenaListaFragment)

                        .commit();

            }
        });

        cenas_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("CAPITULO", capituloStr);
                cenaListaFragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.capitulo_item_opcoes_container, cenaListaFragment)

                        .commit();

            }
        });

        renomear_capitulo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder =
                        new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.capitulo_novo_dialog, null);
                mBuilder.setTitle("Renomear capítulo");
                final EditText capituloNome = mView
                        .findViewById(R.id.capeditText);
                capituloNome.setText(capituloStr);

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nome = capituloNome.getText().toString();
                        int i=0;
                        boolean b = false;

                        if (list.size() == 0){
                            Log.d("KAMIS", nome +" lista vazia ");

                        }

                        if (!nome.equalsIgnoreCase("")) {
                            while (i < list.size() - 1) {
                                if (list.get(i).getTitulo_capitulo().equalsIgnoreCase(nome)) {
                                    b = true;
                                }
                                ++i;
                            }
                        }

                        if(!nome.equalsIgnoreCase("")) {
                            if (!b) {

                                renomearDir(nome);
                                CapituloListaOpcoesFragment fragB = new CapituloListaOpcoesFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("TITULO", tituloStr);
                                bundle.putString("CAPITULO", nome);

                                fragB.setArguments(bundle);
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.capitulo_item_opcoes_container, fragB)

                                        .commit();

                                dialog.dismiss();
                            }else{

                                Toast.makeText(context, "Título existente! Tente outro :)",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, "Título não pode ser vazio!",
                                    Toast.LENGTH_SHORT).show();
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


        delete_capitulo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder =
                        new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.excluir_dialog, null);

                mBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(delete(dir)){
                            Toast.makeText(context, "Capítulo excluído: " + dir.getName(), Toast.LENGTH_SHORT).show();
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", tituloStr);
                        capituloListaFragment.setArguments(bundle);

                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.capitulo_item_opcoes_container, capituloListaFragment)
                                .commit();
//                        fab.setVisibility(View.GONE);

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

        capitulo_concluido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    localData.setEscrito(capituloStr, "1");
                }else{
                    localData.setEscrito(capituloStr, "0");
                }
            }
        });


        return v;
    }

    public boolean delete(File dir) {

        if (dir.exists()) {
            Log.d("KAMIS", "path pra apagar "+dir.getAbsolutePath());
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    delete(files[i]);
                } else {
                    files[i].delete();
                }
            }

        }
        return (dir.delete());


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
    public void renomearDir(String novoNome){
        File antigo = dir;
        File novo = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr
                + Constantes.CAPITULOS + "/" + novoNome);
        try{
            String res = "false";
            boolean bool = antigo.renameTo(novo);
            if (bool){
                res="true";
            }
            Log.d("KAMIS","PROJ_RENAME "+res);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("KAMIS","PROJDEURUIM "+ "deu exception");
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        CapituloListaOpcoesFragment fragB = new CapituloListaOpcoesFragment();

        Bundle bundle = new Bundle();
        bundle.putString("TITULO", tituloStr);
        bundle.putString("CAPITULO", capituloStr);
        fragB.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.capitulos_lista_container, fragB)
                .commit();

    }
}
