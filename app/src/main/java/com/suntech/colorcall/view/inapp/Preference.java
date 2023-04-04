package com.suntech.colorcall.view.inapp;

import static com.suntech.colorcall.extentions.ViewExtensionKt.mutableSetOf;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Preference {

    private SharedPreferences sharedPreferences;
    private String PREFS_ACCOUNT = "PREFS_ACCOUNT";


    private String KEY_PREMIUM = "KEY_PREMIUM"; // premium
    private String KEY_TOTAL_COIN = "KEY_TOTAL_COIN"; // coin

    private String KEY_INSTALL_APP = "KEY_INSTALL_APP";// install app

    private String KEY_BUY_ITEM ="KEY_BUY_ITEM";

    public static Preference instance;

    public static Preference buildInstance(Context context) {
        if (instance == null) {
            instance = new Preference(context);
        }
        return instance;
    }

    public Preference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_ACCOUNT, Context.MODE_PRIVATE);

    }

    public void setPremium(int value) {
        sharedPreferences.edit().putInt(KEY_PREMIUM, value).apply();
    }

    public int getPremium() {
        return sharedPreferences.getInt(KEY_PREMIUM, 0);
    }

    public void setValueCoin(int value) {
        sharedPreferences.edit().putInt(KEY_TOTAL_COIN, value).apply();
    }

    public int getValueCoin() {
        return sharedPreferences.getInt(KEY_TOTAL_COIN, 0);
    }

    public void setFistInstallApp(boolean value){
        sharedPreferences.edit().putBoolean(KEY_INSTALL_APP, value).apply();
    }
    public boolean getFistInstallApp(){
        return  sharedPreferences.getBoolean(KEY_INSTALL_APP, false);
    }
    public Set<String>  getListKeyBy() {

        return sharedPreferences.getStringSet(KEY_BUY_ITEM,mutableSetOf());
    }

    public void  setListKeyBy(Set<String> set){
        sharedPreferences.edit().putStringSet(KEY_BUY_ITEM, set).apply();
    }


}
