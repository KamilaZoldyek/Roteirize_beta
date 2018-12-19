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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonagemEditarFragment extends Fragment {

    private View v;
    private FloatingActionButton fabsave;
    private Context context;
    private EditText nome, apelido, sexo, idade, data_nasc, descricao;
    int position;
    private String tituloStr, apelidop,sexop,idadep,datap,descp, nome_txt;
    private String nomeStr,apelidoStr,sexoStr,idadeStr,dataNascStr,descStr, pathStr;
    private PersonagemListaFragment fragB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tituloStr = getArguments().getString("TITULO");
        nomeStr = getArguments().getString("NOME");
        apelidoStr = getArguments().getString("APELIDO");
        sexoStr = getArguments().getString("SEXO");
        idadeStr = getArguments().getString("IDADE");
        dataNascStr = getArguments().getString("DATAN");
        descStr = getArguments().getString("DESC");
        pathStr = getArguments().getString("PATH");

        fragB = new PersonagemListaFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.personagem_editar_fragment, container, false);
        context = container.getContext();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        fragB = new PersonagemListaFragment();
        getActivity().setTitle("Editar personagem");


        fabsave = v.findViewById(R.id.fab_edit_pers);
        nome = v.findViewById(R.id.novo_personagem_nome_edit);
        apelido = v.findViewById(R.id.novo_personagem_apelido_edit);
        sexo = v.findViewById(R.id.novo_personagem_sexo_edit);
        idade = v.findViewById(R.id.novo_personagem_idade_edit);
        data_nasc = v.findViewById(R.id.novo_personagem_birth_edit);
        descricao = v.findViewById(R.id.novo_personagem_descip_edit);

        nome.setText(nomeStr);
        apelido.setText(apelidoStr);
        sexo.setText(sexoStr);
        idade.setText(idadeStr);
        data_nasc.setText(dataNascStr);
        descricao.setText(descStr);

        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteFile(pathStr);

                apelidop = addSpace(apelido.getText().toString());
                sexop = addSpace(sexo.getText().toString());
                idadep = addSpace(idade.getText().toString());
                datap = addSpace(data_nasc.getText().toString());
                descp = addSpace(descricao.getText().toString());



                if (nome.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Nome não pode ser vazio!", Toast.LENGTH_SHORT).show();
                } else {
                    escrevePersonagem(nome.getText().toString());

                    Bundle bundle = new Bundle();
//                    bundle.putInt("POSITION", position);
                    bundle.putString("TITULO", tituloStr);
                    fragB.setArguments(bundle);

                    ((FragmentActivity) getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.personagem_editar_container, fragB)
                            .commit();

                    fabsave.setVisibility(View.GONE);

                }
            }
        });

        return v;
    }

    public boolean deleteFile(String theFile){
        File file = new File(theFile);
        boolean deleted = file.delete();
        return deleted;
    }
    public String addSpace(String str){

        if (str.equalsIgnoreCase("")){
            str = "   ";
        }

        return str;
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
    public void escrevePersonagem(String nome){



        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        String stuff = "Nome: «" + nome + "«\n"
                + "Apelido: «" + apelidop + "«\n"
                + "Sexo: «" + sexop + "«\n"
                + "Idade: «" + idadep + "«\n"
                + "Data de Nascimento: «" + datap + "«\n"
                + "Descrição: «" + descp + "«\n";


        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + Constantes.PERSONAGENS);
            Log.d("NOVOPERS", f.getAbsolutePath());

            if(!f.exists()){
                f.mkdirs();
            }
            try{
                File text_file = new File(f, nome + "«" + addDateTime()+ ".txt");
                FileOutputStream fos = new FileOutputStream(text_file);

                fos.write(stuff.getBytes());

                fos.flush();
                fos.close();

                nome_txt = text_file.getAbsolutePath();
                Log.d("KAMIS", "novo personagem "+nome_txt);

                Toast.makeText(context, "Personagem editado", Toast.LENGTH_SHORT).show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
}
