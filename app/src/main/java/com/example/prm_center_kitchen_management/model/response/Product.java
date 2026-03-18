package com.example.prm_center_kitchen_management.model.response;

import java.io.Serializable;
public class Product implements Serializable {
    private int id;
    private String sku;
    private String name;
    private Integer baseUnitId;
    private String baseUnitName;
    private int shelfLifeDays;
    private int minStockLevel;
    private String imageUrl;
    private boolean isActive;

    // Getters
    public int getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public Integer getBaseUnitId() { return baseUnitId; }
    public String getBaseUnitName() { return baseUnitName; }
    public int getShelfLifeDays() { return shelfLifeDays; }
    public int getMinStockLevel() { return minStockLevel; }
    public String getImageUrl() { return imageUrl; }
    public boolean isActive() { return isActive; }
}
