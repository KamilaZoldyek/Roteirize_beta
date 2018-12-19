package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProjetoNovoFragment extends Fragment {

    View v;
    FloatingActionButton fabsave;
    Context context;
    EditText nome_arq, descricao;
    ProjetosListaFragment fragB;
    List<Projeto> lproj;
    List<String> list;
    LinearLayout random;
    File dir;
    ProjetoNovoFragment projetoNovoFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        list = carregarProjetos();

        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS);
        if(!dir.exists()){dir.mkdirs();}

//        new code here


//       ends here
    }

    //
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.projeto_novo_fragment, container, false);




        context = container.getContext();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fragB = new ProjetosListaFragment();
        lproj = new ArrayList<>();

        getActivity().setTitle("Novo projeto");



        fabsave = v.findViewById(R.id.fab_new_project);
        nome_arq = v.findViewById(R.id.titulo_novo_projeto);
        descricao = v.findViewById(R.id.descricao_novo_projeto);
        random = v.findViewById(R.id.randomProject);


        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome_arq.setText(GeradorDeImprobabilidadeInfinita.titleGenerator());
                descricao.setText(GeradorDeImprobabilidadeInfinita.ultimatePlotGenerator());
            }
        });

        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = nome_arq.getText().toString();

                if (list.isEmpty()){
                    escreveArquivo(titulo);

                    ((FragmentActivity) getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.novo_projeto_container, fragB)
                            .commit();

                    fabsave.setVisibility(View.GONE);
                }else{
                    if(!titulo.equalsIgnoreCase("")){
                        if (!isSafe(titulo)){
                            escreveArquivo(titulo);

                            ((FragmentActivity) getContext()).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.novo_projeto_container, fragB)
                                    .commit();

                            fabsave.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(context, "Título não pode ser repetido!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(context, "Título não pode ser vazio!", Toast.LENGTH_SHORT).show();
                    }
                }
            }




//                else {
//                    Toast.makeText(context, "Título não pode ser repetido!", Toast.LENGTH_SHORT).show();
//                }


//            }
        });

        return v;
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
    private boolean isExternalStorageWritable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Opa, escreve sim");
            return true;
        }else{
            return false;
        }
    }
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
    public String addDateTime(){
        String date;
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        date = format.format(today);
        return date;
    }
    public void escreveArquivo(String titulo){

//        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

         File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/" + titulo);


        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    if(!f.exists()){
                        f.mkdirs();
                    }
                    try{
                        File text_file = new File(f, titulo + "«" + addDateTime()+ ".txt");
                        FileOutputStream fos = new FileOutputStream(text_file);
                        fos.write(descricao.getText().toString().getBytes());
                        fos.flush();
                        fos.close();

                        Toast.makeText(context, "Projeto criado", Toast.LENGTH_SHORT).show();
                    }catch(IOException e){
                        e.printStackTrace();
                    }

            File f2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + titulo + Constantes.PERSONAGENS);
            File f3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + titulo + Constantes.CENARIOS);
            File f4 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + titulo + Constantes.CAPITULOS);

            if(!f2.exists() && !f3.exists() && !f4.exists()){
                f2.mkdirs();
                f3.mkdirs();
                f4.mkdirs();
            }

        }

    }
    public boolean isSafe(String titulo){
        int i=0;
        while (i<list.size()-1) ++i;
        return list.get(i).equalsIgnoreCase(titulo);
    }
    private List<String> carregarProjetos() {


        List<String> fileList = new ArrayList<>();

        File root = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS);

        if(root.exists()){
            File[] files = root.listFiles();
            fileList.clear();
            for(File file : files){
                fileList.add(file.getName());
            }
        }


        return fileList;

    }



}
