package mobile.project.onlinecoursesforstudent.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mobile.project.onlinecoursesforstudent.R;

public class DeskripsiTugas extends AppCompatActivity {
    //loginsession
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";

    TextView matkul,desc,link;
    Button kerjakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_tugas);
        
        matkul = findViewById(R.id.matkulViewTugas);
        desc = findViewById(R.id.deskViewTugas);
        link = findViewById(R.id.linkViewTugas);
        kerjakan = findViewById(R.id.btnKerjakan);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String getEmail = sharedpreferences.getString(Emaill, "");
        String emailPengguna = getEmail.replaceAll("[\\-\\+\\.\\^:,]", "");

        Bundle intent = getIntent().getExtras();
        matkul.setText(intent.getString("matkul"));
        desc.setText(intent.getString("deskripsi"));
        link.setText(intent.getString("link"));

        kerjakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = intent.getString("link");
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Tugas").child(intent.getString("matkul"));

                DatabaseReference fieldStatus = mRef.child(emailPengguna);

                fieldStatus.setValue("Selesai");

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

    }
}