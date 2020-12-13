package mobile.project.onlinecoursesforstudent.firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mobile.project.onlinecoursesforstudent.R;

public class AdapterListChat extends RecyclerView.Adapter<AdapterListChat.ViewHolder> {

    List<ModelChatView> list = new ArrayList<ModelChatView>();
    Context context;

    public AdapterListChat(List<ModelChatView> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterListChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_chat, parent, false);
        AdapterListChat.ViewHolder myHoder = new AdapterListChat.ViewHolder(view);

        return myHoder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AdapterListChat.ViewHolder holder, int position) {

        ModelChatView mylist = list.get(position);

        SharedPreferences sharedpreferences;
        final String MyPREFERENCES = "MyPrefs";
        final String Pass = "passKey";
        final String Emaill = "emailKey";

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        if (mylist.getFrom().equals(getEmail) || mylist.getTo().equals(emailPengguna)) {
            holder.chat.setText(mylist.getChat());
            holder.chat.setGravity(Gravity.LEFT);
        }else if (mylist.getTo().equals(getEmail) || mylist.getFrom().equals(emailPengguna)){
            holder.chat.setText(mylist.getChat());
            holder.chat.setGravity(Gravity.RIGHT);/*
            holder.linearLayout.setBackgroundColor(Color.parseColor("#80bfff"));
            holder.cardView.setBackgroundColor(Color.parseColor("#80bfff"));*/

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView chat, chatSaya;
        LinearLayout linearLayout;
        CardView cardView;

        public ViewHolder(View itemview) {
            super(itemview);
            chat = itemview.findViewById(R.id.chat);
            linearLayout = itemview.findViewById(R.id.layoutChat1);
            cardView = itemview.findViewById(R.id.layoutChat);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<ModelChatView> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}
