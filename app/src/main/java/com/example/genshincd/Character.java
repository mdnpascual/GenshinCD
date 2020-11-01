package com.example.genshincd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Character {

    public int crossHairX = -1;
    public int crossHairY = -1;
    public int squareX = -1;
    public int squareY = -1;
    public float squareScale = -1;

    private String position = "";
    private SharedPreferences sharedPref = null;

    public Character(Context ctx, String position){
        sharedPref = ((Activity) ctx).getPreferences(Context.MODE_PRIVATE);
        initialize(position);
    }

    private void initialize(String position) {
        crossHairX = sharedPref.getInt(position+"crossHairX", 0);
        crossHairY = sharedPref.getInt(position+"crossHairY", 0);
        squareX = sharedPref.getInt(position+"squareX", 0);
        squareY = sharedPref.getInt(position+"squareY", 0);
        squareScale = Float.parseFloat(sharedPref.getString(position+"squareScale", "1.0"));
        this.position = position;
    }

    public void save(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(position+"crossHairX", crossHairX);
        editor.putInt(position+"crossHairY", crossHairY);
        editor.putInt(position+"squareX", squareX);
        editor.putInt(position+"squareY", squareY);
        editor.putFloat(position+squareScale, squareScale);
        editor.commit();
    }
}
