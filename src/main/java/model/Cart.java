/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Map;

/**
 *
 * @author ThienThu
 */
public class Cart {
    private Map<Integer,CartItem> items;

    public Cart(Map<Integer, CartItem> items) {
        this.items = items;
    }
    
    
}
