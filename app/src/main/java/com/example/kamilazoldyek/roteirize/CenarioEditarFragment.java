package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CenarioEditarFragment extends Fragment {

    private View v;
    private FloatingActionButton fabsave;
    private Context context;
    private EditText nome, descricao, historia, importancia;
    int position;
    private String tituloStr, nomeC,descricaoC,historiaC,importanciaC, nome_txt;
    private String nomeStr,descricaoStr,historiaStr,importanciaStr, pathStr;
    private CenarioListaFragment fragB;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tituloStr = getArguments().getString("TITULO");
        nomeStr = getArguments().getString("NOME");
        descricaoStr = getArguments().getString("DESC");
        historiaStr = getArguments().getString("HISTORIA");
        importanciaStr = getArguments().getString("IMPORTANCIA");
        pathStr = getArguments().getString("PATH");
        fragB = new CenarioListaFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.cenario_editar_fragment, container, false);
        context = container.getContext();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getActivity().setTitle("Editar cenário");

        fabsave = v.findViewById(R.id.fab_edit_scenerio);
        nome = v.findViewById(R.id.editar_cenario_nome);
        descricao = v.findViewById(R.id.editar_cenario_descip);
        historia = v.findViewById(R.id.editar_cenario_historia);
        importancia = v.findViewById(R.id.editar_cenario_importancia);


        nome.setText(nomeStr);
        descricao.setText(descricaoStr);
        historia.setText(historiaStr);
        importancia.setText(importanciaStr);

        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteFile(pathStr);

                descricaoC = addSpace(descricao.getText().toString());
                historiaC = addSpace(historia.getText().toString());
                importanciaC = addSpace(importancia.getText().toString());

                if (nome.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Nome não pode ser vazio!", Toast.LENGTH_SHORT).show();
                } else {
                    escreveCenario(nome.getText().toString());

                    Bundle bundle = new Bundle();
                    bundle.putString("TITULO", tituloStr);
                    fragB.setArguments(bundle);

                    ((FragmentActivity) getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.cenario_editar_container, fragB)
                            .commit();

                    fabsave.setVisibility(View.GONE);

                }
            }
        });



        return v;
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
    public void escreveCenario(String nome){



        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        String stuff = "Nome: «" + nome + "«\n"
                + "Descrição: «" + descricaoC + "«\n"
                + "História: «" + historiaC + "«\n"
                + "Importância para o projeto: «" + importanciaC + "«\n";

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + Constantes.CENARIOS);


            if(!f.exists()){
                f.mkdirs();
            }
            try{
                File text_file = new File(f, nome + "«" + addDateTime()+ ".txt");
                FileOutputStream fos = new FileOutputStream(text_file);

                fos.write(stuff.getBytes());

                fos.flush();
                fos.close();

                Toast.makeText(context, "Cenário editado", Toast.LENGTH_SHORT).show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
    public String addSpace(String str){

        if (str.equalsIgnoreCase("")){
            str = "-";
        }

        return str;
    }
    public boolean deleteFile(String theFile){
        File file = new File(theFile);
        boolean deleted = file.delete();
        return deleted;
    }
}
