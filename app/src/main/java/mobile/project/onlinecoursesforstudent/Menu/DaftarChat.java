package mobile.project.onlinecoursesforstudent.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import mobile.project.onlinecoursesforstudent.R;
import mobile.project.onlinecoursesforstudent.firebase.AdapterListChat;
import mobile.project.onlinecoursesforstudent.firebase.AdapterListDaftarChat;
import mobile.project.onlinecoursesforstudent.firebase.AdapterListDaftarChat;
import mobile.project.onlinecoursesforstudent.firebase.ModelChatView;
import mobile.project.onlinecoursesforstudent.firebase.ModelGetEmail;
import mobile.project.onlinecoursesforstudent.firebase.ModelGetEmail;

public class DaftarChat extends AppCompatActivity {
    //loginsession
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";
    ProgressDialog progressDialog;

    List<ModelGetEmail> list;
    List<ModelChatView> list2;
    AdapterListDaftarChat adapterList = new AdapterListDaftarChat(list, DaftarChat.this);
    AdapterListChat adapterListChat;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_chat);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        final RecyclerView recyclerView = findViewById(R.id.RVdaftarchat);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("memuat...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        if (emailPengguna.equals("admin@gmailcom")) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Akun");
            reference.keepSynced(true);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    list = new ArrayList<ModelGetEmail>();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        ModelGetEmail model = new ModelGetEmail();
                        ModelGetEmail valueEmail = dataSnapshot1.getValue(ModelGetEmail.class);
                        ModelGetEmail valueNama = dataSnapshot1.getValue(ModelGetEmail.class);
                        ModelGetEmail valueProfil = dataSnapshot1.getValue(ModelGetEmail.class);

                        String email = valueEmail.getEmail();
                        String namaLengkap = valueNama.getNamaLengkap();
                        String profil = valueProfil.getFotoProfil();

                        if (email != null){
                            model.setEmail(email);
                            model.setNamaLengkap(namaLengkap);
                            model.setFotoProfil(profil);

                            list.add(model);
                            adapterList = new AdapterListDaftarChat(list, DaftarChat.this);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(DaftarChat.this, 1);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapterList);
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Akun").child("admin@gmailcom");
            reference.keepSynced(true);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    list = new ArrayList<ModelGetEmail>();

                    ModelGetEmail model = new ModelGetEmail();
                    ModelGetEmail valueEmail = dataSnapshot.getValue(ModelGetEmail.class);
                    ModelGetEmail valueNama = dataSnapshot.getValue(ModelGetEmail.class);
                    ModelGetEmail valueProfil = dataSnapshot.getValue(ModelGetEmail.class);

                    String email = valueEmail.getEmail();
                    String namaLengkap = valueNama.getNamaLengkap();
                    String profil = valueProfil.getFotoProfil();

                    model.setEmail(email);
                    model.setNamaLengkap(namaLengkap);
                    model.setFotoProfil(profil);

                    list.add(model);
                    adapterList = new AdapterListDaftarChat(list, DaftarChat.this);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(DaftarChat.this, 1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapterList);
                    progressDialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    //close app
    public void onBackPressed() {
        Intent intent = new Intent(DaftarChat.this, MenuUtama.class);
        startActivity(intent);
        finish();
    }
}