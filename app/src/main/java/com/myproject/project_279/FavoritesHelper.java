package com.myproject.project_279;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoritesHelper {
    private static final String PREFS_NAME = "favoritesPrefs";
    private static final String KEY_FAVORITES = "favorites";
    private static final Gson gson = new Gson();

    public static List<Item> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String favoritesString = prefs.getString(KEY_FAVORITES, null);
        if (favoritesString != null) {
            Type type = new TypeToken<List<Item>>() {}.getType();
            List<Item> favorites = gson.fromJson(favoritesString, type);
            return favorites != null ? favorites : new ArrayList<Item>();
        } else {
            return new ArrayList<Item>();
        }
    }

    public static void addFavorite(Context context, Item item) {
        List<Item> favorites = new ArrayList<>(getFavorites(context));
        boolean exists = false;
        for (Item favorite : favorites) {
            if (favorite.getName().equals(item.getName())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            favorites.add(item);
            saveFavorites(context, favorites);
        }
    }

    public static void removeFavorite(Context context, Item item) {
        List<Item> favorites = new ArrayList<>(getFavorites(context));
        List<Item> toRemove = new ArrayList<>();
        for (Item favorite : favorites) {
            if (favorite.getName().equals(item.getName())) {
                toRemove.add(favorite);
            }
        }
        favorites.removeAll(toRemove);
        saveFavorites(context, favorites);
    }

    public static boolean isFavorite(Context context, Item item) {
        List<Item> favorites = getFavorites(context);
        for (Item favorite : favorites) {
            if (favorite.getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void saveFavorites(Context context, List<Item> favorites) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String favoritesString = gson.toJson(favorites);
        editor.putString(KEY_FAVORITES, favoritesString);
        editor.apply();
    }
}

