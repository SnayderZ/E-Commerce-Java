package com.proyecto.e_commerce_java.data.remote.ProductApi;

import com.google.gson.annotations.SerializedName;

public class ProductDto {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("unitsInStock")
    private int unitsInStock;

    @SerializedName("unitPrice")
    private double unitPrice;

    @SerializedName("estado")
    private boolean active;

    @SerializedName("iva")
    private double tax;

    @SerializedName("imagen")
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public boolean isActive() {
        return active;
    }

    public double getTax() {
        return tax;
    }

    public String getImage() {
        return image;
    }
}
