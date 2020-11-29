package mobile.project.onlinecoursesforstudent.ui.DaftarPelajaran;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import mobile.project.onlinecoursesforstudent.firebase.FirebaseModelMatkul;

public class DaftarPelajaranFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //loginsession
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    List<FirebaseModelMatkul> list;

    Button tambahPelajaran;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_daftar_pelajaran, container, false);

        tambahPelajaran = root.findViewById(R.id.tambahPelajaran);

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Profil Tidak Ditemukan", Toast.LENGTH_SHORT).show();
            }
        });

        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.matkulRV);
        DatabaseReference referenceMatkul = FirebaseDatabase.getInstance().getReference().child("Mata Pelajaran");
        referenceMatkul.keepSynced(true);
        referenceMatkul.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    list = new ArrayList<FirebaseModelMatkul>();
                    FirebaseModelMatkul model = new FirebaseModelMatkul();
                    FirebaseModelMatkul value = dataSnapshot1.getValue(FirebaseModelMatkul.class);
                    String matkul = value.getMatkul();
                    model.setMatkul(matkul);
                    list.add(model);
                    AdapterListMatkul adapterList = new AdapterListMatkul(list, getActivity());
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapterList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}