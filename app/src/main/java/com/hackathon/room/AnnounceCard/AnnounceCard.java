package com.hackathon.room.AnnounceCard;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity
public class AnnounceCard {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("id")

    @Expose
    private String userName;
    @SerializedName("user_name")

    @Expose
    private String email;
    @SerializedName("email")

    @Expose
    private String location;
    @SerializedName("location")

    @Expose
    private String address;
    @SerializedName("address")


    @Expose
    private double lng;
    @SerializedName("lng")

    @Expose
    private double lat;
    @SerializedName("lat")

    @Expose
    private int payment;
    @SerializedName("payment")

    @Expose
    private String title;
    @SerializedName("title")

    @Expose
    private String category;
    @SerializedName("category")

    @Expose
    private String desc;
    @SerializedName("desc")

    @Expose
    private String date;
    @SerializedName("startTime")


    @Expose
    private String startTime;
    @SerializedName("startTime")

    @Expose
    private String finishTime;
    @SerializedName("finishTime")

    @Expose
    private String status;
    @SerializedName("status")

    @Expose
    private ArrayList<Boolean> qualify;
    @SerializedName("qualify")

    @Expose
    private int state;
    @SerializedName("state")

    @Expose
    private int level;
    @SerializedName("level")

    @Expose
    private double score;
    @SerializedName("state")

    @Expose
    private int workFlag;
    @SerializedName("workFlag")


    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }


    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLat() {
        return lat;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
    public int getPayment() {
        return payment;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setDesc(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }

    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
    public String getFinishTime() {
        return finishTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setQualify(ArrayList<Boolean> qualify) {this.qualify =  qualify;}
    public ArrayList<Boolean> getQualify() {
        return qualify;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }

    public void setScore(double score) {
        this.score = score;
    }
    public double getScore() {
        return score;
    }

    public void setWorkFlag(int workFlag) {
        this.workFlag = workFlag;
    }
    public int getWorkFlag() {
        return workFlag;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }
}
