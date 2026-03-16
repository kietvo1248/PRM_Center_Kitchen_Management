package com.example.prm_center_kitchen_management.model.response;

import java.io.Serializable;
public class Supplier implements Serializable{
    private int id;
    private String name;
    private String contactName;
    private String phone;
    private String address;
    private boolean isActive;
    private String createdAt;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getContactName() { return contactName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isActive() { return isActive; }
    public String getCreatedAt() { return createdAt; }
}
