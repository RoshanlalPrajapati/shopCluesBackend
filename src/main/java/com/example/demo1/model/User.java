package com.example.demo1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_role")
    private String userRole;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name= "email_id", unique = true, nullable = false)
    private String emailId;

    @Column(name = "mobile_no", unique = true, nullable = false)
    private Long mobileNo;

    public Long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public User() {
    }

    public Integer getUserUniqueId() {
        return userUniqueId;
    }

    public User(int id, String username, String password, String emailId, Integer userUniqueId, Long mobileNo, String userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.emailId = emailId;
        this.userUniqueId = userUniqueId;
        this.mobileNo = mobileNo;
        this.userRole = userRole;
    }

    public void setUserUniqueId(Integer userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_unique_id", unique = true)
    private Integer userUniqueId;

}
