package com.example.prm_center_kitchen_management.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PaginatedResponse<T> {
    @SerializedName("items")
    private List<T> items;

    /** Một số API đặt page/limit/total trong meta thay vì root của data */
    @SerializedName("meta")
    private PageMeta meta;

    @SerializedName("total")
    private int total;

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    public List<T> getItems() { return items; }

    public int getTotal() {
        if (meta != null && meta.total > 0) return meta.total;
        return total;
    }

    public int getPage() {
        if (meta != null && meta.page > 0) return meta.page;
        return page;
    }

    public int getLimit() {
        if (meta != null && meta.limit > 0) return meta.limit;
        return limit;
    }

    public static class PageMeta {
        @SerializedName("page")
        private int page;
        @SerializedName("limit")
        private int limit;
        @SerializedName("total")
        private int total;
        @SerializedName("totalPages")
        private int totalPages;
    }
}
