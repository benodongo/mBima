package ben.com.mbima;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ben.com.mbima.Adapters.PremiumAdapter;
import ben.com.mbima.helpers.SqliteHelper;
import ben.com.mbima.models.Clients;

public class ViewClient extends AppCompatActivity implements View.OnClickListener {
    TextView tv_name,tv_residence,tv_phone,tv_co,tv_policy,tv_date;
    AppCompatImageButton btn_phone,btn_sms;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);
        sqliteHelper = new SqliteHelper(this);

        initViews();

    }

    private void initViews() {
        btn_phone = findViewById(R.id.btn_phone);
        btn_phone.setOnClickListener(this);
        btn_sms = findViewById(R.id.btn_sms);
        btn_sms.setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_residence = findViewById(R.id.tv_residence);
        tv_phone = findViewById(R.id.tv_phone);
        tv_co = findViewById(R.id.tv_co);
        tv_policy = findViewById(R.id.tv_policy);
        tv_date = findViewById(R.id.tv_date);
        tv_name.setText(getIntent().getExtras().getString("fname")+" "+getIntent().getExtras().getString("lname"));
        final String name = getIntent().getExtras().getString("fname");
        final String clientID = getIntent().getExtras().getString("id");
        tv_residence.setText("Nairobi");
        tv_phone.setText(getIntent().getExtras().getString("phone"));
        tv_co.setText(getIntent().getExtras().getString("company"));
        final String company = getIntent().getExtras().getString("company");
        tv_policy.setText(getIntent().getExtras().getString("policy"));
        final String policy = getIntent().getExtras().getString("policy");
        tv_date.setText(getIntent().getExtras().getString("date"));
        //recycler view for premiums
        RecyclerView rc = findViewById(R.id.rv_detail);
        rc.setHasFixedSize(true);
        RecyclerView.LayoutManager rl = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(rl);
        final ArrayList<Clients> clients = sqliteHelper.getPremiums(name);
        PremiumAdapter adapter = new PremiumAdapter(getApplicationContext(),clients);
        rc.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewClient.this,PremiumEntry.class);
                i.putExtra("clientID",clientID);
               i.putExtra("name",name);
               i.putExtra("company",company);
                i.putExtra("policy",policy);
                startActivity(i);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_phone:
                String phone = "+254705361244";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;
            case R.id.btn_sms:
                String phoneNumber = "+254705361244";
                String message = "Your policy is about to expire";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                i.putExtra("sms_body", message);
                startActivity(i);

        }
    }
}
