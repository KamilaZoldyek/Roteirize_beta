package com.example.kamilazoldyek.roteirize;

import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CenarioListaFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private List<Cenario> lcen;
    private CenarioRecyclerAdapter cenAdapter;
    Context context;
    private TextView suchempty;
    private FloatingActionButton fab_novo_cen;
    private CenarioNovoFragment nov_c;
    private List<Cenario> cens;
    private String tituloStr, absolute_path;
    private File dir;



    public CenarioListaFragment() {    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloStr = getArguments().getString("TITULO");

        dir = new File(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS
                + "/" + tituloStr + Constantes.CENARIOS);

        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    @Nullable
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.cenario_lista_fragment, container, false);
        context = container.getContext();

        suchempty = v.findViewById(R.id.suchemptyCen);
        suchempty.setVisibility(View.GONE);

        getActivity().setTitle("Cenários");

        fab_novo_cen = v.findViewById(R.id.fab_novo_cenario_lista);

        nov_c = new CenarioNovoFragment();
        lcen = new ArrayList<>();
        cens = new ArrayList<>();
        cens = carregarCenarios();

        myRecyclerView = v.findViewById(R.id.cenario_lista);
        cenAdapter = new CenarioRecyclerAdapter(getContext(), cens, communication);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(cenAdapter);

        fab_novo_cen.setVisibility(View.VISIBLE);

        fab_novo_cen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                nov_c.setArguments(bundle);

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cenario_lista_container, nov_c)
                        .commit();

                fab_novo_cen.setVisibility(View.GONE);

            }
        });
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private List<Cenario> carregarCenarios() {

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
                    absolute_path = file.getAbsolutePath();
                    splitMaFile(content,absolute_path );
                }
            } else if (dir.length()==0){
                suchempty.setVisibility(View.VISIBLE);
            }

            if(lcen.isEmpty()) {
                suchempty.setVisibility(View.VISIBLE);
            }
        }

        return lcen;
    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void respostaNotas(int position, String titulo, String corpo, String id) {
//
        }

        @Override
        public void respostaProjetos(int position, String titulo) {

        }

        @Override
        public void respostaCapitulos(int position, String nome_capitulo, String titulo_projeto, String escrito) {

        }

        @Override
        public void respostaPersonagens(int position, String titulo, String nome, String apelido, String sexo, String idade, String data, String descrição, String path) {


        }

        @Override
        public void respostaCenario(int position, String titulo, String nome, String descricao, String historia, String importancia, String path) {


            CenarioListaItemOpcoesFragment fragB = new CenarioListaItemOpcoesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TITULO", tituloStr);
            bundle.putString("NOME", nome);
            bundle.putString("DESC", descricao);
            bundle.putString("HISTORIA", historia);
            bundle.putString("IMPORTANCIA", importancia);
            bundle.putString("PATH", path);
            fragB.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.cenario_lista_container, fragB)

                    .commit();
            fab_novo_cen.setVisibility(View.GONE);

        }

        @Override
        public void respostaCena(int position, String nome_cena, String acao, String local, String personagens, String path, String titulo_projeto, String nome_capitulo) {

        }


    };


    public void splitMaFile(String content, String path){

        StringTokenizer tokens = new StringTokenizer(content, "«");

        String um = tokens.nextToken();
        String nome = tokens.nextToken();
        String tres = tokens.nextToken();
        String descricao = tokens.nextToken();
        String cinco = tokens.nextToken();
        String historia = tokens.nextToken();
        String sete = tokens.nextToken();
        String importancia = tokens.nextToken();

        lcen.add(new Cenario(nome, descricao, historia, importancia, path, tituloStr));

    }





}
