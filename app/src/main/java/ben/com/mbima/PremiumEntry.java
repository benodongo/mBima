package ben.com.mbima;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ben.com.mbima.helpers.Preferences;
import ben.com.mbima.helpers.SqliteHelper;
import ben.com.mbima.models.Clients;

public class PremiumEntry extends AppCompatActivity implements View.OnClickListener {
    SqliteHelper sqliteHelper;
    MaterialDialog dialog;
    EditText ed_policy_no,ed_premium;
    Button btn_submit1,date;
    String name,company,policy,clientID;
    static String mDate;

    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sqliteHelper = new SqliteHelper(this);
        preferences = new Preferences(this);
        initViews();

    }
    private void initViews() {
        clientID = getIntent().getExtras().getString("clientID");
        name = getIntent().getExtras().getString("name");
        company = getIntent().getExtras().getString("company");
        policy = getIntent().getExtras().getString("policy");

        ed_policy_no = findViewById(R.id.ed_policy_no);
        ed_premium = findViewById(R.id.ed_premium);
        date = findViewById(R.id.date);
        btn_submit1 = findViewById(R.id.btn_submit1);
        btn_submit1.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        String mPremium = ed_premium.getText().toString();
        String mPolicyNo =ed_policy_no.getText().toString();
      sqliteHelper.addPremium(new Clients(null,name,company,mDate,policy,mPremium,mPolicyNo));
      retrysubmit();
        Snackbar.make(btn_submit1, "client created successfully!  ", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, Snackbar.LENGTH_LONG);

    }


    public void setdDate(String dDate){}
    public void datePicker2(View view){

        // Initialize a new date picker dialog fragment
        DialogFragment dFragment = new DatePickerFragment();

        // Show the date picker dialog fragment
        dFragment.show(getSupportFragmentManager(), "Date Picker");
    }

    //DATEPICKER DIALOG
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            /*
                Initialize a new DatePickerDialog

                DatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callBack,
                    int year, int monthOfYear, int dayOfMonth)
             */
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),this,year,month,day);
            return  dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style and locale
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String formattedDate = df.format(chosenDate);

            PremiumEntry.mDate=formattedDate;

        }
    }
    //submit to remote db
    private void retrysubmit(){
        String params = null;
        try {
            params = "client_id="+java.net.URLEncoder.encode(clientID, "UTF-8")+
                    "&insurance_company="+java.net.URLEncoder.encode(company, "UTF-8")+
                    "&date="+java.net.URLEncoder.encode(mDate, "UTF-8")+
                    "&policy_number="+java.net.URLEncoder.encode(ed_policy_no.getText().toString(), "UTF-8")+
                    "&premium="+java.net.URLEncoder.encode(ed_premium.getText().toString(), "UTF-8")+
                    "&agent_id="+java.net.URLEncoder.encode(preferences.getId(),"UTF-8");
            //TODO
            new PremiumEntry.submit().execute(ben.com.mbima.helpers.Constants.base_url+"api/newpremium", params);
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void configureDialog(String title,String message){
        dialog=new com.afollestad.materialdialogs.MaterialDialog.Builder(this)
                .title(title)
                .cancelable(false)
                .titleGravity(com.afollestad.materialdialogs.GravityEnum.CENTER)
                .widgetColorRes(R.color.colorPrimary)
                .customView(R.layout.dialog, true)
                .build();
        View view=dialog.getCustomView();
        android.widget.TextView messageText=(android.widget.TextView)view.findViewById(R.id.message);
        messageText.setText(message);
    }
    private class submit extends android.os.AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            configureDialog("Client","Adding client");
//            dialog.show();
        }
        protected String  doInBackground(String... params) {
            return ben.com.mbima.network.NetworkHandler.post(params[0], params[1]);
        }
        protected void onPostExecute(String result) {
//            dialog.dismiss();
            if (result != null) {
                try {
                    Object json = new org.json.JSONTokener(result).nextValue();
                    if (json instanceof org.json.JSONObject) {
                        final org.json.JSONObject object = new org.json.JSONObject(result);
                        String status = object.getString("status");
                        if (status.equals("success") ) {


                            startActivity(new Intent(getApplicationContext(),NavActivity.class));
                            finish();


                        } else {
                            com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback singleButtonCallback=new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@android.support.annotation.NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @android.support.annotation.NonNull com.afollestad.materialdialogs.DialogAction which) {
                                    dialog.dismiss();
                                    retrysubmit();
                                }
                            };
                            ben.com.mbima.helpers.Constants.showDialog(PremiumEntry.this,"Error",object.getString("message"),"RETRY",singleButtonCallback);
                        }

                    } else {
                        com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback singleButtonCallback=new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@android.support.annotation.NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @android.support.annotation.NonNull com.afollestad.materialdialogs.DialogAction which) {
                                dialog.dismiss();
                                retrysubmit();
                            }
                        };
                        ben.com.mbima.helpers.Constants.showDialog(PremiumEntry.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }else {
                com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback singleButtonCallback=new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@android.support.annotation.NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @android.support.annotation.NonNull com.afollestad.materialdialogs.DialogAction which) {
                        dialog.dismiss();
                        retrysubmit();
                    }
                };
                ben.com.mbima.helpers.Constants.showDialog(PremiumEntry.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
            }

        }
    }

}
