package com.example.avnijain.alarms;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static int i=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        //Toast.makeText(context, "Alarm received", Toast.LENGTH_SHORT).show();
        int id = intent.getIntExtra("id",1);
        String  title = intent.getStringExtra(IntentConstants.ExpenseTitle);
        String category = intent.getStringExtra(IntentConstants.ExpenseCategory);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(false)
                .setContentTitle(title)
                .setContentText(category);
        Intent resultintent = new Intent(context,ExpenseDetail.class);
        resultintent.putExtra("id",i);
        resultintent.putExtra(IntentConstants.ExpenseTitle,title);
        resultintent.putExtra(IntentConstants.ExpenseCategory,category);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,i,resultintent,PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(i,nBuilder.build());

    }
}
