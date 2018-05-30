package ben.com.mbima.categories;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ben.com.mbima.Adapters.DataAdapter;
import ben.com.mbima.R;
import ben.com.mbima.ViewClient;
import ben.com.mbima.helpers.RecyclerTouchListener;
import ben.com.mbima.helpers.SqliteHelper;
import ben.com.mbima.models.Clients;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleFragment extends Fragment {


    public VehicleFragment() {
        // Required empty public constructor
    }
    SqliteHelper sqliteHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);
        initViews(view);
        return view;

    }

    private void initViews(View view) {
        sqliteHelper = new SqliteHelper(getActivity());
        RecyclerView rc = view.findViewById(R.id.rv);
        rc.setHasFixedSize(true);
        RecyclerView.LayoutManager rl = new LinearLayoutManager(getActivity());
        rc.setLayoutManager(rl);
        String type = "Vehicle";
        final ArrayList<Clients> clients = sqliteHelper.getSpecificClients(type);
        DataAdapter adapter = new DataAdapter(clients,getActivity());
        rc.setAdapter(adapter);
        rc.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rc, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Clients myClient = clients.get(position);
                Intent intent = new Intent(getActivity(),ViewClient.class);
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

}
