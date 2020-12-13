package mobile.project.onlinecoursesforstudent.Menu.ui.DaftarPelajaran;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mobile.project.onlinecoursesforstudent.R;
import mobile.project.onlinecoursesforstudent.firebase.AdapterListMatkul;
import mobile.project.onlinecoursesforstudent.firebase.ModelMatkul;

public class DaftarPelajaranFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //loginsession
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    List<ModelMatkul> list;
    AdapterListMatkul adapterList = new AdapterListMatkul(list, getActivity());
    ProgressDialog progressDialog;

    Button tambahPelajaran;
    EditText cari;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_daftar_pelajaran, container, false);

        tambahPelajaran = root.findViewById(R.id.tambahPelajaran);
        cari = root.findViewById(R.id.cariMatkul);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Tunggu sebentar");
        progressDialog.setMessage("Menyiapkan Mata Pelajaran...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        final RecyclerView recyclerView = root.findViewById(R.id.matkulRV);

        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mata Pelajaran");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list = new ArrayList<ModelMatkul>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ModelMatkul model = new ModelMatkul();
                    ModelMatkul valueMatkul = dataSnapshot1.getValue(ModelMatkul.class);
                    String valueNama = (String) dataSnapshot1.child(emailPengguna).getValue();
                    String matkul = valueMatkul.getMatkul();
                    model.setMatkul(matkul);
                    model.setNama(valueNama);
                    list.add(model);
                    adapterList = new AdapterListMatkul(list, getActivity());
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapterList);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        return root;
    }

    private void filter(String text) {
        ArrayList<ModelMatkul> filteredList = new ArrayList<>();
        for (ModelMatkul item : list) {
            if (item.getMatkul().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterList.filterList(filteredList);
    }
}