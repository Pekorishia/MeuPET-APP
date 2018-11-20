package com.fepa.meupet.model.agent.person;

import com.fepa.meupet.model.agent.animal.Animal;

import java.util.ArrayList;
import java.util.List;

public class OwnerUser extends Person {

    private List<Animal> myPets;

    public OwnerUser(String name, String phoneNumber) {
        super(name, phoneNumber);
        this.myPets = new ArrayList<>();
    }

    public void removePet(Animal animal){
        this.myPets.remove(animal);
    }

    public void addPet(Animal animal){
        this.myPets.add(animal);
    }

    public List<Animal> getMyPets() {
        return myPets;
    }

    public void setMyPets(List<Animal> myPets) {
        this.myPets = myPets;
    }
}
