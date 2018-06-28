package ben.com.mbima;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import ben.com.mbima.helpers.Constants;
import ben.com.mbima.helpers.Preferences;
import ben.com.mbima.helpers.RecyclerTouchListener;
import ben.com.mbima.helpers.SqliteHelper;
import ben.com.mbima.models.Clients;
import ben.com.mbima.network.NetworkHandler;

public class MyClientsActivity extends AppCompatActivity {
    private String [] names = {"John Doe Milambo","Benson Odongo O","Miles Marsai K.","Kelvin Makau Junior",
    "Grace Michal Rad","Jack Willis RED","Rainbow Mikai","Renne Michaels Shem"};
    private String[] policies = {"Personal Accident","Health Insurance","Life Assurance","General Insurance",
            "Education Policy","Vehicle Insurance","Accident and Fire","Profit & Loss Policy"};
    private String[] dates = {"12 April 2018","2 May 2018","18 Jun 2018","3 Sep 2017",
            "12 April 2018","2 May 2018","18 Jun 2018","3 Sep 2017"};
    SqliteHelper sqliteHelper;
    Preferences preferences;
    MaterialDialog dialog;
    private RecyclerView rc;
    private static DataAdapter mAdapter;
     static  ArrayList<Clients> clients ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clients);
        sqliteHelper = new SqliteHelper(this);
        preferences = new Preferences(this);
        clients = new ArrayList<>();
        getClients();
        initViews();
    }

    private void initViews() {

        RecyclerView rc = findViewById(R.id.recycler_view);
        rc.setHasFixedSize(true);
        Log.e("test", "initViews: "+clients );
        RecyclerView.LayoutManager rl = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(rl);
//        final ArrayList<Clients> clients = sqliteHelper.getAll();
        mAdapter = new DataAdapter(clients,getApplicationContext());
        rc.setAdapter(mAdapter);
        rc.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rc, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Clients myClient = clients.get(position);
                Intent intent = new Intent(getApplicationContext(),ViewClient.class);
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
    //get user clients
    private void getClients(){
        String params = null;
        try {
            params = "id="+ URLEncoder.encode(preferences.getId(), "UTF-8");
            new myClients().execute(Constants.base_url+"api/getClients", params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void configureDialog(String title,String message){
        dialog=new MaterialDialog.Builder(this)
                .title(title)
                .cancelable(false)
                .titleGravity(GravityEnum.CENTER)
                .widgetColorRes(R.color.colorPrimary)
                .customView(R.layout.dialog, true)
                .build();
        View view=dialog.getCustomView();
        TextView messageText=(TextView)view.findViewById(R.id.message);
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
                                MyClientsActivity.clients.add(clients1);

                            }
                            MyClientsActivity.mAdapter.notifyDataSetChanged();

                        } else {
                            MaterialDialog.SingleButtonCallback singleButtonCallback=new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    getClients();
                                }
                            };
                            Constants.showDialog(MyClientsActivity.this,"Error",object.getString("message"),"RETRY",singleButtonCallback);
                        }

                    } else {
                        MaterialDialog.SingleButtonCallback singleButtonCallback=new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                getClients();
                            }
                        };
                        Constants.showDialog(MyClientsActivity.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
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
                Constants.showDialog(MyClientsActivity.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
            }

        }
    }
}
