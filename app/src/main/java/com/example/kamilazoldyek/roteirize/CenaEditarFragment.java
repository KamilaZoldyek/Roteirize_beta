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
import java.util.Date;

public class CenaEditarFragment extends Fragment {

    private View v;
    private FloatingActionButton fabsave;
    private File dir;
    private Context context;
    private EditText nomeCena, acao, local, personagens;
    private String tituloStr, capituloStr,
            tituloCenaStr,acaoStr,localStr
            ,personagensStr, pathStr;

    private String titulop, capitulop,
            tituloCenap,acaop,localp
            ,personagensp, pathp;

    private CenaListaFragment cenaListaFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tituloStr = getArguments().getString("TITULO");
        tituloCenaStr = getArguments().getString("NOMEC");
        acaoStr = getArguments().getString("ACAO");
        localStr = getArguments().getString("LOCAL");
        personagensStr = getArguments().getString("PERSONAGENS");
        capituloStr = getArguments().getString("CAPITULO");
        pathStr = getArguments().getString("PATH");

        cenaListaFragment = new CenaListaFragment();

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

        v = inflater.inflate(R.layout.cena_editar_fragment, container, false);
        context = container.getContext();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getActivity().setTitle("Editar cena");

        fabsave = v.findViewById(R.id.fab_nova_cena_salvar_editar);
        nomeCena = v.findViewById(R.id.nova_cena_nome_editar);
        acao = v.findViewById(R.id.nova_cena_acao_editar);
        local = v.findViewById(R.id.nova_cena_local_editar);
        personagens = v.findViewById(R.id.nova_cena_personagens_editar);

        nomeCena.setText(tituloCenaStr);
        acao.setText(acaoStr);
        local.setText(localStr);
        personagens.setText(personagensStr);

        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteFile(pathStr);

                tituloCenap = addSpace(nomeCena.getText().toString());
                acaop = addSpace(acao.getText().toString());
                localp = addSpace(local.getText().toString());
                personagensp = addSpace(personagens.getText().toString());

                escreveCena(tituloCenap, acaop, localp, personagensp);

                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("CAPITULO", capituloStr);

                cenaListaFragment.setArguments(bundle);

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cena_editar_container, cenaListaFragment)
                        .commit();

                fabsave.setVisibility(View.GONE);
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
    public void escreveCena(String nome, String acao, String local, String personagens){


        String stuff = "Ação: «" + acao + "«\n"
                + "Cenário e tempo: «" + local + "«\n"
                + "Personagens envolvidos: «" + personagens + "«\n";

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            try{
                File text_file = new File(dir, nome + "«" + addDateTime()+ ".txt");
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

}
