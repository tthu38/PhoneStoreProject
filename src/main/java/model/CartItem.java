/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ThienThu
 */
public class CartItem {
    private ProductVariant productVariant;
    private int quantity;
    private boolean selected = true; // Default to selected

    public CartItem(ProductVariant productVariant, int quantity) {
        this.productVariant = productVariant;
        this.quantity = quantity;
    }

    public CartItem() {
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    
}
