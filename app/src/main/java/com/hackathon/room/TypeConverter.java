package com.hackathon.room;

import android.view.inspector.StaticInspectionCompanionProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class TypeConverter {

    @androidx.room.TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @androidx.room.TypeConverter
    public static ArrayList<Boolean> fromList(String value){
        Type listType = new TypeToken<ArrayList<Boolean>>() {}.getType();
        return new Gson().fromJson(value,listType);
    }

    @androidx.room.TypeConverter
    public static String fromArrayList(ArrayList<Boolean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}
