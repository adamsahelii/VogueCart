package com.myproject.project_279;

import java.util.ArrayList;
import java.util.List;

public class MockDataHelper {

    // Mock user ID for development
    public static final int MOCK_USER_ID = 1;
    public static final String MOCK_USERNAME = "Dev User";
    public static final String MOCK_EMAIL = "dev@test.com";

    /**
     * Get a list of mock products for testing
     */
    public static List<Item> getMockProducts() {
        List<Item> items = new ArrayList<>();

        // Fashion items
        items.add(new Item(1, "Classic White T-Shirt", "$19.99", "https://via.placeholder.com/300x400/FFFFFF/000000?text=White+Tee"));
        items.add(new Item(2, "Blue Denim Jeans", "$49.99", "https://via.placeholder.com/300x400/4A90E2/FFFFFF?text=Blue+Jeans"));
        items.add(new Item(3, "Black Leather Jacket", "$129.99", "https://via.placeholder.com/300x400/000000/FFFFFF?text=Leather+Jacket"));

        // Shoes
        items.add(new Item(4, "Running Sneakers", "$79.99", "https://via.placeholder.com/300x400/FF6B6B/FFFFFF?text=Sneakers"));
        items.add(new Item(5, "Formal Oxford Shoes", "$89.99", "https://via.placeholder.com/300x400/8B4513/FFFFFF?text=Oxford+Shoes"));

        // Electronics
        items.add(new Item(6, "Wireless Headphones", "$159.99", "https://via.placeholder.com/300x400/333333/FFFFFF?text=Headphones"));
        items.add(new Item(7, "Smart Watch", "$299.99", "https://via.placeholder.com/300x400/1E90FF/FFFFFF?text=Smart+Watch"));
        items.add(new Item(8, "Bluetooth Speaker", "$69.99", "https://via.placeholder.com/300x400/FF4500/FFFFFF?text=Speaker"));

        // Sports
        items.add(new Item(9, "Yoga Mat", "$29.99", "https://via.placeholder.com/300x400/9370DB/FFFFFF?text=Yoga+Mat"));
        items.add(new Item(10, "Dumbbells Set", "$49.99", "https://via.placeholder.com/300x400/708090/FFFFFF?text=Dumbbells"));

        // Cosmetics
        items.add(new Item(11, "Moisturizing Cream", "$24.99", "https://via.placeholder.com/300x400/FFB6C1/FFFFFF?text=Cream"));
        items.add(new Item(12, "Lipstick Set", "$34.99", "https://via.placeholder.com/300x400/DC143C/FFFFFF?text=Lipstick"));

        return items;
    }

    /**
     * Get mock products by category
     */
    public static List<Item> getMockProductsByCategory(String category) {
        List<Item> items = new ArrayList<>();

        switch (category.toLowerCase()) {
            case "fashion":
                items.add(new Item(1, "Classic White T-Shirt", "$19.99", "https://via.placeholder.com/300x400/FFFFFF/000000?text=White+Tee"));
                items.add(new Item(2, "Blue Denim Jeans", "$49.99", "https://via.placeholder.com/300x400/4A90E2/FFFFFF?text=Blue+Jeans"));
                items.add(new Item(3, "Black Leather Jacket", "$129.99", "https://via.placeholder.com/300x400/000000/FFFFFF?text=Leather+Jacket"));
                items.add(new Item(13, "Summer Dress", "$59.99", "https://via.placeholder.com/300x400/FFD700/000000?text=Summer+Dress"));
                items.add(new Item(14, "Casual Hoodie", "$39.99", "https://via.placeholder.com/300x400/808080/FFFFFF?text=Hoodie"));
                break;

            case "shoes":
                items.add(new Item(4, "Running Sneakers", "$79.99", "https://via.placeholder.com/300x400/FF6B6B/FFFFFF?text=Sneakers"));
                items.add(new Item(5, "Formal Oxford Shoes", "$89.99", "https://via.placeholder.com/300x400/8B4513/FFFFFF?text=Oxford+Shoes"));
                items.add(new Item(15, "Hiking Boots", "$119.99", "https://via.placeholder.com/300x400/654321/FFFFFF?text=Hiking+Boots"));
                items.add(new Item(16, "Beach Sandals", "$29.99", "https://via.placeholder.com/300x400/87CEEB/000000?text=Sandals"));
                break;

            case "electronics":
                items.add(new Item(6, "Wireless Headphones", "$159.99", "https://via.placeholder.com/300x400/333333/FFFFFF?text=Headphones"));
                items.add(new Item(7, "Smart Watch", "$299.99", "https://via.placeholder.com/300x400/1E90FF/FFFFFF?text=Smart+Watch"));
                items.add(new Item(8, "Bluetooth Speaker", "$69.99", "https://via.placeholder.com/300x400/FF4500/FFFFFF?text=Speaker"));
                items.add(new Item(17, "Tablet 10 inch", "$399.99", "https://via.placeholder.com/300x400/000080/FFFFFF?text=Tablet"));
                items.add(new Item(18, "Power Bank", "$39.99", "https://via.placeholder.com/300x400/C0C0C0/000000?text=Power+Bank"));
                break;

            case "sports":
                items.add(new Item(9, "Yoga Mat", "$29.99", "https://via.placeholder.com/300x400/9370DB/FFFFFF?text=Yoga+Mat"));
                items.add(new Item(10, "Dumbbells Set", "$49.99", "https://via.placeholder.com/300x400/708090/FFFFFF?text=Dumbbells"));
                items.add(new Item(19, "Tennis Racket", "$89.99", "https://via.placeholder.com/300x400/32CD32/FFFFFF?text=Tennis+Racket"));
                items.add(new Item(20, "Basketball", "$34.99", "https://via.placeholder.com/300x400/FF8C00/000000?text=Basketball"));
                break;

            case "cosmetics":
                items.add(new Item(11, "Moisturizing Cream", "$24.99", "https://via.placeholder.com/300x400/FFB6C1/FFFFFF?text=Cream"));
                items.add(new Item(12, "Lipstick Set", "$34.99", "https://via.placeholder.com/300x400/DC143C/FFFFFF?text=Lipstick"));
                items.add(new Item(21, "Face Serum", "$44.99", "https://via.placeholder.com/300x400/FFC0CB/000000?text=Serum"));
                items.add(new Item(22, "Perfume", "$79.99", "https://via.placeholder.com/300x400/DDA0DD/000000?text=Perfume"));
                break;

            default:
                items = getMockProducts();
                break;
        }

        return items;
    }

    /**
     * Get a single mock product by ID
     */
    public static Item getMockProductById(int id) {
        List<Item> allProducts = getMockProducts();
        for (Item item : allProducts) {
            if (item.getId() == id) {
                return item;
            }
        }
        // Return first product as default
        return allProducts.get(0);
    }

    /**
     * Get mock cart items
     */
    public static List<Item> getMockCartItems() {
        List<Item> cartItems = new ArrayList<>();
        cartItems.add(new Item(1, "Classic White T-Shirt", "$19.99", "https://via.placeholder.com/300x400/FFFFFF/000000?text=White+Tee", 2));
        cartItems.add(new Item(6, "Wireless Headphones", "$159.99", "https://via.placeholder.com/300x400/333333/FFFFFF?text=Headphones", 1));
        cartItems.add(new Item(9, "Yoga Mat", "$29.99", "https://via.placeholder.com/300x400/9370DB/FFFFFF?text=Yoga+Mat", 1));
        return cartItems;
    }

    /**
     * Get mock favorite items
     */
    public static List<Item> getMockFavorites() {
        List<Item> favorites = new ArrayList<>();
        favorites.add(new Item(3, "Black Leather Jacket", "$129.99", "https://via.placeholder.com/300x400/000000/FFFFFF?text=Leather+Jacket"));
        favorites.add(new Item(7, "Smart Watch", "$299.99", "https://via.placeholder.com/300x400/1E90FF/FFFFFF?text=Smart+Watch"));
        favorites.add(new Item(12, "Lipstick Set", "$34.99", "https://via.placeholder.com/300x400/DC143C/FFFFFF?text=Lipstick"));
        return favorites;
    }

    /**
     * Search mock products
     */
    public static List<Item> searchMockProducts(String query) {
        List<Item> allProducts = getMockProducts();
        List<Item> results = new ArrayList<>();

        String lowerQuery = query.toLowerCase();
        for (Item item : allProducts) {
            if (item.getName().toLowerCase().contains(lowerQuery)) {
                results.add(item);
            }
        }

        return results;
    }

    /**
     * Create a mock LoginResponse
     */
    public static LoginResponse getMockLoginResponse() {
        LoginResponse response = new LoginResponse();
        // You'll need to check the LoginResponse class structure
        // and set appropriate fields
        return response;
    }
}
