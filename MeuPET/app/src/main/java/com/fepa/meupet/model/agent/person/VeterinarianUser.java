package com.fepa.meupet.model.agent.person;

import com.fepa.meupet.model.agent.animal.Animal;

import java.util.ArrayList;
import java.util.List;

public class VeterinarianUser extends Person {

    private List<Animal> myPets;
    private List<Animal> myPatients;

    public VeterinarianUser(String name, String phoneNumber) {
        super(name, phoneNumber);
        this.myPets = new ArrayList<>();
        this.myPatients = new ArrayList<>();
    }

    public void removePet(Animal animal){
        this.myPets.remove(animal);
    }

    public void removePatient(Animal animal){
        this.myPets.remove(animal);
    }

    public void addPet(Animal animal){
        this.myPets.add(animal);
    }

    public void addPatient(Animal animal){
        this.myPets.add(animal);
    }

    public List<Animal> getMyPets() {
        return myPets;
    }

    public void setMyPets(List<Animal> myPets) {
        this.myPets = myPets;
    }

    public List<Animal> getMyPatients() {
        return myPatients;
    }

    public void setMyPatients(List<Animal> myPatients) {
        this.myPatients = myPatients;
    }
}
