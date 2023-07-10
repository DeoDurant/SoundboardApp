package com.example.soundboardapp;

public class Soundboard {

    public String name;
    public String link;

    public Soundboard(){

    }

    public Soundboard(String name, String link){
        this.name = name;
        this.link = link;
    }

    public String getName(){
        return this.name;
    }

    public String getLink(){
        return this.link;
    }

}
