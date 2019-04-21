package com.fepa.meupet.model.agent.pet;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.Date;

public class Pet extends Object implements Serializable {

    private String age;      // year
    private String height;   // cm
    private String weight;   // kg

    private String sex;
    private String name;
    private String breed;
    private String photoPath;
    private Date birthday;

    //    private Veterinarian vet;
    //    private Notification notification;
    //    private List<Module> modules;

    public Pet(){}

    public Pet(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
