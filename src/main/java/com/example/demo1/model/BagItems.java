package com.example.demo1.model;

import jakarta.persistence.*;

@Entity
@Table(name="bag_items")
public class BagItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private subCategoryData product;

    private String size;

    public BagItems(Integer id, subCategoryData product, String size, User user) {
        this.id = id;
        this.product = product;
        this.size = size;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public subCategoryData getProduct() {
        return product;
    }

    public void setProduct(subCategoryData product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BagItems(){}
}
