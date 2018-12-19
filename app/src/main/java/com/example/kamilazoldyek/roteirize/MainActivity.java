package com.example.kamilazoldyek.roteirize;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container_def) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }else if(savedInstanceState==null){
                // Create a new Fragment to be placed in the activity layout
                HomeFragment homeFragment = new HomeFragment();
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container_def, homeFragment)
                        .commit();
                }
            }


    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askPermission(String permission){
        requestPermissions(new String[]{permission}, 1);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if(count==0){
//            super.onBackPressed();
//        }else {
//            getFragmentManager().popBackStack();
//        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();



//            if (id == R.id.nav_newProject) {
//                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                        ft.replace(R.id.fragment_container_def, new ProjetoNovoFragment());
//                        ft.addToBackStack("def");
////                for (int i=0; i< getSupportFragmentManager().getBackStackEntryCount(); i++){
////                    getSupportFragmentManager().popBackStackImmediate();
////
////                }
//                        ft.commit();
//
//            } else

                if (id == R.id.nav_projects) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_def,new ProjetosListaFragment())
                        .addToBackStack("def")
                        .commit();

            }else if (id == R.id.nav_home){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_def,new HomeFragment())
                        .addToBackStack("def")
                        .commit();

            }else if (id == R.id.nav_note_list) {
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container_def,new NotasListaFragment());
                        ft.addToBackStack("def");
//                for (int i=0; i< getSupportFragmentManager().getBackStackEntryCount(); i++){
//                    getSupportFragmentManager().popBackStackImmediate();
//
//                }


                ft.commit();


            }
//            else if (id == R.id.nav_new_note) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container_def,  new NotaNovaFragment())
//                        .addToBackStack("def")
//                        .commit();
//
//
//            }
            else if (id == R.id.nav_alarm) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_def,   new AlarmeFragment())
                        .addToBackStack("def")
                        .commit();

            }else if (id == R.id.about) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_def, new SobreFragment())
                        .addToBackStack("def")
                        .commit();
            }





//        } else if (id == R.id.nav_export) {
//
//        } else if (id == R.id.nav_mail) {







        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







}
