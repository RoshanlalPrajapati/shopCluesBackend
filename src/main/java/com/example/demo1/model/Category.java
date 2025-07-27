package com.example.demo1.model;


import jakarta.persistence.*;

@Entity
@Table(name = "product_sub_products")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String header;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getHeader() {
        return header;
    }

    public String getSubCategory() {
        return subCategory;
    }

    @Column(name = "sub_category")
    private String subCategory;
}
