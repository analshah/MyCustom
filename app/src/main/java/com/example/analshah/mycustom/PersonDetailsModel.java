package com.example.analshah.mycustom;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by analll on 17-03-2017.
 */

public class PersonDetailsModel extends RealmObject {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @PrimaryKey

    private int id;
    private String name;
    private String email;
    private String address;
    private int age;
}
