package mobile.project.onlinecoursesforstudent.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import mobile.project.onlinecoursesforstudent.R;

public class RegisterMenuForAdmin extends AppCompatActivity {
    EditText email;
    EditText password, namaLengkap, noTelp, umur;
    Spinner jenisKelamin, hakAkses;
    Button register;
    ImageView foto;
    FirebaseAuth firebaseAuth;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri filePath, fotonya;

    Bitmap bitmap;

    private final int PICK_IMAGE_REQUEST = 71;

    ProgressDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_menu_for_admin);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        spotsDialog = new ProgressDialog(this);

        email = findViewById(R.id.edtEmailRegisterr);
        password = findViewById(R.id.edtPasswordRegisterr);
        namaLengkap = findViewById(R.id.namaLengkapp);
        noTelp = findViewById(R.id.noTelpp);
        register = findViewById(R.id.btnRegisterUserr);
        umur = findViewById(R.id.Umurr);
        jenisKelamin = findViewById(R.id.jenisKelaminn);
        hakAkses = findViewById(R.id.hakAksess);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAkun();
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
        Intent intent = new Intent(RegisterMenuForAdmin.this, MenuUtama.class);
        startActivity(intent);
        finish();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void CreateAkun() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Membuat Akun");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String result = email.getText().toString().replaceAll("[\\-\\+\\.\\^:,]", "");
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Akun").child(result);

                            DatabaseReference fieldNama = mRef.child("NamaLengkap");
                            DatabaseReference fieldTelp = mRef.child("NoTelp");
                            DatabaseReference fieldEmail = mRef.child("Email");
                            DatabaseReference fieldPassword = mRef.child("Password");
                            DatabaseReference fieldUmur = mRef.child("Umur");
                            DatabaseReference fieldJK = mRef.child("JenisKelamin");
                            DatabaseReference fieldStatus = mRef.child("Status");

                            fieldNama.setValue(namaLengkap.getText().toString());
                            fieldTelp.setValue(noTelp.getText().toString());
                            fieldEmail.setValue(email.getText().toString());
                            fieldPassword.setValue(password.getText().toString());
                            fieldUmur.setValue(umur.getText().toString());
                            fieldJK.setValue(jenisKelamin.getSelectedItem().toString());
                            fieldStatus.setValue(hakAkses.getSelectedItem().toString());

                            Toast.makeText(RegisterMenuForAdmin.this, "Register Sukses", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(RegisterMenuForAdmin.this, MenuUtama.class);
                            startActivity(intent);

                            spotsDialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(RegisterMenuForAdmin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            spotsDialog.dismiss();
                        }
                    }
                });
    }

    private void pilihGambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}