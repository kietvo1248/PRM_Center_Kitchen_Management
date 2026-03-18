package com.example.prm_center_kitchen_management.model.request;

public class ProductRequest {
    private String name;
    private Integer baseUnitId;
    private Integer shelfLifeDays;
    private String imageUrl;

    public ProductRequest(String name, Integer baseUnitId, Integer shelfLifeDays, String imageUrl) {
        this.name = name;
        this.baseUnitId = baseUnitId;
        this.shelfLifeDays = shelfLifeDays;
        this.imageUrl = imageUrl;
    }
}
