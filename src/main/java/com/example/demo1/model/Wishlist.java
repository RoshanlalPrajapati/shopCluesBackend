package com.example.demo1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wishlist(Integer id, subCategoryData product, User user) {
        this.id = id;
        this.product = product;
        this.user = user;
    }

    public Wishlist(){}

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private subCategoryData product;
}
