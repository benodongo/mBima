package ben.com.mbima.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private Context _context;
    public static final String PREFS_NAME = "mbima";
    SharedPreferences settings;
    public Preferences(Context context) {
        _context = context;
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }
    public String getToken() {
        return settings.getString("token", "");
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", token);
        editor.commit();
    }
    public boolean isLoggedin(){
        return settings.getBoolean("isLoggedin", false);
    }
    public void setIsLoggedin(boolean status){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isLoggedin", status);
        editor.commit();
    }
    public String getName() {
        return settings.getString("name", "Jack Wilson");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("name", name);
        editor.commit();
    }
    public String getId() {
        return settings.getString("id", "");
    }

    public void setId(String id) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("id", id);
        editor.commit();
    }

    public String getEmail() {
        return settings.getString("email", "jackwilson@gmail.com");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", email);
        editor.commit();
    }
    public String getPhone() {
        return settings.getString("phone", "+254 705 361 244");
    }

    public void setPhone(String phone) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("phone", phone);
        editor.commit();
    }
    public String getInstanceId() {
        return settings.getString("InstanceId", "");
    }

    public void setInstanceId(String id) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("InstanceId", id);
        editor.commit();
    }

}
