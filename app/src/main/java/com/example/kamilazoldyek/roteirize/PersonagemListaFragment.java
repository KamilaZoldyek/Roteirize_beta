package com.example.kamilazoldyek.roteirize;

import android.Manifest;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonagemListaFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private List<Personagem> lpers;
    private PersonagemRecyclerAdapter persAdapter;
    Context context;
    private TextView suchempty;
    private FloatingActionButton fab_novo_pers;
    private PersonagemNovoFragment nov_p;
    private List<Personagem> pers;
    private String tituloStr, absolute_path;
    private File dir;


    public PersonagemListaFragment() {
    }

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloStr = getArguments().getString("TITULO");


        dir = new File(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS +
                "/" + tituloStr + Constantes.PERSONAGENS);

        if(!dir.exists()){
            dir.mkdirs();
        }

    }

    @Nullable
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.personagem_lista_fragment,
                container, false);
        context = container.getContext();

        suchempty = v.findViewById(R.id.suchemptyPers);
        suchempty.setVisibility(View.GONE);
        getActivity().setTitle("Personagens");
        fab_novo_pers = v.findViewById(R.id.fab_novo_personagem);
        nov_p = new PersonagemNovoFragment();
        lpers = new ArrayList<>();
        pers = new ArrayList<>();
        pers = carregarPersonagens();

        myRecyclerView = v.findViewById(R.id.personagem_lista);
        persAdapter = new PersonagemRecyclerAdapter(getContext(),
                pers, communication);
        myRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        myRecyclerView.setAdapter(persAdapter);

        fab_novo_pers.setVisibility(View.VISIBLE);

        fab_novo_pers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
//                bundle.putInt("POSITION", position);
                nov_p.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.personagem_lista_container, nov_p)

                        .commit();
                fab_novo_pers.setVisibility(View.GONE);
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
    private List<Personagem> carregarPersonagens() {

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
//                    regex(content);
                }
            } else if (dir.length()==0){
                suchempty.setVisibility(View.VISIBLE);
            }

            if(lpers.isEmpty()) {
                suchempty.setVisibility(View.VISIBLE);
            }
        }
        return lpers;
    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void respostaNotas(int position, String titulo,
                                  String corpo, String id) {
//
        }

        @Override
        public void respostaProjetos(int position, String titulo) {

        }

        @Override
        public void respostaCapitulos(int position, String nome_capitulo, String titulo_projeto, String escrito) {

        }

        @Override
        public void respostaPersonagens(int position, String titulo,
                                        String nome, String apelido,
                                        String sexo, String idade,
                                        String data, String descrição,
                                        String path) {

            PersonagemListaItemOpcoesFragment fragB =
                    new PersonagemListaItemOpcoesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TITULO", tituloStr);
            bundle.putString("NOME", nome);
//            bundle.putInt("POSITION", position);
            bundle.putString("APELIDO", apelido);
            bundle.putString("SEXO", sexo);
            bundle.putString("IDADE", idade);
            bundle.putString("DATAN", data);
            bundle.putString("DESC", descrição);
            bundle.putString("PATH", path);
            fragB.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.personagem_lista_container, fragB)

                    .commit();
            fab_novo_pers.setVisibility(View.GONE);
        }

        @Override
        public void respostaCenario(int position, String titulo,
                                    String nome, String descricao,
                                    String historia,
                                    String importancia,
                                    String path) {

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
        String apelido = tokens.nextToken();
        String cinco = tokens.nextToken();
        String sexo = tokens.nextToken();
        String sete = tokens.nextToken();
        String idade = tokens.nextToken();
        String nove = tokens.nextToken();
        String datan = tokens.nextToken();
        String onze = tokens.nextToken();
        String desc = tokens.nextToken();

        lpers.add(new Personagem(tituloStr,nome, apelido, sexo, idade, datan, desc, path));


        Log.d("KAMIS", "splitmafile "+ path);



    }


    public boolean FileExists(String fname){

        File file = new File(Environment.getExternalStorageDirectory() + Constantes.DIRETORIO_NOTAS + "/" + fname);
        return file.exists();
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


//    public void regex(String content) {
//        Pattern myPattern = Pattern.compile("Nome: (.*)\nApelido: (.*)\nSexo: (.*)\nIdade: (.*)\nData de Nascimento: (.*)\nDescrição: ");
//        Matcher m = myPattern.matcher(content);
//        String nome = "";
//        String apelido = "";
//        String sexo = "";
//        String idade = "";
//        String datan = "";
//        String desc = "";
//
//        while (m.find()) {
//            nome = m.group(1);
//            apelido = m.group(2);
//            sexo = m.group(3);
//            idade = m.group(4);
//            datan = m.group(5);
//            desc = m.group(6);
//        }
//
//        lpers.add(new Personagem(nome, apelido, sexo, idade, datan, desc));
//        Log.d("KAMIS", "regex "+nome);
//
//    }

}
