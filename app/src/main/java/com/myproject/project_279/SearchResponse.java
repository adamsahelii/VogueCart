package com.myproject.project_279;

import java.util.List;

public class SearchResponse {
    private List<Item> items;

    public SearchResponse(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

