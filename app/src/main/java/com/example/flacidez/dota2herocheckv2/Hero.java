package com.example.flacidez.dota2herocheckv2;

public class Hero {

    String name;
    String id;
    String localized_name;
    String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalized_name() {
        return localized_name;
    }

    public void setLocalized_name(String localized_name) {
        this.localized_name = localized_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Hero() {
    }

    public Hero(String name, String id, String localized_name) {
        this.name = name;
        this.id = id;
        this.localized_name = localized_name;
        this.picture ="http://cdn.dota2.com/apps/dota2/images/heroes/" + name.substring(14) +"_lg.png";

    }
}
