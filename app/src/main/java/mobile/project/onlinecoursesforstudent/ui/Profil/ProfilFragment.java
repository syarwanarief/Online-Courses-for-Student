package mobile.project.onlinecoursesforstudent.ui.Profil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import mobile.project.onlinecoursesforstudent.Menu.EditProfil;
import mobile.project.onlinecoursesforstudent.Menu.Login;
import mobile.project.onlinecoursesforstudent.Menu.TambahMatkul;
import mobile.project.onlinecoursesforstudent.R;
import mobile.project.onlinecoursesforstudent.Menu.RegisterMenuForAdmin;

public class ProfilFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    LinearLayout logout, editProfil, tambahPelajaran, tambahUser, menu;
    //loginsession
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";

    ImageView fotoProfil;
    TextView nama;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profil, container, false);

        logout = root.findViewById(R.id.Logout);
        fotoProfil = root.findViewById(R.id.fotoProfil);
        nama = root.findViewById(R.id.namaUser);
        editProfil = root.findViewById(R.id.EditProfil);
        tambahUser = root.findViewById(R.id.tambahUser);
        tambahPelajaran = root.findViewById(R.id.tambahPelajaran);
        menu = root.findViewById(R.id.menuAdmin);

        menu.setVisibility(View.INVISIBLE);

        tambahUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RegisterMenuForAdmin.class);
                getContext().startActivity(intent);
                getActivity().finish();
            }
        });

        tambahPelajaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TambahMatkul.class);
                getContext().startActivity(intent);
                getActivity().finish();
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Akun").child(emailPengguna);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String foto = (String) dataSnapshot.child("FotoProfil").getValue();
                String sNama = (String) dataSnapshot.child("NamaLengkap").getValue();
                String sStatus = (String) dataSnapshot.child("Status").getValue();

                Picasso.with(getContext()).load(foto)
                        .fit()
                        .into(fotoProfil);

                nama.setText(sNama);

                if (sStatus.equals("Admin")){
                    menu.setVisibility(View.VISIBLE);
                }else if (sStatus.equals("Guru")){
                    menu.setVisibility(View.VISIBLE);
                    tambahUser.setVisibility(View.INVISIBLE);
                }else{
                    menu.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Profil Tidak Ditemukan", Toast.LENGTH_SHORT).show();
            }
        });

        editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfil.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Login.class);
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(Emaill);
                editor.remove(Pass);
                editor.commit();
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        return root;
    }
}