package ben.com.mbima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ben.com.mbima.helpers.Preferences;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Preferences preferences = new Preferences(this);
        TextView name = findViewById(R.id.name);
        name.setText(preferences.getName());
        TextView email = findViewById(R.id.email);
        email.setText(preferences.getEmail());
        TextView phone = findViewById(R.id.phone);
        phone.setText(preferences.getPhone());
    }
}
