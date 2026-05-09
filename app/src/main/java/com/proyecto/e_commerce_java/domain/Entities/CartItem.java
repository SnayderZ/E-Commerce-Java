package com.proyecto.e_commerce_java.domain.Entities;

public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity){
        this.product = product;
        this.quantity = Math.max(1,quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getSubTotal(){
        return  product.getPrice()*quantity;
    }
    public void increaseQuantity() {
        quantity++;
    }
    public void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
        }
    }
    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }
}
