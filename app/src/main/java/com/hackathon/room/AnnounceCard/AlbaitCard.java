package com.hackathon.room.AnnounceCard;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class AlbaitCard {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("id")

    @Expose
    private String title;
    @SerializedName("title")

    @Expose
    private String location;
    @SerializedName("location")


    @Expose
    private int payment;
    @SerializedName("payment")


    @Expose
    private String category;
    @SerializedName("category")

    @Expose
    private String desc;
    @SerializedName("desc")

    @Expose
    private String userName;
    @SerializedName("user_name")

    @Expose
    private String startTime;
    @SerializedName("startTime")

    @Expose
    private String endTime;
    @SerializedName("endTime")


    @Expose
    private int workFlag;
    @SerializedName("workFlag")

    @Expose
    private int level;
    @SerializedName("level")

    @Expose
    private double grade;
    @SerializedName("grade")

    @Expose
    private String status;
    @SerializedName("status")

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
    public int getPayment() {
        return payment;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setDesc(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getEndTime() {
        return endTime;
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

    public void setGrade(double grade) {
        this.grade = grade;
    }
    public double getGrade() {
        return grade;
    }




    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

}
