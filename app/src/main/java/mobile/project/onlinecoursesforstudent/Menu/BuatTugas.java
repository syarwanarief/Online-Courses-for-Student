package mobile.project.onlinecoursesforstudent.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import mobile.project.onlinecoursesforstudent.R;
import mobile.project.onlinecoursesforstudent.firebase.AdapterListMatkul;
import mobile.project.onlinecoursesforstudent.firebase.AdapterListTugas;
import mobile.project.onlinecoursesforstudent.firebase.ModelMatkul;
import mobile.project.onlinecoursesforstudent.firebase.ModelTugas;
import mobile.project.onlinecoursesforstudent.service.ServiceChannel;
import mobile.project.onlinecoursesforstudent.service.Tugas;

public class BuatTugas extends AppCompatActivity {

    //loginsession
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ArrayList<String> list;

    RadioGroup rbgroup;
    RadioButton radioButton;
    String pilihan;
    ProgressDialog progressDialog;
    EditText deskripsi, link;
    Spinner spinner;
    Button buat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_tugas);

        link = findViewById(R.id.link);
        deskripsi = findViewById(R.id.deskripsi);
        spinner = findViewById(R.id.spinnerPilihMatkul);
        buat = findViewById(R.id.btnBuatTugas);

        progressDialog = new ProgressDialog(this);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mata Pelajaran");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list = new ArrayList<String>();

                list.add("----");

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String valueMatkul = (String) dataSnapshot1.child("Matkul").getValue();
                    list.add(valueMatkul);
                }

                ArrayAdapter<String> numAdapter = new ArrayAdapter<String>(BuatTugas.this,
                        android.R.layout.simple_spinner_dropdown_item, list);
                spinner.setAdapter(numAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (link.getText().toString() == null) {
                    Toast.makeText(BuatTugas.this, "Link masih kosong", Toast.LENGTH_SHORT).show();
                } else if (deskripsi.getText().toString() == null) {
                    Toast.makeText(BuatTugas.this, "Deskripsi masih kosong", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setTitle("Tunggu sebentar");
                    progressDialog.setMessage("Membuat Tugas...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
/*
                // get selected radio button from radioGroup
                int selectedId = rbgroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);*/

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tugas").child(spinner.getSelectedItem().toString());
                    reference.keepSynced(true);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {

                            String nama = (String) ds.child(emailPengguna).getValue();
                            String matkul = (String) ds.child("Matkul").getValue();

                            if (matkul == null) {
                                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Tugas").child(spinner.getSelectedItem().toString());

                                DatabaseReference fieldDeskripsi = mRef.child("Deskripsi");
                                DatabaseReference fieldMatkul = mRef.child("Matkul");
                                DatabaseReference fieldPilihan = mRef.child("Link");
                                DatabaseReference fieldDibuat = mRef.child("DibuatOleh");


                                fieldDeskripsi.setValue(deskripsi.getText().toString());
                                fieldMatkul.setValue(spinner.getSelectedItem().toString());
                                fieldPilihan.setValue(link.getText().toString());
                                fieldDibuat.setValue(getEmail);

                                Toast.makeText(BuatTugas.this, "Berhasil", Toast.LENGTH_SHORT).show();

                                DatabaseReference mRef1 = FirebaseDatabase.getInstance().getReference().child("Notif").child(spinner.getSelectedItem().toString());
                                DatabaseReference notif = mRef1.child("notif");
                                DatabaseReference mapel = mRef1.child("Matkul");
                                notif.setValue("Ada tugas baru untuk mata pelajaran " + spinner.getSelectedItem().toString() + " dimohon untuk segera dikerjakan");
                                mapel.setValue(spinner.getSelectedItem().toString());

                                Intent serviceIntent = new Intent(BuatTugas.this, Tugas.class);
                                ContextCompat.startForegroundService(BuatTugas.this, serviceIntent);

                                new CountDownTimer(1300, 1000) {

                                    public void onTick(long millisUntilFinished) {

                                    }

                                    public void onFinish() {
                                        progressDialog.dismiss();
                                        // setup alert builder
                                        AlertDialog.Builder builder = new AlertDialog.Builder(BuatTugas.this);
                                        builder.setMessage("Ingin buat tugas lagi?");

                                        builder.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });

                                        builder.setNegativeButton("Ya", null);
                                        // buat dan tampilkan alert dialog
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        deskripsi.setText("");
                                        link.setText("");
                                    }

                                }.start();
                            } else {
                                Toast.makeText(BuatTugas.this
                                        , "Tugas untuk pelajaran ini sudah ada, silahkan konfirmasi selesai pada tugas yang telah dibuat",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

    }

    boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Intent intent = new Intent(BuatTugas.this, MenuUtama.class);
        startActivity(intent);
        finish();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}