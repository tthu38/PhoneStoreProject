/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author dangt
 */
public class GoogleUser {
    private String id;
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private boolean verifiedEmail;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getGivenName() { return givenName; }
    public void setGivenName(String givenName) { this.givenName = givenName; }
    
    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }
    
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    
    public boolean isVerifiedEmail() { return verifiedEmail; }
    public void setVerifiedEmail(boolean verifiedEmail) { this.verifiedEmail = verifiedEmail; }
}
