package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CapituloListaFragment extends android.support.v4.app.Fragment {

    private RecyclerView myRecyclerView;
    private List<Capitulo> lchap;
    private CapitulosRecyclerAdapter capAdapter;
    Context context;
    private TextView suchempty, textview50;
    private FloatingActionButton fab_novo_capitulo;
    private String tituloStr;
    private LinearLayout infoLay;
    private File dir;
    private LocalData localData;



    public CapituloListaFragment() {    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tituloStr = getArguments().getString("TITULO");

        dir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS +
                "/" + tituloStr + Constantes.CAPITULOS);

        if(!dir.exists()){
            dir.mkdirs();

        }

        context = getContext();
        localData = new LocalData(context);

        lchap = new ArrayList<>();
        lchap = carregarCapitulos();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.capitulos_lista_fragment,
                container, false);


        getActivity().setTitle("Capítulos");

        fab_novo_capitulo = v.findViewById(R.id.fab_novo_capitulo_lista);

        suchempty = v.findViewById(R.id.suchemptyChap);
        suchempty.setVisibility(View.GONE);
        textview50 = v.findViewById(R.id.textView50);
        infoLay = v.findViewById(R.id.info_layout);
        if (lchap.isEmpty()){
            suchempty.setVisibility(View.VISIBLE);
            infoLay.setVisibility(View.GONE);
        }else{
            sortArrayList();
        }

        myRecyclerView = v.findViewById(R.id.capitulos_lista);
        capAdapter = new CapitulosRecyclerAdapter
                (getContext(), lchap, communication);
        myRecyclerView.setLayoutManager
                (new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(capAdapter);

//
//        fab_novo_capitulo.setVisibility(View.VISIBLE);

        fab_novo_capitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder =
                        new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.capitulo_novo_dialog, null);
                final EditText capituloNome = mView
                        .findViewById(R.id.capeditText);

                final FragmentManager manager = getFragmentManager();


                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nome = capituloNome.getText().toString();
                        int i=0;
                        boolean b = false;

                        if (lchap.size() == 0){
                            Log.d("KAMIS", nome +" lista vazia ");

                            escreveCapitulo(nome);
                        }

                        if (!nome.equalsIgnoreCase("")) {
                            while (i < lchap.size() - 1) {
                                if (lchap.get(i).getTitulo_capitulo().equalsIgnoreCase(nome)) {
                                    b = true;
                                }
                                ++i;
                            }
                        }

                        if(!nome.equalsIgnoreCase("")) {
                            if (!b) {

                                escreveCapitulo(nome);
                                CapituloListaFragment fragB = new CapituloListaFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("TITULO", tituloStr);
                                fragB.setArguments(bundle);
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.capitulos_lista_container, fragB)
                                        .commit();

                                fab_novo_capitulo.setVisibility(View.GONE);

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

                mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        CapituloListaFragment fragB = new CapituloListaFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", tituloStr);
                        fragB.setArguments(bundle);

                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.capitulos_lista_container, fragB)
                                .commit();
                        fab_novo_capitulo.setVisibility(View.INVISIBLE);
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
    private boolean isExternalStorageWritable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Opa, escreve sim");
            return true;
        }else{
            return false;
        }
    }
    private List<Capitulo> carregarCapitulos() {

        List<Capitulo> fileList = new ArrayList<>();

        File root = dir;
        File[] files = root.listFiles();
        fileList.clear();


        for(File file : files){
            fileList.add(new Capitulo(file.getName(),
                    tituloStr, file.getAbsolutePath()));
            Log.d("KAMIS", " carrega capitulos: "+ file.getName());
            localData.getEscrito(file.getName());
        }
        return fileList;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void escreveCapitulo(String titulo){

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + Constantes.CAPITULOS + "/" + titulo);

            File f2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + Constantes.CAPITULOS + "/" + titulo + Constantes.CENAS);


            if(!f.exists()){
                f.mkdirs();
                f2.mkdirs();


            }
            localData.setEscrito(titulo, "0");
        }
    }


    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void respostaNotas(int position, String titulo, String corpo, String id) {

        }

        @Override
        public void respostaProjetos(int position, String titulo) {

//            CenaListaFragment fragB = new CenaListaFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("TITULO", titulo);
//            bundle.putInt("POSITION", position);
//            fragB.setArguments(bundle);
//
//            FragmentManager manager = getFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.replace(R.id.capitulos_lista_container, fragB)
//                    .addToBackStack("listaProj")
//                    .commit();
//            fab_novo_capitulo.setVisibility(View.GONE);
//


        }

        @Override
        public void respostaCapitulos(int position,
                                      String nome_capitulo,
                                      String titulo_projeto,
                                      String escrito) {

            fab_novo_capitulo.setVisibility(View.GONE);

            CenaListaFragment cenaListaFragment =
                    new CenaListaFragment();

            Bundle bundle = new Bundle();
            bundle.putString("CAPITULO", nome_capitulo);
            bundle.putString("ESCRITO", escrito);
            bundle.putString("TITULO", titulo_projeto);
            cenaListaFragment.setArguments(bundle);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction

                    .replace(R.id.capitulos_lista_container, cenaListaFragment)

                    .commit();







            Log.d("KAMIS", "respostaCapituloLista " + nome_capitulo);


        }

        @Override
        public void respostaPersonagens(int position, String titulo, String nome, String apelido, String sexo, String idade, String data, String descrição, String path) {

        }

        @Override
        public void respostaCenario(int position, String titulo, String nome, String descricao, String historia, String importancia, String path) {

        }

        @Override
        public void respostaCena(int position, String nome_cena, String acao, String local, String personagens, String path, String titulo_projeto, String nome_capitulo) {

        }


    };


    private void sortArrayList(){
        Collections.sort(lchap, new Comparator<Capitulo>() {
            @Override
            public int compare(Capitulo o1, Capitulo o2) {
                return o1.getTitulo_capitulo().compareTo(o2.getTitulo_capitulo());
            }


        });
    }


    @Override
    public void onResume() {
        super.onResume();

        capAdapter.notifyDataSetChanged();

//        CapituloListaFragment fragB = new CapituloListaFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("TITULO", tituloStr);
//        fragB.setArguments(bundle);
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction
//                .replace(R.id.capitulos_lista_container, fragB);

    }





}
