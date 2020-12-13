package mobile.project.onlinecoursesforstudent.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import mobile.project.onlinecoursesforstudent.Menu.MenuUtama;
import mobile.project.onlinecoursesforstudent.R;

import static mobile.project.onlinecoursesforstudent.service.App.CHANNEL_ID;
import static mobile.project.onlinecoursesforstudent.service.App.CHANNEL_ID2;

public class ServiceChannel extends android.app.Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DatabaseReference getNotif = FirebaseDatabase.getInstance().getReference().child("Notif").child("Notif Admin");
        getNotif.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                String notif = (String) ds.child("notif").getValue();

                if (notif != null) {
                    Intent notificationIntent = new Intent(ServiceChannel.this, MenuUtama.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ServiceChannel.this, 0, notificationIntent, 0);
                    Notification notification = new NotificationCompat.Builder(ServiceChannel.this, CHANNEL_ID)
                            .setContentTitle("Pemberitahuan")
                            .setContentText(notif)
                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                            .setContentIntent(pendingIntent)
                            .build();

                    startForeground(1, notification);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return START_NOT_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
