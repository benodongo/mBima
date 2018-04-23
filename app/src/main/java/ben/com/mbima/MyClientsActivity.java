package ben.com.mbima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import ben.com.mbima.Adapters.DataAdapter;
import ben.com.mbima.models.Clients;

public class MyClientsActivity extends AppCompatActivity {
    private String [] names = {"John Doe Milambo","Benson Odongo O","Miles Marsai K.","Kelvin Makau Junior",
    "Grace Michal Rad","Jack Willis RED","Rainbow Mikai","Renne Michaels Shem"};
    private String[] policies = {"Personal Accident","Health Insurance","Life Assurance","General Insurance",
            "Education Policy","Vehicle Insurance","Accident and Fire","Profit & Loss Policy"};
    private String[] dates = {"12 April 2018","2 May 2018","18 Jun 2018","3 Sep 2017",
            "12 April 2018","2 May 2018","18 Jun 2018","3 Sep 2017"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clients);
        initViews();
    }

    private void initViews() {
        RecyclerView rc = findViewById(R.id.recycler_view);
        rc.setHasFixedSize(true);
        RecyclerView.LayoutManager rl = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(rl);
        ArrayList clients = prepareData();
        DataAdapter adapter = new DataAdapter(clients,getApplicationContext());
        rc.setAdapter(adapter);
    }

    private ArrayList prepareData() {
        ArrayList clients = new ArrayList();
        for (int i=0;i<names.length;i++)
        {
            Clients myclients = new Clients();
            myclients.setName(names[i]);
            myclients.setDate(dates[i]);
            myclients.setPolicy_type(policies[i]);
            clients.add(myclients);
        }
        return clients;
    }
}
