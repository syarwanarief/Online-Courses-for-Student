package mobile.project.onlinecoursesforstudent.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import mobile.project.onlinecoursesforstudent.R;

public class EditProfil extends AppCompatActivity {

    EditText namaLengkap, noTelp, umur;
    Spinner jenisKelamin;
    Button simpan;
    ImageView fotoProfil;
    FirebaseAuth firebaseAuth;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri filePath, fotonya;

    Bitmap bitmap;

    private final int PICK_IMAGE_REQUEST = 71;

    ProgressDialog spotsDialog;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        spotsDialog = new ProgressDialog(this);

        namaLengkap = findViewById(R.id.namaLengkapBaru);
        noTelp = findViewById(R.id.noTelpBaru);
        simpan = findViewById(R.id.btnSimpanProfilBaru);
        fotoProfil = findViewById(R.id.fotoProfilLoginBaru);
        umur = findViewById(R.id.UmurBaru);
        jenisKelamin = findViewById(R.id.jenisKelaminBaru);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Akun").child(emailPengguna);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String foto = (String) dataSnapshot.child("FotoProfil").getValue();
                String sNama = (String) dataSnapshot.child("NamaLengkap").getValue();
                String sNotelp = (String) dataSnapshot.child("NoTelp").getValue();
                String sUmur = (String) dataSnapshot.child("Umur").getValue();
                String sJK = (String) dataSnapshot.child("JenisKelamin").getValue();

                Picasso.with(EditProfil.this).load(foto)
                        .fit()
                        .into(fotoProfil);

                namaLengkap.setText(sNama);
                noTelp.setText(sNotelp);
                umur.setText(sUmur);

                if (sJK.equals("Laki-Laki")) {
                    jenisKelamin.setSelection(1);
                } else {
                    jenisKelamin.setSelection(2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfil.this, "Profil Tidak Ditemukan", Toast.LENGTH_SHORT).show();
            }
        });

        fotoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihGambar();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfil();
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
        Intent intent = new Intent(EditProfil.this, MenuUtama.class);
        startActivity(intent);
        finish();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void EditProfil() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Membuat Akun");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            File file = new File(String.valueOf(filePath));

            long sizeImage = file.length() / 1024;

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
            String path = MediaStore.Images.Media.insertImage(EditProfil.this.getContentResolver(), bitmap, String.valueOf(filePath), null);
            fotonya = Uri.parse(path);

            final StorageReference reference = storageReference.child("FotoProfil/" + UUID.randomUUID().toString());
            reference.putFile(fotonya)
                    .addOnSuccessListener(taskSnapshot -> progressDialog.dismiss())
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfil.this, "Gagal Mengunggah" + e, Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Status " + (int) progress + "%");
                        }
                    }).addOnCompleteListener(task -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                String image = uri.toString();

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String getEmail = sharedpreferences.getString(Emaill, "");
                String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Akun").child(emailPengguna);

                DatabaseReference fieldFoto = mRef.child("FotoProfil");
                DatabaseReference fieldNama = mRef.child("NamaLengkap");
                DatabaseReference fieldTelp = mRef.child("NoTelp");
                DatabaseReference fieldUmur = mRef.child("Umur");
                DatabaseReference fieldJK = mRef.child("JenisKelamin");

                fieldFoto.setValue(image);
                fieldNama.setValue(namaLengkap.getText().toString());
                fieldTelp.setValue(noTelp.getText().toString());
                fieldUmur.setValue(umur.getText().toString());
                fieldJK.setValue(jenisKelamin.getSelectedItem().toString());

                Toast.makeText(EditProfil.this, "Sukses", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(EditProfil.this, Login.class);
                startActivity(intent);

                spotsDialog.dismiss();
                finish();
            }));

        } else if (filePath == null) {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String getEmail = sharedpreferences.getString(Emaill, "");
            String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Akun").child(emailPengguna);

            DatabaseReference fieldNama = mRef.child("NamaLengkap");
            DatabaseReference fieldTelp = mRef.child("NoTelp");
            DatabaseReference fieldUmur = mRef.child("Umur");
            DatabaseReference fieldJK = mRef.child("JenisKelamin");

            fieldNama.setValue(namaLengkap.getText().toString());
            fieldTelp.setValue(noTelp.getText().toString());
            fieldUmur.setValue(umur.getText().toString());
            fieldJK.setValue(jenisKelamin.getSelectedItem().toString());

            Toast.makeText(EditProfil.this, "Sukses", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(EditProfil.this, MenuUtama.class);
            startActivity(intent);

            spotsDialog.dismiss();
            finish();
        } else {
            Toast.makeText(EditProfil.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                fotoProfil.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}