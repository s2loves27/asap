package com.hackathon.room.User;

import android.media.Image;
import android.provider.ContactsContract;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("id")

    @Expose
    private String email;
    @SerializedName("email")

    @Expose
    private String name;
    @SerializedName("name")

    @Expose
    private String password;
    @SerializedName("password")

    @Expose
    private String gender;
    @SerializedName("gender")

    @Expose
    private int age;
    @SerializedName("age")

    @Expose
    private String image;
    @SerializedName("img")

    @Expose
    private int identified;
    @SerializedName("identified")

    @Expose
    private double score;
    @SerializedName("score")

    @Expose
    private int money;
    @SerializedName("money")

    @Expose
    private int blocked;
    @SerializedName("blocked")

    @Expose
    private String phone;
    @SerializedName("phone")

    @Expose
    private String place;
    @SerializedName("place")

    @Expose
    private String address;
    @SerializedName("address")


    @Expose
    private double lng;
    @SerializedName("lng")

    @Expose
    private double lat;
    @SerializedName("lat")



    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGender() {
        return gender;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return age;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

    public void setIdentified(int identified) {
        this.identified = identified;
    }
    public int getIdentified() {
        return identified;
    }

    public void setScore(double score) { this.score = score; }
    public double getScore() { return score; }

    public void setMoney(int money) {
        this.money = money;
    }
    public int getMoney() {
        return money;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }
    public int getBlocked() {
        return blocked;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
        public String getPhone() {
            return phone;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    public String getPlace() {
        return place;
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



}
