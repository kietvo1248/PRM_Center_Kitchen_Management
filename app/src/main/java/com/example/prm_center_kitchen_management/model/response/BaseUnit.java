package com.example.prm_center_kitchen_management.model.response;

public class BaseUnit {
    private int id;
    private String name;
    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}
