package com.fepa.meupet.model.agent.person;

import com.fepa.meupet.model.agent.pet.Pet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable{

    private String email;
    private List<Pet> pets;

    public Person(String email) {
        this.email = email;
        this.pets = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
