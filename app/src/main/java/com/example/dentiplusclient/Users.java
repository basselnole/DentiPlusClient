package com.example.dentiplusclient;

public class Users {

    public String name,email,phone;

    public Users(String name, String email, String phone) {
        this.name=name;
        this.email=email;
        this.phone=phone;

    }

    public  Users(){}

    public String getName() {
        return name;
    }
}
