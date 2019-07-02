package com.pulimoottil.richu.momsmagickeralafoodrecipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(this, FirstTutorialActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();*/

        startActivity(new Intent(this, UserLogin.class));
        finish();
    }
}
