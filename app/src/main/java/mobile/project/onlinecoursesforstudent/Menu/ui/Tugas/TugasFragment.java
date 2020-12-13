package mobile.project.onlinecoursesforstudent.Menu.ui.Tugas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import mobile.project.onlinecoursesforstudent.firebase.AdapterListTugas;
import mobile.project.onlinecoursesforstudent.firebase.ModelMatkul;
import mobile.project.onlinecoursesforstudent.firebase.ModelTugas;
import mobile.project.onlinecoursesforstudent.service.ServiceChannel;

public class TugasFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    TextView textTugas;
    RecyclerView recyclerView;

    //loginsession
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    List<ModelTugas> list;
    AdapterListTugas adapterList;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tugas, container, false);

        textTugas = root.findViewById(R.id.textTugas);
        recyclerView = root.findViewById(R.id.RVtugas);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Tunggu sebentar");
        progressDialog.setMessage("Mencari tugas yang diberikan...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tugas");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list = new ArrayList<ModelTugas>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String matkul = (String) dataSnapshot1.child("Matkul").getValue();
                    String desc = (String) dataSnapshot1.child("Deskripsi").getValue();
                    String link = (String) dataSnapshot1.child("Link").getValue();
                    String status = (String) dataSnapshot1.child(emailPengguna).getValue();

                    if (matkul != null && status == null) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mata Pelajaran").child(matkul);
                        reference.keepSynced(true);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot ds) {

                                String nama = (String) ds.child(emailPengguna).getValue();
                                String matkul = (String) ds.child("Matkul").getValue();

                                ModelTugas modelTugas = new ModelTugas();

                                if (emailPengguna.equals(nama) || emailPengguna == nama) {
                                    modelTugas.setNama(nama);
                                    modelTugas.setMatkul(matkul);
                                    modelTugas.setDesc(desc);
                                    modelTugas.setLink(link);

                                    list.add(modelTugas);
                                    adapterList = new AdapterListTugas(list, getActivity());
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
                    }

                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}