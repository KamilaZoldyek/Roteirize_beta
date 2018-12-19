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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class CenaListaFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private List<Cena> lcen;
    private List<Cena> cenas;
    private CenaRecyclerAdapter cenaAdapter;
    Context context;
    private TextView suchempty;
    private FloatingActionButton fab_cena, fab_escrito, fab_excluir_capitulo, fab_renomear_capitulo, fab_nova_cena;
    private CenaNovoFragment nov_p;
    private String tituloStr, capituloStr, escritoStr;
    private File dirCapitulo, dirCena;
    private LocalData localData;
    private CapituloListaFragment capituloListaFragment;
    private CenaNovoFragment cenaNovoFragment;
    private List<Capitulo> list;
    private String acaoBundle, cenarioBundle, personagemBundle;
    private ConstraintLayout layoutOpc;

    boolean isOpen = false;
    Animation fabopen, fabclose, rotateforward, rotatebackward;
    LinearLayout novacenalay, editarlay, excluirlay, setEscritolay;




    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        tituloStr = getArguments().getString("TITULO");
        capituloStr = getArguments().getString("CAPITULO");
//        escritoStr =  getArguments().getString("ESCRITO");

        capituloListaFragment = new CapituloListaFragment();
        cenaNovoFragment = new CenaNovoFragment();

        list = new ArrayList<>();

        fabopen = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_close);
        rotatebackward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_backward);
        rotateforward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_forward);




        dirCapitulo = new File(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS +
                "/" + tituloStr + Constantes.CAPITULOS+ "/"+ capituloStr);

        dirCena = new File(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS +
                "/" + tituloStr + Constantes.CAPITULOS+ "/"+ capituloStr + "/" + Constantes.CENAS);


        if(!dirCapitulo.exists()){
            dirCapitulo.mkdirs();
        }

    }

    @Nullable
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.cenas_lista_fragment,
                container, false);
        context = container.getContext();
        suchempty = v.findViewById(R.id.suchemptyCenas);
        suchempty.setVisibility(View.GONE);
        getActivity().setTitle(capituloStr + " - Cenas");

        localData = new LocalData(context);

        fab_cena = v.findViewById(R.id.fab_cena_opc);

        layoutOpc = v.findViewById(R.id.layoutOpc);
        novacenalay = v.findViewById(R.id.capitulo_nova_cena_layout);
        editarlay = v.findViewById(R.id.capitulo_editar_layout);
        excluirlay= v.findViewById(R.id.capitulo_excluir_layout);
        setEscritolay = v.findViewById(R.id.capitulo_escrito_layout);

        layoutOpc.setVisibility(View.GONE);

        novacenalay.setVisibility(View.GONE);
        setEscritolay.setVisibility(View.GONE);
        editarlay.setVisibility(View.GONE);
        excluirlay.setVisibility(View.GONE);

        fab_escrito = v.findViewById(R.id.fab_escrito);
        fab_excluir_capitulo = v.findViewById(R.id.fab_excluir_capitulo);
        fab_renomear_capitulo = v.findViewById(R.id.fab_renomear_capitulo);
        fab_nova_cena = v.findViewById(R.id.fab_nova_cena);


        nov_p = new CenaNovoFragment();
        lcen = new ArrayList<>();
        lcen = carregarCenas();
        list = carregarCapitulos();



        myRecyclerView = v.findViewById(R.id.cenas_lista);
        cenaAdapter = new CenaRecyclerAdapter(getContext(),
                lcen, communication);
        myRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        myRecyclerView.setAdapter(cenaAdapter);

        fab_cena.setVisibility(View.VISIBLE);
        fab_cena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOpen == false){

                    layoutOpc.setVisibility(View.VISIBLE);

                    novacenalay.setVisibility(View.VISIBLE);
                    setEscritolay.setVisibility(View.VISIBLE);
                    editarlay.setVisibility(View.VISIBLE);
                    excluirlay.setVisibility(View.VISIBLE);
                    isOpen = true;

                }else{

                    layoutOpc.setVisibility(View.GONE);
                    novacenalay.setVisibility(View.GONE);
                    setEscritolay.setVisibility(View.GONE);
                    editarlay.setVisibility(View.GONE);
                    excluirlay.setVisibility(View.GONE);
                    isOpen = false;
                }

            }
        });

        novacenalay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("CAPITULO", capituloStr);
                cenaNovoFragment.setArguments(bundle);

                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cenas_lista_container, cenaNovoFragment)
                        .commit();
                fab_cena.setVisibility(View.GONE);
            }
        });

        excluirlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.excluir_dialog, null);


                mBuilder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(delete(dirCapitulo)){
                            Toast.makeText(context, "Capítulo excluído: " + dirCapitulo.getName(), Toast.LENGTH_SHORT).show();
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", tituloStr);
                        capituloListaFragment.setArguments(bundle);

                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.cenas_lista_container, capituloListaFragment) //TODO volta pra lista de projetos
                                .commit();
                        fab_cena.setVisibility(View.GONE);

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

        editarlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.capitulo_novo_dialog, null);
                final EditText capituloNome = mView
                        .findViewById(R.id.capeditText);
                capituloNome.setText(capituloStr);

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nome = capituloNome.getText().toString();
                        int i=0;
                        boolean b = false;

                        if (list.size() == 0){
                            Log.d("KAMIS", nome +" lista vazia ");

                        }

                        if (!nome.equalsIgnoreCase("")) {
                            while (i < list.size() - 1) {
                                if (list.get(i).getTitulo_capitulo().equalsIgnoreCase(nome)) {
                                    b = true;
                                }
                                ++i;
                            }
                        }

                        if(!nome.equalsIgnoreCase("")) {
                            if (!b) {

                                renomearDir(nome);
                                CapituloListaFragment fragB = new CapituloListaFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("TITULO", tituloStr);
                                fragB.setArguments(bundle);
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.capitulos_lista_container, fragB)

                                        .commit();

                                dialog.dismiss();
                            }else{

                                Toast.makeText(context, "Título existente! Tente outro :)",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, "Título não pode ser vazio!",
                                    Toast.LENGTH_SHORT).show();
                        }




                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        setEscritolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

                mBuilder.setTitle("Capítulo concluído");
                mBuilder.setMessage("Já terminou de escrever esse capítulo?");

                mBuilder.setPositiveButton("Sim!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localData.setEscrito(capituloStr, "1");
                        dialog.dismiss();

                        CapituloListaFragment capituloListaFragment = new CapituloListaFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", tituloStr);
                        capituloListaFragment.setArguments(bundle);

                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.capitulos_lista_container, capituloListaFragment)
                                .commit();
                        fab_cena.setVisibility(View.GONE);

                    }
                });

                mBuilder.setNegativeButton("Ainda não...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localData.setEscrito(capituloStr, "0");

                        CapituloListaFragment capituloListaFragment = new CapituloListaFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", tituloStr);
                        capituloListaFragment.setArguments(bundle);

                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.capitulos_lista_container, capituloListaFragment)
                                .commit();
                        fab_cena.setVisibility(View.GONE);

                    }
                });

                mBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });



//        =========================================================

        fab_nova_cena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("TITULO", tituloStr);
                bundle.putString("CAPITULO", capituloStr);
                cenaNovoFragment.setArguments(bundle);

                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cenas_lista_container, cenaNovoFragment)
                        .commit();
                fab_cena.setVisibility(View.GONE);
            }
        });

        fab_excluir_capitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.excluir_dialog, null);


                mBuilder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(delete(dirCapitulo)){
                            Toast.makeText(context, "Capítulo excluído: " + dirCapitulo.getName(), Toast.LENGTH_SHORT).show();
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", tituloStr);
                        capituloListaFragment.setArguments(bundle);

                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.cenas_lista_container, capituloListaFragment) //TODO volta pra lista de projetos
                                .commit();
                        fab_cena.setVisibility(View.GONE);

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

        fab_renomear_capitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater()
                        .inflate(R.layout.capitulo_novo_dialog, null);
                final EditText capituloNome = mView
                        .findViewById(R.id.capeditText);
                capituloNome.setText(capituloStr);

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nome = capituloNome.getText().toString();
                        int i=0;
                        boolean b = false;

                        if (list.size() == 0){
                            Log.d("KAMIS", nome +" lista vazia ");

                        }

                        if (!nome.equalsIgnoreCase("")) {
                            while (i < list.size() - 1) {
                                if (list.get(i).getTitulo_capitulo().equalsIgnoreCase(nome)) {
                                    b = true;
                                }
                                ++i;
                            }
                        }

                        if(!nome.equalsIgnoreCase("")) {
                            if (!b) {

                                renomearDir(nome);
                                CapituloListaFragment fragB = new CapituloListaFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("TITULO", tituloStr);
                                fragB.setArguments(bundle);
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.capitulos_lista_container, fragB)

                                        .commit();

                                dialog.dismiss();
                            }else{

                                Toast.makeText(context, "Título existente! Tente outro :)",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, "Título não pode ser vazio!",
                                    Toast.LENGTH_SHORT).show();
                        }




                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        fab_escrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

                mBuilder.setTitle("Capítulo concluído");
                mBuilder.setMessage("Já terminou de escrever esse capítulo?");

                mBuilder.setPositiveButton("Sim!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localData.setEscrito(capituloStr, "1");
                        dialog.dismiss();

                        CapituloListaFragment capituloListaFragment = new CapituloListaFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", tituloStr);
                        capituloListaFragment.setArguments(bundle);

                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.capitulos_lista_container, capituloListaFragment)
                                .commit();
                        fab_cena.setVisibility(View.GONE);

                    }
                });

                mBuilder.setNegativeButton("Ainda não...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localData.setEscrito(capituloStr, "0");

                        CapituloListaFragment capituloListaFragment = new CapituloListaFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", tituloStr);
                        capituloListaFragment.setArguments(bundle);

                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.capitulos_lista_container, capituloListaFragment)
                                .commit();
                        fab_cena.setVisibility(View.GONE);

                    }
                });

                mBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });



    return v;

    }

    public boolean delete(File dir) {

        if (dir.exists()) {

            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    delete(files[i]);
                } else {
                    files[i].delete();
                }
            }

        }
        return (dir.delete());


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
    private boolean isExternalStorageWritable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State", "Escreve sim");
            return true;
        }else{
            return false;
        }
    }
    private List<Cena> carregarCenas() {

        File[] files = dirCena.listFiles();
        String theFile;
        String content = "";

        if (dirCena.exists()) {
            if (dirCena.length() > 0) {
                for (int f = 0; f < files.length; f++) {
                    theFile = files[f].getName();
                    File file = new File(dirCena, theFile);
                    if(file.exists()){
                        StringBuilder sb = new StringBuilder();
                        try {
                            BufferedReader br = new BufferedReader
                                    (new FileReader(file));
                            String line;
                            while ((line=br.readLine()) != null) {
                                sb.append(line);
                            }
                            content = sb.toString();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "file not found",
                                    Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "ioexception",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                   formatDesc(theFile, content, file.getAbsolutePath());
                }
            } else if (dirCena.length()==0){
                suchempty.setVisibility(View.VISIBLE);
            }

            if(lcen.isEmpty()) {
                suchempty.setVisibility(View.VISIBLE);
            }
        }
        return lcen;
    }








    @RequiresApi(api = Build.VERSION_CODES.M)
    public void editarCapitulo(String titulo){

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + Constantes.CAPITULOS + "/" + titulo);

            File f2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr + Constantes.CAPITULOS + "/" + titulo + Constantes.CENAS);

            if(!f.exists()){
                f.mkdirs();
                f2.mkdirs();
            }
        }
    }

    public void renomearDir(String novoNome){
        File antigo = dirCapitulo;
        File novo = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + Constantes.DIRETORIO_PROJETOS + "/" + tituloStr
                + Constantes.CAPITULOS + "/" + novoNome);
        try{
            String res = "false";
            boolean bool = antigo.renameTo(novo);
            if (bool){
                res="true";
            }
            Log.d("KAMIS","PROJ_RENAME "+res);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("KAMIS","PROJDEURUIM "+ "deu exception");
        }


    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void respostaNotas(int position, String titulo, String corpo, String id) {

        }

        @Override
        public void respostaProjetos(int position, String titulo) {

        }

        @Override
        public void respostaCapitulos(int position, String nome_capitulo, String titulo_projeto, String escrito) {

        }

        @Override
        public void respostaPersonagens(int position, String titulo, String nome, String apelido, String sexo, String idade, String data, String descrição, String path) {

        }

        @Override
        public void respostaCenario(int position, String titulo, String nome, String descricao, String historia, String importancia, String path) {

        }

        @Override
        public void respostaCena(int position,

                                 String nome_cena,
                                 String acao,
                                 String local,
                                 String personagens,
                                 String titulo_projeto,
                                 String nome_capitulo,
                                 String path) {
            CenaItemOpcoesFragment cenaItemOpcoesFragment =
                    new CenaItemOpcoesFragment();

            Bundle bundle = new Bundle();


            bundle.putString("NOMEC", nome_cena);
            bundle.putString("ACAO", acao);
            bundle.putString("LOCAL", local);
            bundle.putString("PERSONAGENS", personagens);
            bundle.putString("PATH",path );
            bundle.putString("TITULO", tituloStr);
            bundle.putString("CAPITULO", capituloStr);
            cenaItemOpcoesFragment.setArguments(bundle);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.cenas_lista_container, cenaItemOpcoesFragment)

                    .commit();
            fab_cena.setVisibility(View.GONE);


        }
    };

    @Override
    public void onResume() {
        super.onResume();
        cenaAdapter.notifyDataSetChanged();


//        CenaListaFragment fragB = new CenaListaFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putString("TITULO", tituloStr);
//        bundle.putString("CAPITULO", capituloStr);
//        fragB.setArguments(bundle);
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.cenas_lista_container, fragB)
//                .commit();

    }

    private List<Capitulo> carregarCapitulos() {

        List<Capitulo> fileList = new ArrayList<>();

        File root = dirCapitulo;


        if (dirCapitulo.exists()) {
            if (dirCapitulo.length() > 0) {
                File[] files = root.listFiles();
                fileList.clear();
                for (File file : files) {
                    fileList.add(new Capitulo(file.getName(),
                            tituloStr, file.getAbsolutePath()));
                    localData.getEscrito(file.getName());
                }
            }
        }



        return fileList;
    }

    public void formatDesc(String nomeCena, String content, String path){

        StringTokenizer tokens = new StringTokenizer(content, "«");
        String first = tokens.nextToken();
        String acao = tokens.nextToken();
        String third = tokens.nextToken();
        String cenario = tokens.nextToken();
        String fifth = tokens.nextToken();
        String pers = tokens.nextToken();

        lcen.add(new Cena(nomeCena, acao, cenario, pers, path, tituloStr, capituloStr));
    }





}
