package com.example.aplicacionesmoviles.model;

public class User {

    public int id;
    public String name;
    public String lastName;
    public String email;
    public String country;
    public String phone;
    public String password;

    public User(int id, String name, String lastName, String email, String country, String phone, String password) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.phone = phone;
        this.password = password;
    }
    public User(String name, String lastName, String email, String country, String phone, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.phone = phone;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
