package com.example.prm_center_kitchen_management.model.response;

import java.util.List;
public class BaseUnitListResponse {
    private Data data;
    public Data getData() { return data; }
    public static class Data {
        private List<BaseUnit> items;
        public List<BaseUnit> getItems() { return items; }
    }
}
