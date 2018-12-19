package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjetosListaFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private List<Projeto> lproj;
    ProjetosRecyclerViewAdapter projAdapter;
    Context context;
    TextView suchempty;
    FloatingActionButton fab_novo_proj;
    ProjetoNovoFragment nov_p;
    List<String> projs;


    public ProjetosListaFragment(){}

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.projetos_lista_fragment, container, false);
        context = container.getContext();
        suchempty = v.findViewById(R.id.suchemptyP);
        suchempty.setVisibility(View.GONE);
        getActivity().setTitle("Projetos");
        fab_novo_proj = v.findViewById(R.id.fab_novo_projeto);
        nov_p = new ProjetoNovoFragment();
        lproj = new ArrayList<>();
        projs = new ArrayList<>();
        projs = carregarProjetos();
        if (projs.isEmpty()){
            suchempty.setVisibility(View.VISIBLE);
        }

//        prepareProjects();

        myRecyclerView = v.findViewById(R.id.proj_lista);
        projAdapter = new ProjetosRecyclerViewAdapter(getContext(), projs, communication);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(projAdapter);

        fab_novo_proj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.proj_lista_container, nov_p)
                        .commit();



                fab_novo_proj.setVisibility(View.GONE);




            }
        });
        return v;
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isExternalStorageReadable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            Log.i("State", "Opa, le sim");
            return true;
        }else{
            return false;
        }
    }

    private List<String> carregarProjetos() {

        List<String> fileList = new ArrayList<>();

        File root = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS);

        if(!root.exists()){root.mkdirs();}

        if(root.exists()){
            File[] files = root.listFiles();
            fileList.clear();
            for(File file : files){
                fileList.add(file.getName());
            }
        }

        return fileList;

    }


    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void respostaNotas(int position, String titulo, String corpo, String id) {
//
        }

        @Override
        public void respostaProjetos(int position, String titulo) {
            ProjetosListaItemOpcoesFragment fragB = new ProjetosListaItemOpcoesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TITULO", titulo);
            bundle.putInt("POSITION", position);
            fragB.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction
                    .replace(R.id.proj_lista_container, fragB)

                    .commit();
            fab_novo_proj.setVisibility(View.GONE);
        }

        @Override
        public void respostaCapitulos(int position, String nome_capitulo, String titulo_projeto, String escrito) {

        }

        @Override
        public void respostaPersonagens(int position,  String titulo, String nome, String apelido, String sexo, String idade, String data, String descrição, String path) {

        }

        @Override
        public void respostaCenario(int position, String titulo, String nome, String descricao, String historia, String importancia, String path) {

        }

        @Override
        public void respostaCena(int position, String nome_cena, String acao, String local, String personagens, String path, String titulo_projeto, String nome_capitulo) {

        }


    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void prepareProjects() {
        File dir;


        if (isExternalStorageReadable() && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//Environment.getDataDirectory()

            dir = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS);
            File[] files = dir.listFiles();
            String theFile;
            String content = "";

            if (dir.exists()) {

                if (dir.length() > 0) {
                    for (int f = 0; f < files.length; f++) {
                        theFile = files[f].getName();
                        File file = new File(dir, theFile);
                        if(file.exists()){
                            StringBuilder sb = new StringBuilder();
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                String line;
                                while ((line=br.readLine()) != null) {
                                    sb.append(line);
                                }
                                content = sb.toString();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "file not found", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "ioexception", Toast.LENGTH_SHORT).show();
                            }
                        }
                        lproj.add(new Projeto(theFile, content)) ;


                    }
                } else if (dir.length()==0){

                    suchempty.setVisibility(View.VISIBLE);

                }

                if(lproj.isEmpty()) {

                    suchempty.setVisibility(View.VISIBLE);
                }

            } else if (!dir.exists()){
                Toast.makeText(context, "Diretório não existe!", Toast.LENGTH_SHORT).show();
            }
        }else if (!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            Toast.makeText(context, "readable problem", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean FileExists(String fname){

        File file = new File(Environment.getExternalStorageDirectory() + Constantes.DIRETORIO_NOTAS + "/" + fname);
        return file.exists();
    }



}
