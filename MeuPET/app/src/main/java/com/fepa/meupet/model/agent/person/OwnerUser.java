package com.fepa.meupet.model.agent.person;

import com.fepa.meupet.model.agent.pet.Pet;

import java.util.ArrayList;
import java.util.List;

public class OwnerUser extends Person {

    private List<Pet> myPets;

    public OwnerUser(String name, String phoneNumber) {
        super(name, phoneNumber);
        this.myPets = new ArrayList<>();
    }

    public void removePet(Pet pet){
        this.myPets.remove(pet);
    }

    public void addPet(Pet pet){
        this.myPets.add(pet);
    }

    public List<Pet> getMyPets() {
        return myPets;
    }

    public void setMyPets(List<Pet> myPets) {
        this.myPets = myPets;
    }
}
