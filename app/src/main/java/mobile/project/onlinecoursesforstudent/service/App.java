package mobile.project.onlinecoursesforstudent.service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class App extends Application {
    public static final String CHANNEL_ID = "ServiceChannel";
    public static final String CHANNEL_ID2 = "ServiceTugas";
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel Tugas = new NotificationChannel(
                    CHANNEL_ID2,"Tugas",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,"Lainnya",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager managertugas = getSystemService(NotificationManager.class);
            managertugas.createNotificationChannel(Tugas);
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }
    }
}
