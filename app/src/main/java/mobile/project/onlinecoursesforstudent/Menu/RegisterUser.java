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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import mobile.project.onlinecoursesforstudent.R;

public class RegisterUser extends AppCompatActivity {

    EditText email;
    EditText password, namaLengkap, noTelp, umur;
    Spinner jenisKelamin;
    Button register;
    ImageView foto;
    FirebaseAuth firebaseAuth;
    TextView tvSambutan;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri filePath, fotonya;

    Bitmap bitmap;

    private final int PICK_IMAGE_REQUEST = 71;

    ProgressDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        spotsDialog = new ProgressDialog(this);

        email = findViewById(R.id.edtEmailRegister);
        password = findViewById(R.id.edtPasswordRegister);
        namaLengkap = findViewById(R.id.namaLengkap);
        noTelp = findViewById(R.id.noTelp);
        tvSambutan = findViewById(R.id.tvSambutan);
        register = findViewById(R.id.btnRegisterUser);
        foto = findViewById(R.id.fotoProfilLogin);
        umur = findViewById(R.id.Umur);
        jenisKelamin = findViewById(R.id.jenisKelamin);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihGambar();
            }
        });


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
        Intent intent = new Intent(RegisterUser.this, Login.class);
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

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Membuat Akun");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            File file = new File(String.valueOf(filePath));

            long sizeImage = file.length() / 1024;

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
            String path = MediaStore.Images.Media.insertImage(RegisterUser.this.getContentResolver(), bitmap, String.valueOf(filePath), null);
            fotonya = Uri.parse(path);

            final StorageReference reference = storageReference.child("FotoProfil/" + UUID.randomUUID().toString());
            reference.putFile(fotonya)
                    .addOnSuccessListener(taskSnapshot -> progressDialog.dismiss())
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUser.this, "Gagal Mengunggah" + e, Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Status " + (int) progress + "%");
                        }
                    }).addOnCompleteListener(task -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                String image = uri.toString();

                String result = email.getText().toString().replaceAll("[\\-\\+\\.\\^:,]", "");

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Akun").child(result);


                                    DatabaseReference fieldFoto = mRef.child("FotoProfil");
                                    DatabaseReference fieldNama = mRef.child("NamaLengkap");
                                    DatabaseReference fieldTelp = mRef.child("NoTelp");
                                    DatabaseReference fieldEmail = mRef.child("Email");
                                    DatabaseReference fieldPassword = mRef.child("Password");
                                    DatabaseReference fieldUmur = mRef.child("Umur");
                                    DatabaseReference fieldJK = mRef.child("JenisKelamin");
                                    DatabaseReference fieldStatus = mRef.child("Status");

                                    fieldFoto.setValue(image);
                                    fieldNama.setValue(namaLengkap.getText().toString());
                                    fieldTelp.setValue(noTelp.getText().toString());
                                    fieldEmail.setValue(email.getText().toString());
                                    fieldPassword.setValue(password.getText().toString());
                                    fieldUmur.setValue(umur.getText().toString());
                                    fieldJK.setValue(jenisKelamin.getSelectedItem().toString());
                                    fieldStatus.setValue("User");

                                    Toast.makeText(RegisterUser.this, "Register Sukses", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(RegisterUser.this, Login.class);
                                    startActivity(intent);

                                    spotsDialog.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterUser.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    spotsDialog.dismiss();
                                }
                            }
                        });

                Toast.makeText(RegisterUser.this, "Berhasil", Toast.LENGTH_SHORT).show();
            }));

        } else {
            Toast.makeText(RegisterUser.this, "Pilih Foto Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }
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