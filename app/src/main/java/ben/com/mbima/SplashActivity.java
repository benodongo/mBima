package ben.com.mbima;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ben.com.mbima.helpers.Preferences;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = new Preferences(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferences.isLoggedin() == true)
                {
                    Intent intent = new Intent(SplashActivity.this,NavActivity.class);
                    startActivity(intent);

                }
                else {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);

                }


            }
        },SPLASH_TIME_OUT);
    }
}
