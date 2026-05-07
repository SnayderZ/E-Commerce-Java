package com.proyecto.e_commerce_java.data.remote.ProductApi;

import com.google.gson.annotations.SerializedName;

public class ProductoDto {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("unitsInStock")
    private int unitsInStock;

    @SerializedName("unitPrice")
    private double unitPrice;

    @SerializedName("estado")
    private boolean estado;

    @SerializedName("iva")
    private double iva;

    @SerializedName("imagen")
    private String imagen;

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

    public boolean isEstado() {
        return estado;
    }

    public double getIva() {
        return iva;
    }

    public String getImagen() {
        return imagen;
    }
}
