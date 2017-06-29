package com.comincini_micheli.quest4run.other;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.activity.QuestHistoryActivity;
import com.comincini_micheli.quest4run.activity.TaskHistoryActivity;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Quest;

/**
 * Created by Daniele on 29/06/2017.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHandler db = new DatabaseHandler(context);
        Quest activeQuest = db.getActiveQuest();

        Intent notifyIntent = new Intent(context, QuestHistoryActivity.class);
        PendingIntent myIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getResources().getString(R.string.quest_complete_notification_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        String.format(context.getResources().getString(R.string.quest_complete_notification_text),
                                activeQuest.getTitle(),
                                activeQuest.getExpReward())))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info")
                .setContentIntent(myIntent);


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1,builder.build());
        activeQuest.setCompleted(true);
        activeQuest.setActive(false);
        activeQuest.setDateFinish(System.currentTimeMillis());
        db.updateQuest(activeQuest);

        SharedPreferences settings = context.getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        Character myCharacter = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE,-1));
        myCharacter.setExp(myCharacter.getExp()+activeQuest.getExpReward());
        db.updateCharacter(myCharacter);

        //TODO gestire refresh QuestFragment list
        //TODO gestire back quando non esiste activity main
    }
}
