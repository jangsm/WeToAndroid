package com.ninano.weto.src;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.ninano.weto.R;
import com.ninano.weto.src.main.MainActivity;

import static com.ninano.weto.src.ApplicationClass.sSharedPreferences;

public class CellularService extends JobService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("job", "create");
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Toast.makeText(getApplicationContext(), "와이파이 연결 끊어짐", Toast.LENGTH_LONG).show();
        System.out.println("cellular startjob");
        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
        String currentWifiBSSID = sf.getString("recentWifi", "");
        boolean isFirstWifiNoti = sf.getBoolean("firstWifiNoti", false);
        if(isFirstWifiNoti){
            if (currentWifiBSSID.equals("b4:a9:4f:50:d9:e2")){
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("recentWifi", "");
                editor.putBoolean("firstWifiNoti", false);
                editor.apply();

                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

                final String CHANNEL_ID = "채널ID";
                NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    final String CHANNEL_NAME = "채널이름";
                    final String CHANNEL_DESCRIPTION = "채널 Description";
                    final int importance = NotificationManager.IMPORTANCE_HIGH;

                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
                    mChannel.setDescription(CHANNEL_DESCRIPTION);
                    mChannel.enableLights(true);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                    mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(mChannel);
                    }
                }


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setWhen(System.currentTimeMillis());
                builder.setContentTitle("와이파이 해제");
                builder.setContentText("와이파이 해제되었습니다.");
                builder.setContentIntent(pendingIntent);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setAutoCancel(true);
                if (notificationManager != null) {
                    notificationManager.notify(1, builder.build());
                }
                jobFinished(jobParameters, false);
            } else {
                Toast.makeText(getApplicationContext(), "와이파이 연결 끊어짐", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("job", "startCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("job", "destroy");
    }
}