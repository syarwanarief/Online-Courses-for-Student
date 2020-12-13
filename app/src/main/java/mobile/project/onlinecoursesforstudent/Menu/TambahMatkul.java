package mobile.project.onlinecoursesforstudent.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import mobile.project.onlinecoursesforstudent.R;

public class TambahMatkul extends AppCompatActivity {

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    Button simpan;
    EditText matkul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_matkul);

        simpan = findViewById(R.id.btnSimpanMatkul);
        matkul = findViewById(R.id.idMatkul);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Mata Pelajaran")
                        .child(matkul.getText().toString());

                DatabaseReference fieldMatkul = mRef.child("Matkul");

                fieldMatkul.setValue(matkul.getText().toString());

                Toast.makeText(TambahMatkul.this, "Register Sukses", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(TambahMatkul.this, MenuUtama.class);
                startActivity(intent);
            }
        });
    }

    //close app
    boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Intent intent = new Intent(TambahMatkul.this, Login.class);
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