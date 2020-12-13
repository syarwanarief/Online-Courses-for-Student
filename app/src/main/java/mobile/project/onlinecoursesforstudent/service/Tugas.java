package mobile.project.onlinecoursesforstudent.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mobile.project.onlinecoursesforstudent.Menu.MenuUtama;
import mobile.project.onlinecoursesforstudent.R;

import static mobile.project.onlinecoursesforstudent.service.App.CHANNEL_ID;
import static mobile.project.onlinecoursesforstudent.service.App.CHANNEL_ID2;

public class Tugas extends android.app.Service {

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

        //loginsession
        SharedPreferences sharedpreferences;
        final String MyPREFERENCES = "MyPrefs";
        final String Pass = "passKey";
        final String Emaill = "emailKey";

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mata Pelajaran");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String matkul = (String) dataSnapshot1.child("Matkul").getValue();
                    String user = (String) dataSnapshot1.child(emailPengguna).getValue();

                    if (user != null) {

                        if (user.equals(emailPengguna)){
                            DatabaseReference getNotif = FirebaseDatabase.getInstance().getReference().child("Notif");
                            getNotif.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot ds) {

                                    for (DataSnapshot dataSnapshot2 : ds.getChildren()) {

                                        String mapel = (String) dataSnapshot2.child("Matkul").getValue();
                                        String notif = (String) dataSnapshot2.child("notif").getValue();

                                        if (mapel == matkul || mapel.equals(matkul)) {
                                            Intent notificationIntent = new Intent(Tugas.this, MenuUtama.class);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(Tugas.this, 0, notificationIntent, 0);
                                            Notification notification = new NotificationCompat.Builder(Tugas.this, CHANNEL_ID2)
                                                    .setContentTitle("Ada tugas baru untukmu")
                                                    .setContentText(notif)
                                                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                                    .setContentIntent(pendingIntent)
                                                    .build();

                                            startForeground(1, notification);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //do heavy work on a background thread
        //stopSelf();
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
