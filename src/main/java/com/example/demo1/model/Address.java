package com.example.demo1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressLine1;
    private String city;
    private String state;

    private String name;
    private Long phoneNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(Long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address(String addressLine1, String city, String country, Long id, String pincode, String state, User user, String name, Long phoneNo) {
        this.addressLine1 = addressLine1;
        this.city = city;
        this.country = country;
        this.id = id;
        this.pincode = pincode;
        this.state = state;
        this.user = user;
        this.phoneNo = phoneNo;
        this.name = name;
    }

    public Address(){}

    private String pincode;
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
