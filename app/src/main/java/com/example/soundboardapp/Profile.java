package com.example.soundboardapp;

import java.io.Serializable;

public class Profile implements Serializable {
    public String name;
    public String description;

    public Profile(){

    }

    public Profile(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }
}
