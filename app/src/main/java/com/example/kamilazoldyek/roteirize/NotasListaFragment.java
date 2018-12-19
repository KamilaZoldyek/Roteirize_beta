package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotasListaFragment extends android.support.v4.app.Fragment {

    private RecyclerView myRecyclerView;
    public List<Nota> lnotas;
    NotasRecyclerAdapter notasListaAdapter;
    Context context;

    File dir;
    SuchEmptyFragment fragB;

    TextView suchempty;
    FloatingActionButton fab_nova_nota;
    NotaNovaFragment nov_f;

    public NotasListaFragment(){
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.notas_lista_fragment, container, false);
        context = container.getContext();
        fragB = new SuchEmptyFragment();
        suchempty = v.findViewById(R.id.suchempty);
        suchempty.setVisibility(View.GONE);
        getActivity().setTitle("Anotações");
        fab_nova_nota = v.findViewById(R.id.fab_nova_anotacao);
        nov_f = new NotaNovaFragment();
        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_NOTAS);
        if(!dir.exists()){dir.mkdirs();}
        lnotas = new ArrayList<>();

        prepareNotes();

        myRecyclerView = v.findViewById(R.id.notas_lista);
        notasListaAdapter = new NotasRecyclerAdapter(getContext(), lnotas, communication);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(notasListaAdapter);

        fab_nova_nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
//                        .addToBackStack("NOTALIST")
                        .replace(R.id.notas_lista_container, nov_f)

                        .commit();
                fab_nova_nota.setVisibility(View.GONE);
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
    private void prepareNotes() {


        if (isExternalStorageReadable() && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//Environment.getDataDirectory()


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
                        lnotas.add(new Nota(theFile, content)) ;


                    }
                } else if (dir.length()==0){

                    suchempty.setVisibility(View.VISIBLE);

                }

                if(lnotas.isEmpty()) {

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

    public String openFile(String filename)
    {
        String content = new String();

        StringBuilder txt = new StringBuilder();


        FileInputStream fis;

        if(FileExists(filename)){
            try{

                int n;
                fis = getContext().openFileInput(filename);
                StringBuffer fileContent = new StringBuffer();
                byte[] buffer = new byte[1024];
                while ((n=fis.read(buffer))!=-1){
                    fileContent.append(new String(buffer, 0, n));
                }
                content = fileContent.toString();
                fis.close();



//                BufferedReader br = new BufferedReader(new FileReader(filename));
//                String line;
//
//                while ((line = br.readLine()) != null){
//
//                    Toast.makeText(context, line, Toast.LENGTH_SHORT).show();
//
//                    txt.append(line);
//                    txt.append('\n');
//                }
//                content = txt.toString();
//
//                br.close();









//
//
//                InputStream in = getContext().openFileInput(filename);
//                if(in != null){
//                    Toast.makeText(context, "oh hai", Toast.LENGTH_SHORT).show();
//                    InputStreamReader tmp = new InputStreamReader(in);
//                    BufferedReader reader = new BufferedReader(tmp);
//                    String str;
//                    StringBuilder buf = new StringBuilder();
//                    while((str = reader.readLine())!=null){
//                        buf.append(str + "\n");
//                    }in.close();
//                    content = buf.toString();
//
//                }

                Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(context, "nem existe", Toast.LENGTH_SHORT).show();
        }
        return content;

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



    public List<Nota> createDummyList(List<Nota> lista){
        for (int j = 1; j>21; j++){
            lista.add(new Nota("Dummy item #" + j, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur"));
        }
        return lista;
    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void respostaNotas(int position, String titulo, String corpo, String id) {
            NotaItemOpcoesFragment fragB = new NotaItemOpcoesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TITULO", titulo);
            bundle.putString("CORPO", corpo);
            bundle.putString("ID", id);
            bundle.putInt("POSITION", position);
            fragB.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction
//                    .addToBackStack("NOTALIST")
                    .replace(R.id.notas_lista_container, fragB)
                    .commit();

            fab_nova_nota.setVisibility(View.GONE);
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

        }

        @Override
        public void respostaCena(int position, String nome_cena, String acao, String local, String personagens, String path, String titulo_projeto, String nome_capitulo) {

        }


    };


//        lnotas.add(new Nota("Dummy 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua" + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua" + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
//                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));
//        lnotas.add(new Nota("Dummy 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));
//        lnotas.add(new Nota("Dummy 3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));
//        lnotas.add(new Nota("Dummy 4", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"));



}
