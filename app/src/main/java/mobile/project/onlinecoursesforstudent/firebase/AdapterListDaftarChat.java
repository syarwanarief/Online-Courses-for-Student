package mobile.project.onlinecoursesforstudent.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mobile.project.onlinecoursesforstudent.Menu.MenuChating;
import mobile.project.onlinecoursesforstudent.R;

public class AdapterListDaftarChat extends RecyclerView.Adapter<AdapterListDaftarChat.ViewHolder> {

    List<ModelGetEmail> list = new ArrayList<ModelGetEmail>();
    List<ModelChatView> list2 = new ArrayList<>();
    Context context;

    public AdapterListDaftarChat(List<ModelGetEmail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterListDaftarChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_daftar_chat, parent, false);
        AdapterListDaftarChat.ViewHolder myHoder = new AdapterListDaftarChat.ViewHolder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListDaftarChat.ViewHolder holder, int position) {

        ModelGetEmail mylist = list.get(position);

        SharedPreferences sharedpreferences;
        final String MyPREFERENCES = "MyPrefs";
        final String Pass = "passKey";
        final String Emaill = "emailKey";

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        Picasso.with(context).load(mylist.getFotoProfil())
                .fit()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.pp);

        holder.nama.setText(mylist.getNamaLengkap());

        Picasso.with(context).load(mylist.getFotoProfil())
                .fit()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.pp);

        holder.klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPengguna = mylist.getEmail().replaceAll("[\\-\\+\\.\\^:,]", "");
                Intent intent = new Intent(context, MenuChating.class);
                intent.putExtra("email", emailPengguna);
                context.startActivity(intent);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, isichat;
        ImageView pp;
        LinearLayout klik;

        public ViewHolder(View itemview) {
            super(itemview);
            nama = (TextView) itemview.findViewById(R.id.emailChat);
            isichat = (TextView) itemview.findViewById(R.id.isiChatDC);
            pp = (ImageView) itemview.findViewById(R.id.profilChat);
            klik = itemview.findViewById(R.id.klikChat);
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

    public void filterList(ArrayList<ModelGetEmail> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}
