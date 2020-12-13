package mobile.project.onlinecoursesforstudent.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import mobile.project.onlinecoursesforstudent.firebase.AdapterListChat;
import mobile.project.onlinecoursesforstudent.firebase.ModelChatView;

public class MenuChating extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";


    List<ModelChatView> list;
    AdapterListChat adapterList;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    RecyclerView recyclerView;
    EditText isichat;
    ImageButton btnSend;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_chating);

        getSupportActionBar().setLogo(R.drawable.ic_baseline_person_24);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recyclerView = findViewById(R.id.idChating);
        btnSend = findViewById(R.id.btnSendChat);
        isichat = findViewById(R.id.textChat);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tunggu sebentar");
        progressDialog.setMessage("Menyiapkan pesan...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        if (emailPengguna.equals("admin@gmailcom")){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chating")
                    .child("admin@gmailcom"+"X"+getIntent().getStringExtra("email"));
            reference.keepSynced(true);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    list = new ArrayList<ModelChatView>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        ModelChatView model = new ModelChatView();
                        ModelChatView valuechat = dataSnapshot1.getValue(ModelChatView.class);
                        ModelChatView valuefrom = dataSnapshot1.getValue(ModelChatView.class);
                        ModelChatView valueto = dataSnapshot1.getValue(ModelChatView.class);

                        String chat = valuechat.getChat();
                        String from = valuefrom.getFrom();
                        String to = valueto.getTo();

                        model.setChat(chat);
                        model.setChatFrom(chat);
                        model.setTo(to);
                        model.setFrom(from);
                        model.setEmail(getIntent().getStringExtra("email"));

                        list.add(model);
                        adapterList = new AdapterListChat(list, MenuChating.this);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MenuChating.this, 1);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.smoothScrollToPosition(adapterList.getItemCount());
                        recyclerView.setAdapter(adapterList);

                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MenuChating.this, "Profil Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chating")
                    .child("admin@gmailcom"+"X"+emailPengguna);
            reference.keepSynced(true);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    list = new ArrayList<ModelChatView>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        ModelChatView model = new ModelChatView();
                        ModelChatView valuechat = dataSnapshot1.getValue(ModelChatView.class);
                        ModelChatView valuefrom = dataSnapshot1.getValue(ModelChatView.class);
                        ModelChatView valueto = dataSnapshot1.getValue(ModelChatView.class);

                        String chat = valuechat.getChat();
                        String from = valuefrom.getFrom();
                        String to = valueto.getTo();

                        model.setChat(chat);
                        model.setChatFrom(chat);
                        model.setTo(to);
                        model.setFrom(from);
                        model.setEmail(getIntent().getStringExtra("email"));

                        list.add(model);
                        adapterList = new AdapterListChat(list, MenuChating.this);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MenuChating.this, 1);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.smoothScrollToPosition(adapterList.getItemCount());
                        recyclerView.setAdapter(adapterList);
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MenuChating.this, "Profil Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            });
        }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isichat.getText().toString().equals("") || isichat.getText().toString() == null){
                    Toast.makeText(MenuChating.this, "Ketik pesan", Toast.LENGTH_SHORT).show();
                }else{

                    if (emailPengguna.equals("admin@gmailcom")){

                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Chating")
                                .child("admin@gmailcom"+"X"+getIntent().getStringExtra("email")).push();

                        DatabaseReference fieldFrom = mRef.child("From");
                        DatabaseReference fieldTo = mRef.child("To");
                        DatabaseReference fieldMatkul = mRef.child("Chat");

                        fieldMatkul.setValue(isichat.getText().toString());
                        fieldFrom.setValue(emailPengguna);
                        fieldTo.setValue(getIntent().getStringExtra("email"));

                        isichat.setText("");

                    }else{
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Chating")
                                .child("admin@gmailcom"+"X"+emailPengguna).push();

                        DatabaseReference fieldFrom = mRef.child("From");
                        DatabaseReference fieldTo = mRef.child("To");
                        DatabaseReference fieldMatkul = mRef.child("Chat");

                        fieldMatkul.setValue(isichat.getText().toString());
                        fieldFrom.setValue(emailPengguna);
                        fieldTo.setValue(getIntent().getStringExtra("email"));

                        isichat.setText("");
                    }

                }
            }
        });
    }
}