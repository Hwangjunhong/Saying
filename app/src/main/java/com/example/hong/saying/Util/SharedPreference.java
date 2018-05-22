package com.example.hong.saying.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {




    public SharedPreference() {
        super();
    }

    private final static String PREF_NAME = "myapp.pref";
    private final static String COOKIE_NAME = "myapp.pref";



    public void put(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.apply();
    }
    public void putToken(Context context, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("fcmToken", value);
        editor.apply();
    }
    public void put(Context context,String key, long value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.apply();
    }
    public void put(Context context, String key, HashSet<String> value){
        SharedPreferences pref = context.getSharedPreferences(COOKIE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key,value);
        editor.apply();
    }
    public void put(Context context,String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }
    public void putFirstOpen(Context context,String key , boolean value){
        SharedPreferences pref = context.getSharedPreferences("firstopen",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }
    public boolean getFisrtOpen(Context context,String key,boolean dftValue){
        SharedPreferences pref = context.getSharedPreferences("firstopen",
                MODE_PRIVATE);

        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }
    public void put(Context context,String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.apply();
    }
    public void remove(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
    public void removeCookie(Context context){
        SharedPreferences pref = context.getSharedPreferences(COOKIE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public String getValue(Context context,String key, String dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);

        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }
    public String getValueToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);

        try {
            return pref.getString("fcmToken", "");
        } catch (Exception e) {
            return "";
        }

    }
    public Set<String> getValue(Context context, String key, HashSet<String> value){
        SharedPreferences pref = context.getSharedPreferences(COOKIE_NAME,MODE_PRIVATE);
        try {
            return pref.getStringSet(key,value);
        }catch (Exception e){
            return value;
        }
    }
    public int getValue(Context context,String key, int dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);

        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }
    public long getValue(Context context,String key, long dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);

        try {
            return pref.getLong(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public boolean getValue(Context context ,String key, boolean dftValue) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                MODE_PRIVATE);

        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public void storeList(Context context, String key, List countries) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(countries);
        editor.putString(key, jsonFavorites);
        editor.apply();
    }

    public ArrayList<String> loadList(Context context, String key) {

        SharedPreferences pref;
        List<String> favorites;
        pref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (pref.contains(key)) {
            String jsonFavorites = pref.getString(key, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites, String[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;
        return (ArrayList<String>) favorites;
    }

    public void addList(Context context, String key, String country) {
        List<String> favorites = loadList(context,key);
        if (favorites == null)
            favorites = new ArrayList<>();

        if (favorites.contains(country)) {

            favorites.remove(country);

        }
        favorites.add(country);

        storeList(context,key, favorites);

    }

    public void removeList(Context context, String key, String country) {
        ArrayList favorites = loadList(context,key);
        if (favorites != null) {
            favorites.remove(country);
            storeList(context, key, favorites);
        }
    }

    public void deleteList(Context context,String key) {
        ArrayList<String> list = loadList(context,key);
        if (list != null) {
            list.clear();
        }
        storeList(context,key,list);
    }
}