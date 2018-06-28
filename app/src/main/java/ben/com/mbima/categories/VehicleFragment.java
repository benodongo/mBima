package ben.com.mbima.categories;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ben.com.mbima.Adapters.DataAdapter;
import ben.com.mbima.R;
import ben.com.mbima.ViewClient;
import ben.com.mbima.helpers.Constants;
import ben.com.mbima.helpers.Preferences;
import ben.com.mbima.helpers.RecyclerTouchListener;
import ben.com.mbima.helpers.SqliteHelper;
import ben.com.mbima.models.Clients;
import ben.com.mbima.network.NetworkHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleFragment extends Fragment {


    public VehicleFragment() {
        // Required empty public constructor
    }
    SqliteHelper sqliteHelper;
    Preferences preferences;
    MaterialDialog dialog;
    private RecyclerView rc;
    private static DataAdapter mAdapter;
    static  ArrayList<Clients> clients ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);
        sqliteHelper = new SqliteHelper(getActivity());
        preferences = new Preferences(getActivity());
        initViews(view);
        getClients();

        return view;

    }

    private void initViews(View view) {

        RecyclerView rc = view.findViewById(R.id.rv);
        rc.setHasFixedSize(true);
        RecyclerView.LayoutManager rl = new LinearLayoutManager(getActivity());
        rc.setLayoutManager(rl);
        String type = "Vehicle";
        clients = new ArrayList<>();
       // final ArrayList<Clients> clients = sqliteHelper.getSpecificClients(type);
        mAdapter = new DataAdapter(clients,getActivity());
        rc.setAdapter(mAdapter);
        rc.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rc, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Clients myClient = clients.get(position);
                Intent intent = new Intent(getActivity(),ViewClient.class);
                intent.putExtra("id",myClient.getId());
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
    //get user clients
    private void getClients(){
        String params = null;
        String type = "Vehicle";
        try {
            params = "id="+ URLEncoder.encode(preferences.getId(), "UTF-8")+
                    "&policy="+ URLEncoder.encode(type, "UTF-8")
            ;
            new myClients().execute(Constants.base_url+"api/getSpecific", params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void configureDialog(String title,String message){
        dialog=new MaterialDialog.Builder(getActivity())
                .title(title)
                .cancelable(false)
                .titleGravity(GravityEnum.CENTER)
                .widgetColorRes(R.color.colorPrimary)
                .customView(R.layout.dialog, true)
                .build();
        View view=dialog.getCustomView();
        TextView messageText=view.findViewById(R.id.message);
        messageText.setText(message);
    }
    private class myClients extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            configureDialog("please wait","fetching");
            dialog.show();
        }
        protected String  doInBackground(String... params) {
            return NetworkHandler.post(params[0], params[1]);
        }
        protected void onPostExecute(String result) {
            dialog.dismiss();
            ArrayList<Clients> myclients = new ArrayList<>();
            if (result != null) {
                try {
                    Object json = new JSONTokener(result).nextValue();
                    if (json instanceof JSONObject) {
                        final JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        if (status.equals("success") ) {
                            JSONArray clientsArr = object.getJSONArray("clients");
                            for (int i = 0; i < clientsArr.length(); i++)
                            {
                                //getting each client obj
                                JSONObject obj = clientsArr.getJSONObject(i);
                                //adding clients to arraylist
                                Clients clients1 = new Clients();
                                clients1.setId(obj.getString("id"));
                                clients1.setFirstname(obj.getString("fname"));
                                clients1.setLastname(obj.getString("lname"));
                                clients1.setInsurance_co(obj.getString("insurance_company"));
                                clients1.setPhone(obj.getString("phone"));
                                clients1.setDate(obj.getString("date"));
                                clients1.setExpiry_date(obj.getString("expiry_date"));
                                clients1.setPolicy_type(obj.getString("policy_type"));
                                clients1.setDuration("( "+obj.getString("duration" )+" cycle )");
                                clients1.setDue_time(obj.getString("due" )+" Months from now");
                                VehicleFragment.clients.add(clients1);

                            }
                            VehicleFragment.mAdapter.notifyDataSetChanged();

                        } else {
                            MaterialDialog.SingleButtonCallback singleButtonCallback=new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    getClients();
                                }
                            };
                            Constants.showDialog(getActivity(),"Error",object.getString("message"),"RETRY",singleButtonCallback);
                        }

                    } else {
                        MaterialDialog.SingleButtonCallback singleButtonCallback=new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                getClients();
                            }
                        };
                        Constants.showDialog(getActivity(),"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                MaterialDialog.SingleButtonCallback singleButtonCallback=new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        getClients();
                    }
                };
                Constants.showDialog(getContext(),"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
            }

        }
    }

}
