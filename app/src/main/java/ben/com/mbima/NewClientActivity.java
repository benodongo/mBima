package ben.com.mbima;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ben.com.mbima.helpers.Preferences;
import ben.com.mbima.helpers.SqliteHelper;
import ben.com.mbima.models.Clients;

public class NewClientActivity extends AppCompatActivity implements View.OnClickListener {
    EditText ed_fname,ed_lname,ed_email,ed_phone,ed_company,ed_policy_no,ed_premium;
    Spinner sp_policy_type,sp_duration;
   Button date;
    Button btn_submit;
    String [] policy = {"Vehicle","General","Personal Accident","Health Insurance","Education"};
    String [] duration ={"6 Months","12 Months","18 Months","24 Months"};
    String mPolicyType,mPolicyDuration; static  String mDate;
    SqliteHelper sqliteHelper;
    MaterialDialog dialog;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        sqliteHelper = new SqliteHelper(this);
        preferences = new Preferences(this);
        initViews();
    }

    private void initViews() {
        ed_fname = findViewById(R.id.ed_firstname);
        ed_lname = findViewById(R.id.ed_lastname);
        ed_email = findViewById(R.id.ed_email);
        ed_phone = findViewById(R.id.ed_phone);
        ed_company = findViewById(R.id.ed_insurance_company);
        ed_policy_no = findViewById(R.id.ed_policy_no);
        ed_premium = findViewById(R.id.ed_premium);
        sp_policy_type = findViewById(R.id.sp_policyType);
        sp_duration = findViewById(R.id.sp_duration);
        date = findViewById(R.id.date);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        //set spinner adapters
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,policy);
        sp_policy_type.setAdapter(adapter);
        sp_policy_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = sp_policy_type.getSelectedItemPosition();
                mPolicyType = (policy[+position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,duration);
        sp_duration.setAdapter(adapter1);
        sp_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = sp_duration.getSelectedItemPosition();
                mPolicyDuration = (policy[+position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        String mFname = ed_fname.getText().toString();
        String mLame = ed_lname.getText().toString();
        String mEmail = ed_email.getText().toString();
        String mPhone = ed_phone.getText().toString();
        String mCompany = ed_company.getText().toString();
        String mPremium = ed_premium.getText().toString();
        String mPolicyNo =ed_policy_no.getText().toString();
        sqliteHelper.addClient(new Clients(null,mFname,mLame,mEmail,mPhone,mCompany,mDate,mPolicyType,mPremium,mPolicyNo,mPolicyDuration));
        retrysubmit();
        Snackbar.make(btn_submit, "client created successfully! Please Login ", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, Snackbar.LENGTH_LONG);

    }


    public void setdDate(String dDate){}
    public void datePicker(View view){

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

           NewClientActivity.mDate=formattedDate;

       }
   }
   //submit to remote db
   //login
   private void retrysubmit(){
       String params = null;
       try {
           params = "first_name="+java.net.URLEncoder.encode(ed_fname.getText().toString(), "UTF-8")+
                   "&last_name="+java.net.URLEncoder.encode(ed_lname.getText().toString(), "UTF-8")+
                   "&phone="+java.net.URLEncoder.encode(ed_phone.getText().toString(), "UTF-8")+
                   "&email="+java.net.URLEncoder.encode(ed_email.getText().toString(), "UTF-8")+
                   "&insurance_company="+java.net.URLEncoder.encode(ed_company.getText().toString(), "UTF-8")+
                   "&policy_type="+java.net.URLEncoder.encode(mPolicyType, "UTF-8")+
                   "&date="+java.net.URLEncoder.encode(mDate, "UTF-8")+
                   "&duration="+java.net.URLEncoder.encode(mPolicyDuration, "UTF-8")+
                   "&policy_number="+java.net.URLEncoder.encode(ed_policy_no.getText().toString(), "UTF-8")+
                   "&premium="+java.net.URLEncoder.encode(ed_premium.getText().toString(), "UTF-8")+
                   "&agent_id="+java.net.URLEncoder.encode(preferences.getId(),"UTF-8");
           new NewClientActivity.submit().execute(ben.com.mbima.helpers.Constants.base_url+"api/newClient", params);
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
                            ben.com.mbima.helpers.Constants.showDialog(NewClientActivity.this,"Error",object.getString("message"),"RETRY",singleButtonCallback);
                        }

                    } else {
                        com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback singleButtonCallback=new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@android.support.annotation.NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @android.support.annotation.NonNull com.afollestad.materialdialogs.DialogAction which) {
                                dialog.dismiss();
                               retrysubmit();
                            }
                        };
                        ben.com.mbima.helpers.Constants.showDialog(NewClientActivity.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
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
                ben.com.mbima.helpers.Constants.showDialog(NewClientActivity.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
            }

        }
    }

}
