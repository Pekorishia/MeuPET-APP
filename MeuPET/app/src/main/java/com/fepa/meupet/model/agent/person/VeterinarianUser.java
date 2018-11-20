package com.fepa.meupet.model.agent.person;

import com.fepa.meupet.model.agent.pet.Pet;

import java.util.ArrayList;
import java.util.List;

public class VeterinarianUser extends Person {

    private List<Pet> myPets;
    private List<Pet> myPatients;

    public VeterinarianUser(String name, String phoneNumber) {
        super(name, phoneNumber);
        this.myPets = new ArrayList<>();
        this.myPatients = new ArrayList<>();
    }

    public void removePet(Pet pet){
        this.myPets.remove(pet);
    }

    public void removePatient(Pet pet){
        this.myPets.remove(pet);
    }

    public void addPet(Pet pet){
        this.myPets.add(pet);
    }

    public void addPatient(Pet pet){
        this.myPets.add(pet);
    }

    public List<Pet> getMyPets() {
        return myPets;
    }

    public void setMyPets(List<Pet> myPets) {
        this.myPets = myPets;
    }

    public List<Pet> getMyPatients() {
        return myPatients;
    }

    public void setMyPatients(List<Pet> myPatients) {
        this.myPatients = myPatients;
    }
}
