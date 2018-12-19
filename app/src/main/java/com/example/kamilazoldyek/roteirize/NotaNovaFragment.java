package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
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

public class NotaNovaFragment extends Fragment {

    View v;
    FloatingActionButton fabsave;
    Context context;
    EditText nome_arq, corpo_texto;
    NotasListaFragment fragB;
    List<Nota> lnotas;
    LocalData localData;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        context = container.getContext();
        v = inflater.inflate(R.layout.notas_nova_fragment, container, false);




        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fragB = new NotasListaFragment();
        lnotas = new ArrayList<>();
        getActivity().setTitle("Nova anotação");

        localData = new LocalData(context);
        Boolean firsttime = localData.getFirstTimeUser();
        if (firsttime){
            firstTime();
        }

        carregarNotas();

        fabsave = v.findViewById(R.id.fab_new_note);

        nome_arq = v.findViewById(R.id.fragment_nota_titulo);
        corpo_texto = v.findViewById(R.id.nova_nota_corpo);

        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String st = nome_arq.getText().toString() + ".txt";
//                Toast.makeText(context, st + i +" e " + lnotas.get(i).getTitulo_nota(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Titulo existente!", Toast.LENGTH_SHORT).show();

                escreveArquivo();

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.notas_nova_container, fragB)
                        .commit();

                fabsave.setVisibility(View.GONE);
//                Toast.makeText(context, "SAAAAVE", Toast.LENGTH_SHORT).show();


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

    public boolean validacao(){

        for (int i = 0; i<lnotas.size(); i++){
            if (nome_arq.getText().toString().equalsIgnoreCase(lnotas.get(i).getTitulo_nota())){
                Toast.makeText(context, "Titulo existente!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void escreveArquivo(){


        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){


            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + Constantes.DIRETORIO_NOTAS);

            if(!f.exists()){
                f.mkdirs();
            }

            String titulo = nome_arq.getText().toString();


            try{

                File text_file = new File(f, titulo + "«" + addDateTime()+ ".txt");
                FileOutputStream fos = new FileOutputStream(text_file);
                fos.write(corpo_texto.getText().toString().getBytes());
                fos.flush();
                fos.close();

                Toast.makeText(context, "Anotação salva", Toast.LENGTH_SHORT).show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void carregarNotas() {

        if (isExternalStorageReadable() && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + Constantes.DIRETORIO_NOTAS);
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
                        lnotas.add(new Nota(theFile, content));
                    }
                } else if (dir.length() == 0){
                    Toast.makeText(context, "wow such empty", Toast.LENGTH_SHORT).show();
                }

            } else {
                //TODO algum jeito de mostrar uma lista vazia aqui!
                Toast.makeText(context, "diretorio nem existe", Toast.LENGTH_SHORT).show();
            }
        }else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            Toast.makeText(context, "readable problem", Toast.LENGTH_SHORT).show();
        }
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

    public boolean FileExists(String fname){
        File file = getContext().getFileStreamPath(fname);
        return file.exists();
    }

    public String verificaNome(String titulo){

        int fileNo = 0;
        String newFileName = "";

        try{
            File aFile = new File(Environment.getExternalStoragePublicDirectory(Environment
                    .DIRECTORY_DOCUMENTS) + Constantes.DIRETORIO_NOTAS, titulo + ".txt");
        if (aFile.exists()){
            Toast.makeText(context, "Titulo existente!", Toast.LENGTH_SHORT).show();
            while (aFile.exists()){
                fileNo++;
                aFile = new File(titulo + "(" + "_copy" + fileNo + ").txt");
                newFileName = titulo + "(" + "_copy" + fileNo + ")";
            }
        }else if(!aFile.exists()){
            aFile.createNewFile();
            newFileName = titulo;
        }
    }catch(IOException e){
            e.printStackTrace();
            Toast.makeText(context, "IOException", Toast.LENGTH_SHORT).show();
        }



        return newFileName;





//        String st = titulo + ".txt";
//
//
//            for (int i = 0; i<lnotas.size(); i++){
//                if (lnotas.get(i).getTitulo_nota().equals(st)){
//
//                    Toast.makeText(context, "st "+ st, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, "t nota: "+ lnotas.get(i).getTitulo_nota(), Toast.LENGTH_SHORT).show();
//                    newNome = titulo + "_copia";
//                    Toast.makeText(context, "new nome: "+ newNome, Toast.LENGTH_SHORT).show();
//
//                }else {
//                    newNome = titulo;
//
//                }
//        }

    }

    public String addDateTime(){
        String date;
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        date = format.format(today);
        return date;
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
