package com.example.demo1.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Integer quantity;
    private String size;
    private String orderStatus;

    public Orders(Long id, LocalDateTime orderDate, String orderStatus, subCategoryData product, Integer quantity, String razorpayOrderId, String size, Integer totalAmount, User user) {
        Id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.product = product;
        this.quantity = quantity;
        this.razorpayOrderId = razorpayOrderId;
        this.size = size;
        this.totalAmount = totalAmount;
        this.user = user;
    }

    public Orders(){

    };

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public subCategoryData getProduct() {
        return product;
    }

    public void setProduct(subCategoryData product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "order_date")
    @CreationTimestamp
    private LocalDateTime orderDate;
    private Integer totalAmount;
    private String razorpayOrderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private subCategoryData product;
}
