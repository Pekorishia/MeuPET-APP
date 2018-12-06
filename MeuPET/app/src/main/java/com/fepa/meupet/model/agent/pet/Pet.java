package com.fepa.meupet.model.agent.pet;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.Date;

public class Pet implements Serializable {

    private float age;      // year
    private int height;     // cm
    private boolean sex;    // M - false F - true
    private float weight;   // kg

    private String name;
    private String breed;
    private Date birthday;
    private ImageView photo;

    //    private Veterinarian vet;
    //    private Notification notification;
    //    private List<Module> modules;

    public Pet(){}

    public Pet(String name) {
        this.name = name;
    }

    public float getAge() {
        return age;
    }

    public void setAge(float age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }
}
