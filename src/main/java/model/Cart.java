/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ThienThu
 */
public class Cart {
    private Map<Integer, CartItem> cartitems = new HashMap<>();
    private double totalPrice;

    public Cart(Map<Integer, CartItem> cartitems) {
        this.cartitems = cartitems;
    }

    public Map<Integer, CartItem> getCartItems() {
        return cartitems;
    }

    public void setCartItems(Map<Integer, CartItem> cartitems) {
        this.cartitems = cartitems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
