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
import java.util.StringTokenizer;

public class NotaEditarFragment  extends Fragment {

    View v;
    FloatingActionButton fabsave;
    private Context context;
    TextView nome_arq;
    EditText corpo_texto;
    String tituloStr, corpoStr, idStr;
    int position;
    List<Nota> lnotas;
    NotasListaFragment frag;
    NotasRecyclerAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloStr = getArguments().getString("TITULO");
        corpoStr = getArguments().getString("CORPO");
        idStr = getArguments().getString("ID");
        position = getArguments().getInt("POSITION");
        lnotas = new ArrayList<>();
        frag = new NotasListaFragment();
        getActivity().setTitle("Editar");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = container.getContext();
        v = inflater.inflate(R.layout.notas_editar_fragment, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fabsave = v.findViewById(R.id.fab_salvar_nota_editada);

        nome_arq = v.findViewById(R.id.title_note_edit);
        corpo_texto = v.findViewById(R.id.description_note_edit);

        String tituloF = formatTitle(tituloStr);

        nome_arq.setText(tituloF);
        corpo_texto.setText(corpoStr);

        fabsave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                escreveArquivo(nome_arq.getText().toString(), corpo_texto.getText().toString());

                if(deleteFile(tituloStr)){
                    Toast.makeText(context, "Atualizado", Toast.LENGTH_SHORT).show();
                }
                    ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.notas_editar_container, frag)
                        .commit();

                fabsave.setVisibility(View.GONE);
            }
        });


        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void escreveArquivo(String tituloNota, String corpoNota){

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + Constantes.DIRETORIO_NOTAS);

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

    public String formatTitle(String title){

        StringTokenizer tokens = new StringTokenizer(title, "«");
        String first = tokens.nextToken();
        return first;
    }

    public boolean deleteFile(String theFile){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + Constantes.DIRETORIO_NOTAS, theFile);
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


    public Nota pesquisar(List<Nota> list, int positionTarget){
        Nota nota = new Nota("","","");

        if (list.size() != 0){
            for (int i = 0; i<list.size(); i++){
                if (list.get(i).getId_nota() == list.get(positionTarget).getId_nota()){
                    nota = list.get(i);
                }
            }
        }

        return nota;
    }



}
