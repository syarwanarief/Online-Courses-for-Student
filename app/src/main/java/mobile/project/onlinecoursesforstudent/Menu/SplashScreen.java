package mobile.project.onlinecoursesforstudent.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import mobile.project.onlinecoursesforstudent.R;

public class SplashScreen extends AppCompatActivity {

    //loginsession
    public static int SPLASH_TIME_OUT = 2300;
    //loginsession
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Pass = "passKey";
    public static final String Emaill = "emailKey";
    public static final String Status = "statusKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //cek status login
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Emaill) && sharedpreferences.contains(Pass)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent homeIntent = new Intent(SplashScreen.this, MenuUtama.class);
                    startActivity(homeIntent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent homeIntent = new Intent(SplashScreen.this, Login.class);
                    startActivity(homeIntent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }
    }
}