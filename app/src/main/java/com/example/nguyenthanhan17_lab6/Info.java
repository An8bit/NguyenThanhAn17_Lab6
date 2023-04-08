package com.example.nguyenthanhan17_lab6;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Info implements Serializable, Comparable<Info> {
    int id;
    String fname;
    String lname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    String birthday;
    String image;
    String phone;
    String mail;

    public void setBirthday(String birthday){
        this.birthday=birthday;
    }

    public Info(int id, String fname, String lname, String image, String phone, String mail,String birthday) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.image = image;
        this.phone = phone;
        this.mail = mail;
        this.birthday=birthday;

    }



    @Override
    public int compareTo(Info info) {
       if(fname.compareToIgnoreCase(info.fname)==0){
           return lname.compareToIgnoreCase(info.lname);
       }
       return fname.compareToIgnoreCase(info.fname);
    }

}
