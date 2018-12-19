package com.example.kamilazoldyek.roteirize;
import android.Manifest;
import android.app.ActionBar;
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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class NotaItemOpcoesFragment extends Fragment{

    boolean isOpen = false;
    Animation fabopen, fabclose, rotateforward, rotatebackward;
    FloatingActionButton fabopcoes, fab_editar_note, fab_excluir_note;
    LinearLayout editarLay, excluirLay;
    TextView titulonotaTV, corponotaTV;
    String tituloStr, corpoStr, idStr;
    int position;
    View v;
    private Context context;
    Nota itemNota;
    List<Nota> lnotas;
    NotaEditarFragment fragB;
    NotasListaFragment frag;

    NotasRecyclerAdapter adapter;


    public NotaItemOpcoesFragment() {
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloStr = getArguments().getString("TITULO");
        corpoStr = getArguments().getString("CORPO");
        idStr = getArguments().getString("ID");
        position = getArguments().getInt("POSITION");
        lnotas = new ArrayList<>();

        carregarNotas();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        isOpen = false;

        v = inflater.inflate(R.layout.notas_lista_opcoes_fragment, container, false);
        context = container.getContext();
        frag = new NotasListaFragment();
        getActivity().setTitle("Anotações");


        titulonotaTV = v.findViewById(R.id.title_note);
        corponotaTV = v.findViewById(R.id.description_note);
        corponotaTV.setMovementMethod(new ScrollingMovementMethod());


        String tituloF = formatTitle(tituloStr);

        titulonotaTV.setText(tituloF);
        corponotaTV.setText(corpoStr);


        fabopcoes = v.findViewById(R.id.fab_opcoes_do_menu_note);
        fab_editar_note = v.findViewById(R.id.fab_editar_note);
        fab_excluir_note = v.findViewById(R.id.fab_excluir_note);


        editarLay = v.findViewById(R.id.note_editar_layout);
        excluirLay = v.findViewById(R.id.note_excluir_layout);

        editarLay.setVisibility(View.GONE);
        excluirLay.setVisibility(View.GONE);

        fabopen = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_close);
        rotatebackward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_backward);
        rotateforward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_forward);

        fabopcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen == false){

                    excluirLay.setVisibility(View.VISIBLE);
                    editarLay.setVisibility(View.VISIBLE);
                    isOpen = true;

                }else{
                    excluirLay.setVisibility(View.GONE);
                    editarLay.setVisibility(View.GONE);
                    isOpen = false;
                }
            }
        });


        editarLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Nota note = pesquisar(lnotas, position);

                fragB = new NotaEditarFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", note.getTitulo_nota());
                bundle.putString("CORPO", note.getCorpo_nota());
                bundle.putString("ID", note.getId_nota());
                bundle.putInt("POSITION", position);
                fragB.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.notas_item_opcoes_container, fragB)
                        .commit();
                fabopcoes.setVisibility(View.GONE);


            }
        });

        excluirLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder =
                        new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.excluir_dialog, null);

                mBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Nota note = pesquisar(lnotas, position);

                        if(deleteFile(note.getTitulo_nota())){
                            Toast.makeText(context, "Nota excluida: " + formatTitle(note.getTitulo_nota()), Toast.LENGTH_SHORT).show();

                            ((FragmentActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.notas_item_opcoes_container, frag)
                                    .commit();
                            fabopcoes.setVisibility(View.GONE);

                        }else {
                            Toast.makeText(context, "deu algum biziu" + note.getTitulo_nota(), Toast.LENGTH_SHORT).show();
                            fabopcoes.setVisibility(View.GONE);
                        }

                    }

                });
                mBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();



            }
        });


//        =================================================================


        fab_editar_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Nota note = pesquisar(lnotas, position);

                fragB = new NotaEditarFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", note.getTitulo_nota());
                bundle.putString("CORPO", note.getCorpo_nota());
                bundle.putString("ID", note.getId_nota());
                bundle.putInt("POSITION", position);
                fragB.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.notas_item_opcoes_container, fragB)
                        .commit();
                fabopcoes.setVisibility(View.GONE);


            }
        });

        fab_excluir_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder =
                        new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.excluir_dialog, null);

                mBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Nota note = pesquisar(lnotas, position);

                        if(deleteFile(note.getTitulo_nota())){
                            Toast.makeText(context, "Nota excluida: " + formatTitle(note.getTitulo_nota()), Toast.LENGTH_SHORT).show();

                            ((FragmentActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.notas_item_opcoes_container, frag)
                                    .commit();
                            fabopcoes.setVisibility(View.GONE);

                        }else {
                            Toast.makeText(context, "deu algum biziu" + note.getTitulo_nota(), Toast.LENGTH_SHORT).show();
                            fabopcoes.setVisibility(View.GONE);
                        }

                    }

                });
                mBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();



            }
        });



        return v;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void carregarNotas() {

        if (isExternalStorageReadable() && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_NOTAS);
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
                                    sb.append("\n");
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
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

    public String formatTitle(String title){

        StringTokenizer tokens = new StringTokenizer(title, "«");
        String first = tokens.nextToken();
        return first;


//        StringBuilder sb = new StringBuilder(title);
//        int start = title.length()-4;
//        int end = title.length()-1;
//
//        sb.delete(start, end);
//        sb.deleteCharAt(start);
//
//        String result = sb.toString();
//        return result;

    }

    public boolean deleteFile(String theFile){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + Constantes.DIRETORIO_NOTAS, theFile);
        boolean deleted = file.delete();
        return deleted;
    }



}
