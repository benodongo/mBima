package ben.com.mbima;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    //view objects


    Button btn_reg, btn_login;
    android.widget.EditText ed_name,ed_phone,ed_email,ed_pass;
    ben.com.mbima.helpers.Preferences preferences;
    com.afollestad.materialdialogs.MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferences = new ben.com.mbima.helpers.Preferences(this);
        initViews();
    }

    private void initViews() {
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_email = findViewById(R.id.ed_email);
        ed_pass = findViewById(R.id.ed_password);
        btn_reg = findViewById(R.id.btn_reg);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_reg:
                retryLogin();
                break;
            case R.id.btn_login:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        }
    }
      //login
    private void retryLogin(){
        String params = null;
        try {
            params = "name="+java.net.URLEncoder.encode(ed_name.getText().toString(), "UTF-8")+
                     "&phone="+java.net.URLEncoder.encode(ed_phone.getText().toString(), "UTF-8")+
                     "&email="+java.net.URLEncoder.encode(ed_email.getText().toString(), "UTF-8")+
                    "&password="+java.net.URLEncoder.encode(ed_pass.getText().toString(),"UTF-8");
            new login().execute(ben.com.mbima.helpers.Constants.base_url+"api/register", params);
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
    private class login extends android.os.AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            configureDialog("Login","Please wait as we set up your account.We will automatically redirect you once we finish");
            dialog.show();
        }
        protected String  doInBackground(String... params) {
            return ben.com.mbima.network.NetworkHandler.post(params[0], params[1]);
        }
        protected void onPostExecute(String result) {
            dialog.dismiss();
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
                                    retryLogin();
                                }
                            };
                            ben.com.mbima.helpers.Constants.showDialog(RegisterActivity.this,"Error",object.getString("message"),"RETRY",singleButtonCallback);
                        }

                    } else {
                        com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback singleButtonCallback=new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@android.support.annotation.NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @android.support.annotation.NonNull com.afollestad.materialdialogs.DialogAction which) {
                                dialog.dismiss();
                                retryLogin();
                            }
                        };
                        ben.com.mbima.helpers.Constants.showDialog(RegisterActivity.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }else {
                com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback singleButtonCallback=new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@android.support.annotation.NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @android.support.annotation.NonNull com.afollestad.materialdialogs.DialogAction which) {
                        dialog.dismiss();
                        retryLogin();
                    }
                };
                ben.com.mbima.helpers.Constants.showDialog(RegisterActivity.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
            }

        }
    }

}
