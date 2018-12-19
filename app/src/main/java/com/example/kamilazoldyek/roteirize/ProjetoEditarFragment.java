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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjetoEditarFragment extends Fragment {

    private View v;
    private FloatingActionButton fabsave;
    private EditText descricao, nome_proj;
    private String tituloStr, corpoStr, tituloTXT;
    int position;
    List<Projeto> lproj;
    List<String> dirList;
    private ProjetosListaFragment frag;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloStr = getArguments().getString("TITULO");
        corpoStr = getArguments().getString("CORPO");
        position = getArguments().getInt("POSITION");
        tituloTXT = getArguments().getString("TITULOTXT");
        Log.d("PROJTXT", tituloTXT);

        lproj = new ArrayList<>();
        frag = new ProjetosListaFragment();
        dirList = new ArrayList<>();
        dirList = carregarProjetos();
        getActivity().setTitle("Editar");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.projeto_editar_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fabsave = v.findViewById(R.id.fab_save_project);
        descricao = v.findViewById(R.id.descricao_editar_projeto);
        nome_proj = v.findViewById(R.id.titulo_editar_projeto);

        descricao.setText(corpoStr);
        nome_proj.setText(tituloStr);

        fabsave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                renomearDir(nome_proj.getText().toString());

                if(deleteFile(tituloTXT)){

                    Toast.makeText(getContext(), "Editado!", Toast.LENGTH_SHORT).show();
                }


                escreveArquivo(nome_proj.getText().toString(), descricao.getText().toString());

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.editar_projeto_container, frag)
                        .commit();

                fabsave.setVisibility(View.GONE);
            }
        });




        return v;
    }

    private List<String> carregarProjetos() {

        List<String> fileList = new ArrayList<>();

        File root = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS);

        File[] files = root.listFiles();
        fileList.clear();

        for(File file : files){
            fileList.add(file.getName());
        }
        return fileList;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void escreveArquivo(String tituloNota, String corpoNota){

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr);
            Log.d("PROJPATH", f.getAbsolutePath());

            if(!f.exists()){
                f.mkdirs();
            }

            try{
                File text_file = new File(f, tituloNota + "«" + addDateTime()+ ".txt");
                FileOutputStream fos = new FileOutputStream(text_file);
                fos.write(corpoNota.getBytes());
                fos.flush();
                fos.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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



    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


    public boolean deleteFile(String theFile){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + "/" +theFile);
        Log.d("PROJTODEL", file.getAbsolutePath());
        boolean deleted = file.delete();
        return deleted;
    }
    public String addDateTime(){
        String date;
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        date = format.format(today);
        return date;
    }

    public void renomearDir(String novoNome){
        File antigo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr);
        File novo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/" + novoNome);

        try{
            String res = "false";
            boolean bool = antigo.renameTo(novo);
            if (bool){
                res="true";
            }
            Log.d("PROJ_RENAME",res);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("PROJDEURUIM", "deu exception");
        }


    }
}
