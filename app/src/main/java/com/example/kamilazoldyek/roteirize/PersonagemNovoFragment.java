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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonagemNovoFragment extends Fragment {

    View v;
    FloatingActionButton fabsave;
    Context context;
    EditText nome, apelido, sexo, idade, data_nasc, descricao;
    PersonagemListaFragment fragB;
    List<Personagem> lpers;
    int position;
    String tituloStr, nomep,apelidop,sexop,idadep,datap,descp, nome_txt ;

    ImageView randomNome, randomSexo,randomIdade,randomNasc,randomDesc;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tituloStr = getArguments().getString("TITULO");
//        position = getArguments().getInt("POSITION");

        fragB = new PersonagemListaFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.personagem_novo_fragment, container, false);
        context = container.getContext();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        fragB = new PersonagemListaFragment();
        lpers = new ArrayList<>();
        getActivity().setTitle("Novo personagem");


        fabsave = v.findViewById(R.id.fab_new_pers);
        nome = v.findViewById(R.id.novo_personagem_nome);
        apelido = v.findViewById(R.id.novo_personagem_apelido);
        sexo = v.findViewById(R.id.novo_personagem_sexo);
        idade = v.findViewById(R.id.novo_personagem_idade);
        data_nasc = v.findViewById(R.id.novo_personagem_birth);
        descricao = v.findViewById(R.id.novo_personagem_descip);

        randomNome = v.findViewById(R.id.randomName);
        randomSexo = v.findViewById(R.id.randomSex);
        randomIdade = v.findViewById(R.id.randomIdade);
        randomNasc = v.findViewById(R.id.randomDataLocal);
        randomDesc = v.findViewById(R.id.randomCaracteristicas);

        LinearLayout randomAll = v.findViewById(R.id.randomAll);



        randomAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome.setText(GeradorDeImprobabilidadeInfinita.fullNameGenerator());
                sexo.setText(GeradorDeImprobabilidadeInfinita.genderGenerator());
                idade.setText(GeradorDeImprobabilidadeInfinita.ageGenerator());
                data_nasc.setText(GeradorDeImprobabilidadeInfinita.birthDateGenerator());
                descricao.setText(GeradorDeImprobabilidadeInfinita.personFeaturesGenerator());
            }
        });


        randomNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome.setText(GeradorDeImprobabilidadeInfinita.fullNameGenerator());
            }
        });
        randomSexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexo.setText(GeradorDeImprobabilidadeInfinita.genderGenerator());
            }
        });
        randomIdade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idade.setText(GeradorDeImprobabilidadeInfinita.ageGenerator());

            }
        });
        randomNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data_nasc.setText(GeradorDeImprobabilidadeInfinita.birthDateGenerator());
            }
        });
        randomDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descricao.setText(GeradorDeImprobabilidadeInfinita.personFeaturesGenerator());
            }
        });


        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        .replace(R.id.personagem_novo_container, fragB)
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
    public void escrevePersonagem(String nome){



        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        String stuff = "Nome: «" + nome + "«\n"
                + "Apelido: «" + apelidop + "«\n"
                + "Gênero: «" + sexop + "«\n"
                + "Idade: «" + idadep + "«\n"
                + "Data de Nascimento: «" + datap + "«\n"
                + "Descrição: «" + descp + "«\n";


        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + Constantes.PERSONAGENS);

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

                Toast.makeText(context, "Personagem criado", Toast.LENGTH_SHORT).show();
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
}
