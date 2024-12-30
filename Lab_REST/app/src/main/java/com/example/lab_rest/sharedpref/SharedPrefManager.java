package com.example.lab_rest.sharedpref;


import static okhttp3.internal.Internal.instance;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lab_rest.model.Users;

import okhttp3.internal.Internal;

public class SharedPrefManager {


    //the constants
    private static final String SHARED_PREF_NAME = "bookstoresharedpref";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_TOKEN = "keytoken";
    private static final String KEY_ROLE = "keyrole";

    private final Context mCtx;

    public SharedPrefManager(Context context) {

        mCtx = context;


    }

    /**
     * method to let the user login
     * this method will store the user data in shared preferences
     *
     * @param users
     */
    public void storeUser(Users users) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, users.getUSER_ID());
        editor.putString(KEY_USERNAME, users.getUSER_USERNAME());
        editor.putString(KEY_EMAIL, users.getUSER_EMAIL());
        editor.putString(KEY_TOKEN, users.getUSER_TOKEN());
        editor.putString(KEY_ROLE, users.getUSER_TOKEN());
        editor.apply();
    }


    /**
     * this method will checker whether user is already logged in or not.
     * return True if already logged in
     */

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    /**
     * this method will give the information of logged in user, retrieved from SharedPreferences
     */
    public Users getUsers() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Users users = new Users();
        users.setUSER_ID(sharedPreferences.getInt(KEY_ID, -1));
        users.setUSER_USERNAME(sharedPreferences.getString(KEY_USERNAME, null));
        users.setUSER_EMAIL(sharedPreferences.getString(KEY_EMAIL, null));
        users.setUSER_TOKEN(sharedPreferences.getString(KEY_TOKEN, null));
        users.setUSER_ROLE(sharedPreferences.getString(KEY_ROLE, null));

        return users;
    }
    public String getUsersRole() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ROLE, null);
    }



    public void clear() {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


    /**
     * this method will logout the user. clear the SharedPreferences
     */
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
