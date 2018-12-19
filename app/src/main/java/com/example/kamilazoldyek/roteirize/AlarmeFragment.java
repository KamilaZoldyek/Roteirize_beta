package com.example.kamilazoldyek.roteirize;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AlarmeFragment extends Fragment{

    Context context;
    View v;
    Button button;
    public static final int DAILY_REMINDER_REQUEST_CODE = 100;
    TextView tv;
    LocalData localData;
    Context context2;
    int hour, min;
    Switch lembreteswitch;
    ConstraintLayout timelay;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.alarme_fragment, container, false);
        context = container.getContext();
        context2 = getActivity().getApplicationContext();
        getActivity().setTitle("Lembrete");
        localData = new LocalData(getContext());
        tv = v.findViewById(R.id.datahora);
        lembreteswitch = v.findViewById(R.id.switch2);
        timelay = v.findViewById(R.id.layout_config);

        hour = localData.get_hour();
        min = localData.get_min();

        tv.setText(hour+":"+min);
        lembreteswitch.setChecked(localData.getReminderStatus());

        if(!localData.getReminderStatus()){
            timelay.setAlpha(0.4f);
        }

        lembreteswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localData.setReminderStatus(isChecked);
                if(isChecked){
                    AlarmeAgendar.setReminder(getContext(),
                            AlarmReceiver.class,
                            localData.get_hour(), localData.get_min());
                    timelay.setAlpha(1f);
                }else {
                    AlarmeAgendar.cancelReminder(getContext(), AlarmReceiver.class);
                    timelay.setAlpha(0.4f);
                }

            }
        });

        timelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(localData.getReminderStatus()){
                    showTimePickerDialog(localData.get_hour(),localData.get_min());
                }
            }
        });


        return v;
    }

    private void  showTimePickerDialog(int h, int m){

        TimePickerDialog builder = new TimePickerDialog(getContext(), R.style.Theme_AppCompat_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.d("Lembrete", "hora: "+hourOfDay);
                        Log.d("Lembrete", "min: "+ minute);
                        localData.set_hour(hourOfDay);
                        localData.set_min(minute);
                        tv.setText(hourOfDay+":"+minute);
                        AlarmeAgendar.setReminder(getContext(),
                                AlarmReceiver.class,
                                localData.get_hour(),localData.get_min());
                    }
                }, h, m, true);
//        builder.setCustomTitle(v);
        builder.show();

    }


}
