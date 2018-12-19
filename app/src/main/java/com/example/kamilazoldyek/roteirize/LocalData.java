package com.example.kamilazoldyek.roteirize;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalData {


    private static final String APP_SHARED_PREFS = "LembretePref";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;


    private static final String lembreteStatus = "lembreteStatus";
    private static final String hour = "hora";
    private static final String min = "min";
    private static final String FIRSTTIMEUSER = "FIRST_TIME_USER";



    public LocalData(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }


    public boolean getReminderStatus(){

        return appSharedPrefs.getBoolean(lembreteStatus, false);

    }

   public void setReminderStatus(boolean status){
        prefsEditor.putBoolean(lembreteStatus, status);
        prefsEditor.commit();
   }
   public int get_hour(){
        return appSharedPrefs.getInt(hour,20);
   }
   public void set_hour(int h){
        prefsEditor.putInt(hour, h);
        prefsEditor.commit();
   }
    public int get_min(){
        return appSharedPrefs.getInt(min,00);
    }
    public void set_min(int m){
        prefsEditor.putInt(min, m);
        prefsEditor.commit();
    }
    public void reset(){
        prefsEditor.clear();
        prefsEditor.commit();
    }


    public String getEscrito(String key){

        return appSharedPrefs.getString(key, "0");
    }

    public void setEscrito(String key, String newValue){

        prefsEditor.remove(key);
        prefsEditor.commit();
        prefsEditor.putString(key, newValue);
        prefsEditor.commit();
    }

    public String getNomeProjeto(String key){

        return appSharedPrefs.getString(key, "");
    }

    public void setNomeProjeto(String key, String newValue){

        prefsEditor.remove(key);
        prefsEditor.commit();
        prefsEditor.putString(key, newValue);
        prefsEditor.commit();
    }

    public Boolean getFirstTimeUser(){

        return appSharedPrefs.getBoolean(FIRSTTIMEUSER, true);
    }

    public void setFirstTimeUser(Boolean newValue){

        prefsEditor.remove(FIRSTTIMEUSER);
        prefsEditor.commit();
        prefsEditor.putBoolean(FIRSTTIMEUSER, newValue);
        prefsEditor.commit();
    }

}
