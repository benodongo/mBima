package ben.com.mbima;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ben.com.mbima.helpers.Constants;
import ben.com.mbima.helpers.Preferences;
import ben.com.mbima.network.NetworkHandler;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button signin,register;
    MaterialDialog dialog;
    private EditText ed_email,ed_pass;
    Preferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

    }
    private void initViews()
    {
        ed_email = findViewById(R.id.ed_email);
        ed_pass = findViewById(R.id.ed_pass);
        signin = findViewById(R.id.btn_signin);
        signin.setOnClickListener(this);
        register = findViewById(R.id.btn_register);
        register.setOnClickListener(this);
        preferences=new Preferences(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_signin:
                retryLogin();
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        }
    }
    //login
    private void retryLogin(){
        String params = null;
        try {
            params = "email="+URLEncoder.encode(ed_email.getText().toString(), "UTF-8")+
                    "&password="+URLEncoder.encode(ed_pass.getText().toString(),"UTF-8");
            new login().execute(Constants.base_url+"api/login", params);
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
    private class login extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            configureDialog("Login","Please wait as we set up your account.We will automatically redirect you once we finish");
            dialog.show();
        }
        protected String  doInBackground(String... params) {
            return NetworkHandler.post(params[0], params[1]);
        }
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result != null) {
                try {
                    Object json = new JSONTokener(result).nextValue();
                    if (json instanceof JSONObject) {
                        final JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        if (status.equals("success") ) {
                            preferences.setId(object.getString("id"));
                            preferences.setName(object.getString("name"));
                            preferences.setEmail(object.getString("email"));
                            preferences.setPhone(object.getString("phone"));
                            preferences.setIsLoggedin(true);


                            startActivity(new Intent(getApplicationContext(),NavActivity.class));
                            finish();


                        } else {
                            MaterialDialog.SingleButtonCallback singleButtonCallback=new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    retryLogin();
                                }
                            };
                            Constants.showDialog(LoginActivity.this,"Error",object.getString("message"),"RETRY",singleButtonCallback);
                        }

                    } else {
                        MaterialDialog.SingleButtonCallback singleButtonCallback=new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                retryLogin();
                            }
                        };
                        Constants.showDialog(LoginActivity.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                MaterialDialog.SingleButtonCallback singleButtonCallback=new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        retryLogin();
                    }
                };
                Constants.showDialog(LoginActivity.this,"Error",getResources().getString(R.string.error),"RETRY",singleButtonCallback);
            }

        }
    }
}
