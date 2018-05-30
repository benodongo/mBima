package ben.com.mbima.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ben.com.mbima.models.Clients;
import ben.com.mbima.models.User;

public class SqliteHelper extends SQLiteOpenHelper {


    public SqliteHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create Table when oncreate gets called
        sqLiteDatabase.execSQL(Constants.SQL_TABLE_USERS);
        sqLiteDatabase.execSQL(Constants.SQL_TABLE_CLIENTS);
        sqLiteDatabase.execSQL(Constants.SQL_TABLE_PREMIUMS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table to create new one if database version updated
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + Constants.TABLE_USERS);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + Constants.TABLE_CLIENTS);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + Constants.TABLE_PREMIUMS);
    }

    //using this method we can add users to user table
    public void addUser(User user) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put username in  @values
        values.put(Constants.KEY_USER_NAME, user.userName);
        //Put username in  @values
        values.put(Constants.KEY_USER_NAME, user.userName);

        //Put phone in  @values
        values.put(Constants.KEY_USER_PHONE, user.phone);

        //Put password in  @values
        values.put(Constants.KEY_PASSWORD, user.password);

        // insert row
        long todo_id = db.insert(Constants.TABLE_USERS, null, values);
    }

    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_USERS,// Selecting Table
                new String[]{Constants.KEY_ID, Constants.KEY_USER_NAME,Constants.KEY_USER_PHONE, Constants.KEY_EMAIL,
                        Constants.KEY_PASSWORD},//Selecting columns want to query
                Constants.KEY_EMAIL + "=?",
                new String[]{user.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            //if cursor has value then in user database there is user associated with this given email
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3),cursor.getString(4));

            //Match both passwords check they are same or not
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }

        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_USERS,// Selecting Table
                new String[]{Constants.KEY_ID, Constants.KEY_USER_NAME,Constants.KEY_USER_PHONE, Constants.KEY_EMAIL,
                        Constants.KEY_PASSWORD},//Selecting columns want to query
                Constants.KEY_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }
    //add new client
    public void addClient(Clients clients)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.FNAME,clients.getFirstname());
        values.put(Constants.LNAME,clients.getLastname());
        values.put(Constants.KEY_EMAIL,clients.getEmail());
        values.put(Constants.KEY_USER_PHONE,clients.getPhone());
        values.put(Constants.INSURANCE_COMPANY,clients.getInsurance_co());
        values.put(Constants.POLICY,clients.getPolicy_type());
        values.put(Constants.POLICY_DATE,clients.getDate());
        values.put(Constants.POLICY_DURATION,clients.getDuration());
        values.put(Constants.POLICY_NO,clients.getPolicy_no());
        values.put(Constants.PREMIUM,clients.getPremium());
        // insert row
        long todo_id = db.insert(Constants.TABLE_CLIENTS, null, values);
    }
    //fetch all clients
    public ArrayList getAll()
    {
        ArrayList clients = new ArrayList();
        String query = "SELECT * FROM "+Constants.TABLE_CLIENTS;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        while (c.moveToNext())
        {
            Clients clients1 = new Clients();
            clients1.setFirstname(c.getString(c.getColumnIndex(Constants.FNAME)));
            clients1.setLastname(c.getString(c.getColumnIndex(Constants.LNAME)));
            clients1.setInsurance_co(c.getString(c.getColumnIndex(Constants.INSURANCE_COMPANY)));
            clients1.setPhone(c.getString(c.getColumnIndex(Constants.KEY_USER_PHONE)));
            clients1.setDate(c.getString(c.getColumnIndex(Constants.POLICY_DATE)));
            clients1.setPolicy_type(c.getString(c.getColumnIndex(Constants.POLICY)));
            clients1.setDuration(c.getString(c.getColumnIndex(Constants.POLICY_DURATION)));
            clients.add(clients1);
        }
        return clients;
    }
    //getClients based on type
    public ArrayList getSpecificClients(String type)
    {
        ArrayList clients = new ArrayList();
        String query = "SELECT * FROM "+Constants.TABLE_CLIENTS+ " WHERE "+Constants.POLICY + " = ?";
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c = db.rawQuery(query,new String[]{type});
        while (c.moveToNext())
        {
            Clients clients1 = new Clients();
            clients1.setFirstname(c.getString(c.getColumnIndex(Constants.FNAME)));
            clients1.setLastname(c.getString(c.getColumnIndex(Constants.LNAME)));
            clients1.setInsurance_co(c.getString(c.getColumnIndex(Constants.INSURANCE_COMPANY)));
            clients1.setPhone(c.getString(c.getColumnIndex(Constants.KEY_USER_PHONE)));
            clients1.setDate(c.getString(c.getColumnIndex(Constants.POLICY_DATE)));
            clients1.setPolicy_type(c.getString(c.getColumnIndex(Constants.POLICY)));
            clients1.setDuration(c.getString(c.getColumnIndex(Constants.POLICY_DURATION)));
            clients.add(clients1);
        }
        return clients;
    }
    //add new PREMIUM
    public void addPremium(Clients clients)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.INSURANCE_COMPANY,clients.getInsurance_co());
        values.put(Constants.POLICY,clients.getPolicy_type());
        values.put(Constants.POLICY_DATE,clients.getDate());
        values.put(Constants.POLICY_NO,clients.getPolicy_no());
        values.put(Constants.PREMIUM,clients.getPremium());
        values.put(Constants.CLIENT,clients.getFirstname());
        // insert row
        long todo_id = db.insert(Constants.TABLE_PREMIUMS, null, values);
    }
    //getPremiums per client
    public ArrayList getPremiums(String name)
    {
        ArrayList clients = new ArrayList();
        String query = "SELECT * FROM "+Constants.TABLE_PREMIUMS+ " WHERE "+Constants.CLIENT + " = ?";
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c = db.rawQuery(query,new String[]{name});
        while (c.moveToNext())
        {
            Clients clients1 = new Clients();

            clients1.setInsurance_co(c.getString(c.getColumnIndex(Constants.INSURANCE_COMPANY)));
            clients1.setPhone(c.getString(c.getColumnIndex(Constants.POLICY_NO)));
            clients1.setDate(c.getString(c.getColumnIndex(Constants.POLICY_DATE)));
            clients1.setPolicy_type(c.getString(c.getColumnIndex(Constants.POLICY)));
            clients1.setPremium(c.getString(c.getColumnIndex(Constants.PREMIUM)));
            clients.add(clients1);
        }
        return clients;
    }
}

