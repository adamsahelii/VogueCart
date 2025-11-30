package com.myproject.project_279;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddToCartHelper {
    private static final String PREFS_NAME = "cartPrefs";
    private static final String KEY_CART_ITEMS = "cartItems";
    private static final Gson gson = new Gson();

    public static List<Item> getCartItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String cartItemsString = prefs.getString(KEY_CART_ITEMS, null);
        if (cartItemsString != null) {
            Type type = new TypeToken<List<Item>>() {}.getType();
            List<Item> cartItems = gson.fromJson(cartItemsString, type);
            return cartItems != null ? cartItems : new ArrayList<Item>();
        } else {
            return new ArrayList<Item>();
        }
    }

    public static void addItemToCart(Context context, Item item) {
        List<Item> cartItems = new ArrayList<>(getCartItems(context));
        boolean exists = false;
        for (Item cartItem : cartItems) {
            if (cartItem.getName().equals(item.getName())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            cartItems.add(item);
            saveCartItems(context, cartItems);
        }
    }

    public static void saveCartItems(Context context, List<Item> cartItems) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String cartItemsString = gson.toJson(cartItems);
        editor.putString(KEY_CART_ITEMS, cartItemsString);
        editor.apply();
    }
}

