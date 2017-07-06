package com.comincini_micheli.quest4run.other;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.activity.SplashActivity;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Quest;

import java.util.Date;

/**
 * Created by Daniele on 29/06/2017.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHandler db = new DatabaseHandler(context);
        Quest activeQuest = db.getActiveQuest();
        if(activeQuest!=null) {
            Intent notifyIntent = new Intent(context, SplashActivity.class);
            Bundle b = new Bundle();
            b.putBoolean(Constants.FROM_NOTIFICATION_QUEST_COMPLETED, true);
            notifyIntent.putExtras(b);

            PendingIntent myIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(context.getResources().getString(R.string.quest_complete_notification_title))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(
                            String.format(context.getResources().getString(R.string.quest_complete_notification_text),
                                    activeQuest.getTitle(),
                                    activeQuest.getExpReward())))
                    .setContentIntent(myIntent);


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify((int)((new Date().getTime()/1000L)%Integer.MAX_VALUE), builder.build());
            activeQuest.setCompleted(true);
            activeQuest.setActive(false);
            activeQuest.setDateFinish(System.currentTimeMillis());
            db.updateQuest(activeQuest);

            SharedPreferences settings = context.getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
            Character myCharacter = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE, -1));
            myCharacter.setExp(myCharacter.getExp() + activeQuest.getExpReward());
            db.updateCharacter(myCharacter);
        }
    }
}
