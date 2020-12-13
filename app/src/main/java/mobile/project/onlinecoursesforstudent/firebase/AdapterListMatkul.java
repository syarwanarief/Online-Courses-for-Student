package mobile.project.onlinecoursesforstudent.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

import mobile.project.onlinecoursesforstudent.Menu.Login;
import mobile.project.onlinecoursesforstudent.Menu.MenuUtama;
import mobile.project.onlinecoursesforstudent.R;
import mobile.project.onlinecoursesforstudent.service.ServiceChannel;
import mobile.project.onlinecoursesforstudent.service.Tugas;

public class AdapterListMatkul extends RecyclerView.Adapter<AdapterListMatkul.ViewHolder> {

    List<ModelMatkul> list = new ArrayList<ModelMatkul>();
    Context context;

    public AdapterListMatkul(List<ModelMatkul> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterListMatkul.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_daftar_pelajaran, parent, false);
        AdapterListMatkul.ViewHolder myHoder = new AdapterListMatkul.ViewHolder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListMatkul.ViewHolder holder, int position) {

        ModelMatkul mylist = list.get(position);

        //loginsession
        SharedPreferences sharedpreferences;
        final String MyPREFERENCES = "MyPrefs";
        final String Pass = "passKey";
        final String Emaill = "emailKey";

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        holder.matkul.setText(mylist.getMatkul());

        if (emailPengguna.equals(mylist.getNama())) {
            holder.addMatkul.setVisibility(View.INVISIBLE);
        } else {
            holder.addMatkul.setVisibility(View.VISIBLE);
        }

        holder.addMatkul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailPengguna != null) {

                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Mata Pelajaran")
                            .child(mylist.getMatkul());

                    DatabaseReference fieldMatkul = mRef.child(emailPengguna);

                    fieldMatkul.setValue(emailPengguna);
                    Toast.makeText(context, "Mata Pelajaran " + mylist.getMatkul() + " Berhasil di Ambil", Toast.LENGTH_SHORT).show();

                    Intent serviceTugas = new Intent(context, Tugas.class);
                    ContextCompat.startForegroundService(context, serviceTugas);
                } else {
                    Toast.makeText(context, "Gagal. \nCoba Ulangi Kembali", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView matkul, parsing1, parsing2;
        ImageButton addMatkul;

        public ViewHolder(View itemview) {
            super(itemview);
            matkul = (TextView) itemview.findViewById(R.id.viewMatkul);
            addMatkul = (ImageButton) itemview.findViewById(R.id.btnAddMatkul);
        }


    }

    @Override
    public int getItemCount() {
        int arr = 0;

        try {
            if (list.size() == 0) {
                arr = 0;
            } else {

                arr = list.size();
            }
        } catch (Exception e) {
        }
        return arr;
    }

    public void filterList(ArrayList<ModelMatkul> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}
