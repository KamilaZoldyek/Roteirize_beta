package com.example.kamilazoldyek.roteirize;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {


    String TAG = "Lembrete";


    public static final String LEMBRETE = "Já escreveu hoje?";
    public static final String LEMBRETE2 = "Um parágrafo já basta!";

    @Override
    public void onReceive(Context context, Intent intent) {

        String headline = GeradorDeImprobabilidadeInfinita.headlineGenerator();
        String tagline = GeradorDeImprobabilidadeInfinita.taglinesGenerator();


//        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
          if(Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())){
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                AlarmeAgendar.setReminder(context, AlarmReceiver.class,
                    localData.get_hour(),
                    localData.get_min());
            return;
        }

        AlarmeAgendar.showNotification(context, MainActivity.class,
                headline, tagline);
    }


}
