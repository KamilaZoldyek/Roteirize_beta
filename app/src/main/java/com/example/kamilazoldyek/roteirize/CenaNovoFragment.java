package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class CenaNovoFragment extends Fragment {

    private FloatingActionButton fabsave;
    Context context;
    private EditText nova_cena_acao, nova_cena_local, nova_cena_personagens, nova_cena_nome;
    private CenaListaFragment cenaListaFragment;
    private List<Cena> lcenas;
    private String tituloStr, path, capituloStr, cena, local, personagens, nome_cena;
    private File dir;
    LocalData localData;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloStr = getArguments().getString("TITULO");
        capituloStr = getArguments().getString("CAPITULO");
        cenaListaFragment = new CenaListaFragment();
        lcenas = new ArrayList<>();

        dir = new File(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS
                + "/" + tituloStr + Constantes.CAPITULOS+ "/"
                + capituloStr + "/" + Constantes.CENAS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate
                (R.layout.cena_nova_fragment, container, false);
        getActivity().getWindow()
                .setSoftInputMode(WindowManager
                        .LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getActivity().setTitle(capituloStr + "- Nova cena");

        localData = new LocalData(getContext());
        Boolean firsttime = localData.getFirstTimeUser();
        if (firsttime){
            firstTime();
        }


        fabsave = v.findViewById(R.id.fab_nova_cena_salvar);
        nova_cena_acao = v.findViewById(R.id.nova_cena_acao);
        nova_cena_local= v.findViewById(R.id.nova_cena_local);
        nova_cena_personagens= v.findViewById(R.id.nova_cena_personagens);
        nova_cena_nome= v.findViewById(R.id.nova_cena_nome);

        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome_cena = addSpace(nova_cena_nome.getText().toString());
                cena = addSpace(nova_cena_acao.getText().toString());
                local = addSpace(nova_cena_local.getText().toString());
                personagens = addSpace(nova_cena_personagens.getText().toString());

                escreveCena(nome_cena, cena, local, personagens);

                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("CAPITULO", capituloStr);
                bundle.putString("NOMEC", nome_cena);
                bundle.putString("ACAO", cena);
                bundle.putString("LOCAL", local);
                bundle.putString("PERSONAGENS", personagens);
                bundle.putString("PATH", path);

                cenaListaFragment.setArguments(bundle);

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cena_nova_container, cenaListaFragment)
                        .commit();

                fabsave.setVisibility(View.GONE);

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
    public void escreveCena(String nome, String acao, String local, String personagens){

        if(!dir.exists()){
            dir.mkdirs();
        }
        String stuff = "Ação: «" + acao + "«\n"
                + "Cenário e tempo: «" + local + "«\n"
                + "Personagens envolvidos: «" + personagens + "«\n";

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            try{
                File text_file = new File(dir, nome + "«" + addDateTime()+ ".txt");
                path = text_file.getAbsolutePath();
                FileOutputStream fos = new FileOutputStream(text_file);
                fos.write(stuff.getBytes());
                fos.flush();
                fos.close();

                Toast.makeText(getContext(), "Cena salva", Toast.LENGTH_SHORT).show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public String addSpace(String str){

        if (str.equalsIgnoreCase("")){
            str = "-";
        }

        return str;
    }

    public void firstTime(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.note_first_time_user, null);

        mBuilder.setPositiveButton("Entendi!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                localData.setFirstTimeUser(false);
                dialog.dismiss();
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

}
