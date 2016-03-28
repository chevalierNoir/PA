/**
 * Created by bowenshi on 25/03/16.
 */
package com.example.bowenshi.intersaclay;


public class PersonalInformation {
    private int id;
    private String name;
    private String selfDescription;

    public PersonalInformation(){}

    public PersonalInformation(String un, String description){
        super();
        name=un;
        selfDescription=description;
    }

    public int getId(){
        return id;
    }

    public void setId(int newId){
        this.id=newId;
    }

    public void setSelfDescription(String d){
        this.selfDescription=d;
    }

    public String getSelfDescription(){
        return this.selfDescription;
    }

    public void setName(String d){
        this.name=d;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "User [id=" + id + ", selfDescription=" + selfDescription + "]";
    }
}
