package com.proyecto.e_commerce_java.domain.Entities;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String image;

    public Product(int id, String name, double price, int stock) {
        this(id, name, price, stock, null);
    }

    public Product(int id, String name, double price, int stock, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getImage() {
        return image;
    }
}
