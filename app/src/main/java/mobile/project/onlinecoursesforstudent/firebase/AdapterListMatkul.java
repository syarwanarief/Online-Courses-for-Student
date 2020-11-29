package mobile.project.onlinecoursesforstudent.firebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mobile.project.onlinecoursesforstudent.R;

public class AdapterListMatkul extends RecyclerView.Adapter<AdapterListMatkul.ViewHolder> {

    List<FirebaseModelMatkul> list = new ArrayList<FirebaseModelMatkul>();
    Context context;

    public AdapterListMatkul(List<FirebaseModelMatkul> list, Context context) {
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

        FirebaseModelMatkul mylist = list.get(position);

        holder.matkul.setText(mylist.getMatkul());

        holder.add_matkul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView matkul;
        ImageButton add_matkul;

        public ViewHolder(View itemview) {
            super(itemview);
            matkul = (TextView) itemview.findViewById(R.id.viewMatkul);
            add_matkul = itemview.findViewById(R.id.btnAddMatkul);
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
}
