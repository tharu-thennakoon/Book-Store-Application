/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.model;

import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private List<CartItem> items;
    private double totalAmount;

    // Required no-arg constructor
    public Order() {}

    public Order(int id, int customerId, List<CartItem> items, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }
    public int getCustomerId() { 
        return customerId; 
    }
    public void setCustomerId(int customerId) { 
        this.customerId = customerId; 
    }
    public List<CartItem> getItems() { 
        return items; 
    }
    public void setItems(List<CartItem> items) { 
        this.items = items; 
    }
    public double getTotalAmount() { 
        return totalAmount; 
    }
    public void setTotalAmount(double totalAmount) { 
        this.totalAmount = totalAmount; 
    }
}
