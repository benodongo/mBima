package ben.com.mbima;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import ben.com.mbima.Adapters.DataAdapter;
import ben.com.mbima.helpers.RecyclerTouchListener;
import ben.com.mbima.helpers.SqliteHelper;
import ben.com.mbima.models.Clients;

public class MyClientsActivity extends AppCompatActivity {
    private String [] names = {"John Doe Milambo","Benson Odongo O","Miles Marsai K.","Kelvin Makau Junior",
    "Grace Michal Rad","Jack Willis RED","Rainbow Mikai","Renne Michaels Shem"};
    private String[] policies = {"Personal Accident","Health Insurance","Life Assurance","General Insurance",
            "Education Policy","Vehicle Insurance","Accident and Fire","Profit & Loss Policy"};
    private String[] dates = {"12 April 2018","2 May 2018","18 Jun 2018","3 Sep 2017",
            "12 April 2018","2 May 2018","18 Jun 2018","3 Sep 2017"};
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clients);
        sqliteHelper = new SqliteHelper(this);
        initViews();
    }

    private void initViews() {
        RecyclerView rc = findViewById(R.id.recycler_view);
        rc.setHasFixedSize(true);
        RecyclerView.LayoutManager rl = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(rl);
        String type = "Vehicle";
        final ArrayList<Clients> clients = sqliteHelper.getAll();
        DataAdapter adapter = new DataAdapter(clients,getApplicationContext());
        rc.setAdapter(adapter);
        rc.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rc, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Clients myClient = clients.get(position);
                Intent intent = new Intent(getApplicationContext(),ViewClient.class);
                intent.putExtra("fname",myClient.getFirstname());
                intent.putExtra("lname",myClient.getLastname());
                intent.putExtra("policy",myClient.getPolicy_type());
                intent.putExtra("phone",myClient.getPhone());
                intent.putExtra("date",myClient.getDate());
                intent.putExtra("company",myClient.getInsurance_co());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private ArrayList prepareData() {
        ArrayList clients = new ArrayList();
        for (int i=0;i<names.length;i++)
        {
            Clients myclients = new Clients();
            myclients.setFirstname(names[i]);
            myclients.setDate(dates[i]);
            myclients.setPolicy_type(policies[i]);
            clients.add(myclients);
        }
        return clients;
    }
}
