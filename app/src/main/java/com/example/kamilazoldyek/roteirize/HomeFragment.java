package com.example.kamilazoldyek.roteirize;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    boolean isOpen = false;
    View v;
    Context context;
    Animation fabopen, fabclose, rotateforward, rotatebackward;
    FloatingActionButton fabNovoP, fabNovaN, fabopcoes;
    LinearLayout novaNotaLay, novoProjetoLay;

    NotaNovaFragment fragNota;
    ProjetoNovoFragment fragProj;
    HomeFragment homeFragment;




    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeFragment = new HomeFragment();
//        ((FragmentActivity)getContext()).getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.home_container, homeFragment, "HOME")
//                .commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.home_fragment, container, false);

        criarDiretorios();



        isOpen = false;
        context = container.getContext();
        fragNota = new NotaNovaFragment();
        fragProj = new ProjetoNovoFragment();

        fabopcoes = v.findViewById(R.id.fab_opcoes_do_menu_home);
        fabNovaN = v.findViewById(R.id.fab_nova_nota_home);
        fabNovoP = v.findViewById(R.id.fab_novo_proj_home);

        novaNotaLay = v.findViewById(R.id.nova_nota_fab_layout);
        novoProjetoLay = v.findViewById(R.id.novo_projeto_fab_layout);

        novaNotaLay.setVisibility(View.GONE);
        novoProjetoLay.setVisibility(View.GONE);



        fabopen = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fab_close);
        rotatebackward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_backward);
        rotateforward = AnimationUtils.loadAnimation(this.getActivity(), R.anim.rotate_forward);

        fabopcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isOpen == false){

                    novaNotaLay.setVisibility(View.VISIBLE);
                    novoProjetoLay.setVisibility(View.VISIBLE);
                    isOpen = true;

                }else{
                    novaNotaLay.setVisibility(View.GONE);
                    novoProjetoLay.setVisibility(View.GONE);
                    isOpen = false;
                }
            }
        });


        novaNotaLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("HOME")
                        .replace(R.id.home_container, fragNota)
                        .commit();
//                fabopcoes.setVisibility(View.GONE);
            }
        });

        novoProjetoLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("HOME")
                        .replace(R.id.home_container, fragProj)
                        .commit();
//                fabopcoes.setVisibility(View.GONE);
            }
        });

        fabNovaN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("HOME")
                        .replace(R.id.home_container, fragNota)
                        .commit();
//                fabopcoes.setVisibility(View.GONE);
            }
        });

        fabNovoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("HOME")
                        .replace(R.id.home_container, fragProj)
                        .commit();
//                fabopcoes.setVisibility(View.GONE);
            }
        });


        return v;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void criarDiretorios(){

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            File f0 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_RAIZ);
            File f1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_NOTAS);
            File f2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + Constantes.DIRETORIO_PROJETOS);

            if(!f0.exists()) {
                f0.mkdirs();
                f1.mkdirs();
                f2.mkdirs();

                Log.d("KAMIS", "entrou aqui "+ f0.getAbsolutePath());
            }


        }
    }

    private boolean isExternalStorageWritable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("KAMIS", "Opa, escreve sim");
            return true;
        }else{
            return false;
        }
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


}
