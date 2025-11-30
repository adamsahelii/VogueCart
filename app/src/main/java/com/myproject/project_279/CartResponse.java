package com.myproject.project_279;

import java.util.List;

public class CartResponse {
    private boolean success;
    private List<Item> items;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getMessage() {
        return message;
    }
}
