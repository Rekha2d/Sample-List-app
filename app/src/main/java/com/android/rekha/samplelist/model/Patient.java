package com.android.rekha.samplelist.model;

/**
 * Patient is a POJO model class for which will contain all attributes of an patient. <br>
 * <i>Created by Rekha on 7/9/2018. </i>
 */

public class Patient {

    private long id;
    private String name;
    private String age;
    private String status;

    public Patient(){

    }

    public Patient(String name, String age, String status) {
        this.name = name;
        this.age = age;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
