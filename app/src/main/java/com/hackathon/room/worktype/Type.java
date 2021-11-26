package com.hackathon.room.worktype;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Type {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("worktype")
    @Expose
    private String bigtype;
    @SerializedName("bigtype")
    @Expose
    private String smalltype;
    @SerializedName("smalltype")



    public String getBigtype() {
        return bigtype;
    }

    public void setBigtype(String bigtype) {
        this.bigtype = bigtype;
    }

    public String getSmalltype(){
        return smalltype;
    }

    public void setSmalltype(String smalltype){
        this.smalltype = smalltype;
    }



}