package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ProjetosListaItemOpcoesFragment extends Fragment {

    boolean isOpen = false;
    Animation fabopen, fabclose, rotateforward, rotatebackward;
    FloatingActionButton opcoesfab,fabeditar,fabnovacena,fabnovopers,fabnovocenario, fabexcluir;

    LinearLayout novacenalay, novoperslay, novocenariolay, editarlay, excluirlay, resumoLay;
    ConstraintLayout metasLay;

    TextView tituloprojTV, descricaoprojTV, meta, progresso, progressoP;
    String tituloStr, corpoStr, idStr, tituloTXT;
    int position;
    View v;
    private Context context;
    Projeto itemProj;
    List<Projeto> lproj;
    List<String> listaDir;
    String descricaoBundle;

    ProjetosListaFragment listaProjetosFrag;

    ConstraintLayout defMeta, addProgress;



    ProgressBar progressBar;

    PersonagemListaFragment fragPers;
    PersonagemNovoFragment fragNov;
    CenarioListaFragment fragListaCenario;
    ResumoFragment resumoFragment;



    public ProjetosListaItemOpcoesFragment() {
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tituloStr = getArguments().getString("TITULO");
        position = getArguments().getInt("POSITION");


        lproj = new ArrayList<>();
        listaDir = new ArrayList<>();
        listaDir = carregarDiretorio();
        descricaoBundle = "";
        tituloTXT = "";

        fragPers = new PersonagemListaFragment();
        fragNov = new PersonagemNovoFragment();
        fragListaCenario = new CenarioListaFragment();
        resumoFragment = new ResumoFragment();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        isOpen = false;


        v = inflater.inflate(R.layout.projetos_lista_opcoes, container, false);
        context = container.getContext();
        getActivity().setTitle("Projeto");
        listaProjetosFrag = new ProjetosListaFragment();

        opcoesfab = v.findViewById(R.id.fab_opcoes_do_menu);
        fabeditar = v.findViewById(R.id.fab_editar);
        fabnovacena = v.findViewById(R.id.fab_nova_cena);
        fabnovopers = v.findViewById(R.id.fab_novo_personagem);
        fabnovocenario = v.findViewById(R.id.fab_novo_cenario);
        fabexcluir = v.findViewById(R.id.fab_excluir_proj);

        novacenalay = v.findViewById(R.id.nova_cena_layout);
        novocenariolay = v.findViewById(R.id.capitulo_nova_cena_layout);
        novoperslay = v.findViewById(R.id.capitulo_escrito_layout);
        editarlay = v.findViewById(R.id.capitulo_editar_layout);
        excluirlay = v.findViewById(R.id.proj_excluir_layout);
        metasLay = v.findViewById(R.id.meta_layout);
        defMeta = v.findViewById(R.id.definir_meta_layout);
        addProgress = v.findViewById(R.id.add_progresso_layout);
        resumoLay = v.findViewById(R.id.resumo_linear_layout);




//        metasLay.setVisibility(View.GONE);
//        defMeta.setVisibility(View.GONE);
//        addProgress.setVisibility(View.GONE);

        novacenalay.setVisibility(View.GONE);
        novoperslay.setVisibility(View.GONE);
        novocenariolay.setVisibility(View.GONE);
        editarlay.setVisibility(View.GONE);
        excluirlay.setVisibility(View.GONE);

        tituloprojTV = v.findViewById(R.id.title_project);
        descricaoprojTV = v.findViewById(R.id.description_project);
        progressBar = v.findViewById(R.id.progressBar);
        progresso = v.findViewById(R.id.progresso);
        progressoP = v.findViewById(R.id.progressoPercent);
        meta = v.findViewById(R.id.meta_numbers2);

        fabopen = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_close);
        rotatebackward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_backward);
        rotateforward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_forward);

        int met = getMeta(tituloStr+"Meta");
        String pro = String.valueOf(getProgress(tituloStr+"Progresso"));
        String perc = String.valueOf(getProgressPercent(tituloStr+"Percent"));

        descricaoprojTV.setMovementMethod(new ScrollingMovementMethod());

        meta.setText(String.valueOf(met) + " " + getPalavrasSpinner(tituloStr+"Spinner"));
        progresso.setText(pro + " "+ getPalavrasSpinner(tituloStr+"Spinner") + " de " +String.valueOf(getMeta(tituloStr+"Meta")));
        progressoP.setText(perc + "%");
        progressBar.setProgress(getProgressPercent(tituloStr+"Percent"));

//        opcoesfab.setVisibility(View.VISIBLE);




                lerArquivo();


         resumoLay.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 int meta = getMeta(tituloStr+"Meta");
                 String palavras = getPalavrasSpinner(tituloStr+"Spinner");
                 Bundle bundle = new Bundle();
                 bundle.putString("TITULO", tituloStr );
                 bundle.putString("DESCRICAO", descricaoBundle);
                 bundle.putInt("META", meta);
                 bundle.putString("SPINNER", palavras);

                 resumoFragment.setArguments(bundle);

                 ((FragmentActivity)context).getSupportFragmentManager()
                         .beginTransaction()
                         .replace(R.id.proj_lista_opcoes_container, resumoFragment)
//                         .addToBackStack("PROJSUMM")
                         .commit();

                 opcoesfab.setVisibility(View.GONE);
             }
         });

        defMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.projeto_definir_meta_dialog, null);
                final EditText metaN = mView.findViewById(R.id.metaeditText);
                final Spinner palavras = mView.findViewById(R.id.spinner_palavras);
//                Button buttonOk = mView.findViewById(R.id.button);

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String valor = metaN.getText().toString();
                        int valorInteiro = Integer.parseInt(valor);
                        String palavrasCaracteres = " " + String.valueOf(palavras.getSelectedItem());
                        setPalavrasSpinner(tituloStr+"Spinner", palavrasCaracteres);

                        progresso.setText("0 "+palavrasCaracteres+" de " + valor);
                        progressBar.setMax(100);
                        progressBar.setProgress(0);
                        progressoP.setText("0%");
                        meta.setText(valor + " "+palavrasCaracteres);


                        setMeta(tituloStr+"Meta", valorInteiro);
                        dialog.dismiss();
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

        addProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getMeta(tituloStr+"Meta")==0){
                    Toast.makeText(getContext(),"Defina uma meta primeiro!", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    View mView = getLayoutInflater().inflate(R.layout.projeto_progresso_dialog, null);
                    final EditText progress = mView.findViewById(R.id.progresso_edit_text);

                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String valor = progress.getText().toString();
                            int inserido = Integer.parseInt(valor);
                            int meta = getMeta(tituloStr+"Meta");
                            int progressoExistente = getProgress(tituloStr+"Progresso");
                            int progressoTotal = progressoExistente+inserido;
                            int progressoPercent = (inserido*100)/meta;

                            setProgress(tituloStr+"Progresso", progressoTotal);
                            setProgressPercent(tituloStr+"Percent", progressoPercent);


                            progressBar.setProgress(progressoPercent);

                            progressoP.setText(progressoPercent+"%");
                            progresso.setText(progressoTotal + " " +getPalavrasSpinner(tituloStr+"Spinner"));


                            dialog.dismiss();
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




            }


        });

        opcoesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen == false){

                    novacenalay.setVisibility(View.VISIBLE);
                    novoperslay.setVisibility(View.VISIBLE);
                    novocenariolay.setVisibility(View.VISIBLE);
                    editarlay.setVisibility(View.VISIBLE);
                    excluirlay.setVisibility(View.VISIBLE);
                    isOpen = true;

                }else{
                    novacenalay.setVisibility(View.GONE);
                    novoperslay.setVisibility(View.GONE);
                    novocenariolay.setVisibility(View.GONE);
                    editarlay.setVisibility(View.GONE);
                    excluirlay.setVisibility(View.GONE);
                    isOpen = false;
                }


            }
        });


        //TODO ARRUMA AQUI OS ONCLICK
        editarlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                opcoesfab.setVisibility(View.VISIBLE);
//                opcoesfab.setVisibility(View.GONE);

                ProjetoEditarFragment projetoEditarFragment = new ProjetoEditarFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr );
                bundle.putString("CORPO", descricaoBundle);
                bundle.putInt("POSITION", position);
                bundle.putString("TITULOTXT", tituloTXT);

                projetoEditarFragment.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.proj_lista_opcoes_container, projetoEditarFragment)
                        .addToBackStack("PROJSUMM")
                        .commit();




            }
        });

        novacenalay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                CenaListaFragment fragB = new CenaListaFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("TITULO", tituloStr);
//                fragB.setArguments(bundle);
//
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.proj_lista_opcoes_container, fragB)
//                        .addToBackStack("ProjOpc")
//                        .commit();

//                opcoesfab.setVisibility(View.GONE);

                CapituloListaFragment fragB = new CapituloListaFragment();

                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                fragB.setArguments(bundle);

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.proj_lista_opcoes_container, fragB)
                        .addToBackStack("PROJSUMM")
                        .commit();



            }
        });

        novocenariolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                opcoesfab.setVisibility(View.VISIBLE);
//                opcoesfab.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr );

                fragListaCenario.setArguments(bundle);

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.proj_lista_opcoes_container, fragListaCenario)
                        .addToBackStack("PROJSUMM")
                        .commit();



            }
        });

        novoperslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                opcoesfab.setVisibility(View.VISIBLE);
//                opcoesfab.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putInt("POSITION", position);
                fragPers.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.proj_lista_opcoes_container, fragPers)
                        .addToBackStack("PROJSUMM")
                        .commit();


            }
        });
        excluirlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder =
                        new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.excluir_dialog, null);

                mBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        opcoesfab.setVisibility(View.VISIBLE);
//                        opcoesfab.setVisibility(View.GONE);
                        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                                + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr);

                        if(delete(dir)){
                            Toast.makeText(context, "Projeto excluído: " + dir.getName(), Toast.LENGTH_SHORT).show();

                        }
                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.proj_lista_opcoes_container, listaProjetosFrag)
                                .commit();


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

//        ========================================

        fabeditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                opcoesfab.setVisibility(View.VISIBLE);
//                opcoesfab.setVisibility(View.GONE);

                ProjetoEditarFragment projetoEditarFragment = new ProjetoEditarFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr );
                bundle.putString("CORPO", descricaoBundle);
                bundle.putInt("POSITION", position);
                bundle.putString("TITULOTXT", tituloTXT);

                projetoEditarFragment.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.proj_lista_opcoes_container, projetoEditarFragment)
                        .addToBackStack("PROJSUMM")
                        .commit();




            }
        });

        fabnovacena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                CenaListaFragment fragB = new CenaListaFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("TITULO", tituloStr);
//                fragB.setArguments(bundle);
//
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.proj_lista_opcoes_container, fragB)
//                        .addToBackStack("ProjOpc")
//                        .commit();

//                opcoesfab.setVisibility(View.GONE);

                CapituloListaFragment fragB = new CapituloListaFragment();

                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                fragB.setArguments(bundle);

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.proj_lista_opcoes_container, fragB)
                        .addToBackStack("PROJSUMM")
                        .commit();



            }
        });

        fabnovocenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                opcoesfab.setVisibility(View.VISIBLE);
//                opcoesfab.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr );

                fragListaCenario.setArguments(bundle);

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.proj_lista_opcoes_container, fragListaCenario)
                        .addToBackStack("PROJSUMM")
                        .commit();



            }
        });

        fabnovopers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                opcoesfab.setVisibility(View.VISIBLE);
//                opcoesfab.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putInt("POSITION", position);
                fragPers.setArguments(bundle);

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.proj_lista_opcoes_container, fragPers)
                        .addToBackStack("PROJSUMM")
                        .commit();


            }
        });
        fabexcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder =
                        new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.excluir_dialog, null);

                mBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        opcoesfab.setVisibility(View.VISIBLE);
//                        opcoesfab.setVisibility(View.GONE);
                        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                                + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr);

                        if(delete(dir)){
                            Toast.makeText(context, "Projeto excluído: " + dir.getName(), Toast.LENGTH_SHORT).show();

                        }
                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.proj_lista_opcoes_container, listaProjetosFrag)
                                .commit();


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
    private void lerArquivo() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        if (isExternalStorageReadable() && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr);
            File[] files = dir.listFiles();
            String theFile;
            String content = "";

            if (dir.exists()) {
                if (dir.length() > 0) {
                    for (int f = 0; f < files.length; f++) {
                        theFile = files[f].getName();
                        if(theFile.contains(".txt")){

                            File file = new File(dir, "/" + theFile);
                            tituloTXT = file.getName();
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
                            tituloprojTV.setText(tituloStr);
                            descricaoprojTV.setText(content);
                            descricaoBundle = content;
                        }
                    }
                } else if (dir.length() == 0){
                    Toast.makeText(context, "wow such empty", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(context, "diretorio nem existe", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private List<String> carregarDiretorio() {


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

    public Projeto pesquisarProj(List<Projeto> list, int positionTarget){
        Projeto proj = new Projeto("","","");

        if (list.size() != 0){
            for (int i = 0; i<list.size(); i++){
                if (list.get(i).getId_projeto() == list.get(positionTarget).getId_projeto()){
                    proj = list.get(i);
                }
            }
        }

        return proj;
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

    public boolean delete(File dir){

            if (dir.exists()){
                Log.d("PROJETO", dir.getAbsolutePath());
                File[] files = dir.listFiles();
                for (int i =0; i< files.length; i++){
                    if (files[i].isDirectory()){
                        delete(files[i]);
                    }else{
                        files[i].delete();
                    }
                }

            }
            return (dir.delete());

//        boolean deleted = file.delete();
//        return deleted;
    }


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//
//    }

//TODO se voce tiver tempo, mexe nisso
    private void animateFab(){
        if (isOpen == false){
            opcoesfab.startAnimation(rotateforward);
            fabeditar.startAnimation(fabclose);
            fabnovacena.startAnimation(fabclose);
            fabnovocenario.startAnimation(fabclose);
            fabnovopers.startAnimation(fabclose);
            fabeditar.setClickable(false);
            fabnovacena.setClickable(false);
            fabnovocenario.setClickable(false);
            fabnovopers.setClickable(false);

        }else{
            opcoesfab.startAnimation(rotatebackward);
            fabeditar.startAnimation(fabopen);
            fabnovacena.startAnimation(fabopen);
            fabnovocenario.startAnimation(fabopen);
            fabnovopers.startAnimation(fabopen);
            fabeditar.setClickable(true);
            fabnovacena.setClickable(true);
            fabnovocenario.setClickable(true);
            fabnovopers.setClickable(true);
            isOpen = true;
        }

    }

    public String splitMaFile(String file){

            StringTokenizer tokens = new StringTokenizer(file, ".");
            String first = tokens.nextToken();
            String second = tokens.nextToken();
            return second;

    }


    public int getMeta(String key){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        return prefs.getInt(key, 0);
    }

    public void setMeta(String key, int newValue){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
        editor.putInt(key, newValue);
        editor.commit();
    }

    public int getProgress(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getInt(key, 0);
    }

    public void setProgress(String key, int newValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
        editor.putInt(key, newValue);
        editor.commit();
    }

    public int getProgressPercent(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getInt(key, 0);
    }

    public void setProgressPercent(String key, int newValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
        editor.putInt(key, newValue);
        editor.commit();
    }

    public String getPalavrasSpinner(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getString(key, "palavras");
    }

    public void setPalavrasSpinner(String key, String newValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
        editor.putString(key, newValue);
        editor.commit();
    }



    private boolean isExternalStorageWritable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Opa, escreve sim");
            return true;
        }else{
            return false;
        }
    }







}
