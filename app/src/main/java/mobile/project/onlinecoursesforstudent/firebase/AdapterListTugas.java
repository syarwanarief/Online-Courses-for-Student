package mobile.project.onlinecoursesforstudent.firebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mobile.project.onlinecoursesforstudent.Menu.BuatTugas;
import mobile.project.onlinecoursesforstudent.Menu.DeskripsiTugas;
import mobile.project.onlinecoursesforstudent.R;

public class AdapterListTugas extends RecyclerView.Adapter<AdapterListTugas.ViewHolder> {

    List<ModelTugas> list = new ArrayList<ModelTugas>();
    Context context;

    public AdapterListTugas(List<ModelTugas> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterListTugas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_tugas, parent, false);
        AdapterListTugas.ViewHolder myHoder = new AdapterListTugas.ViewHolder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListTugas.ViewHolder holder, int position) {

        ModelTugas mylist = list.get(position);

        holder.matkul.setText(mylist.getMatkul());
        holder.desc.setText(mylist.getDesc());

        //loginsession
        SharedPreferences sharedpreferences;
        final String MyPREFERENCES = "MyPrefs";
        final String Pass = "passKey";
        final String Emaill = "emailKey";

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Akun").child(emailPengguna);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String foto = (String) dataSnapshot.child("FotoProfil").getValue();
                String sNama = (String) dataSnapshot.child("NamaLengkap").getValue();
                String sStatus = (String) dataSnapshot.child("Status").getValue();

                if (sStatus == "Guru" || sStatus == "Admin"){

                    holder.kerjakan.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Konfirmasi Tugas");
                            builder.setMessage("Tutup sesi tugas?");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            builder.setNegativeButton("Nanti", null);
                            // buat dan tampilkan alert dialog
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return false;
                        }
                    });

                    holder.desc.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Konfirmasi Tugas");
                            builder.setMessage("Tutup sesi tugas?");

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            builder.setNegativeButton("Nanti", null);
                            // buat dan tampilkan alert dialog
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return false;
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Profil Tidak Ditemukan", Toast.LENGTH_SHORT).show();
            }
        });

        holder.kerjakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Kerjakan tugas sekarang?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(context, DeskripsiTugas.class);
                        intent.putExtra("deskripsi",mylist.getDesc());
                        intent.putExtra("link",mylist.getLink());
                        intent.putExtra("matkul",mylist.getMatkul());
                        context.startActivity(intent);
                    }
                });

                builder.setNegativeButton("Nanti", null);
                // buat dan tampilkan alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Kerjakan tugas sekarang?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, DeskripsiTugas.class);
                        intent.putExtra("deskripsi",mylist.getDesc());
                        intent.putExtra("link",mylist.getLink());
                        intent.putExtra("matkul",mylist.getMatkul());
                        context.startActivity(intent);
                    }
                });

                builder.setNegativeButton("Nanti", null);
                // buat dan tampilkan alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView matkul, desc, parsing2;
        LinearLayout kerjakan;

        public ViewHolder(View itemview) {
            super(itemview);
            matkul = itemview.findViewById(R.id.viewMatkulTugas);
            kerjakan = itemview.findViewById(R.id.klikTugas);
            desc = itemview.findViewById(R.id.viewDesc);
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

    public void filterList(ArrayList<ModelTugas> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}
