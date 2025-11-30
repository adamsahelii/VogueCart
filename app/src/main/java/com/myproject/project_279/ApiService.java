package com.myproject.project_279;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // ---------- ITEMS ----------
    @GET("products.php")
    Call<List<Item>> getAllItems();

    @GET("product.php")
    Call<Item> getItemById(@Query("id") int id);

    // search via products.php?query=...
    @GET("products.php")
    Call<List<Item>> searchItems(@Query("query") String query);

    // ---------- AUTH ----------
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<LoginResponse> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username
    );

    // ---------- FAVORITES ----------
    @GET("favorites_get.php")
    Call<FavoritesResponse> getFavorites(@Query("user_id") int userId);

    @FormUrlEncoded
    @POST("favorites_add.php")
    Call<SimpleResponse> addFavorite(
            @Field("user_id") int userId,
            @Field("item_id") int itemId
    );

    @FormUrlEncoded
    @POST("favorites_remove.php")
    Call<SimpleResponse> removeFavorite(
            @Field("user_id") int userId,
            @Field("item_id") int itemId
    );

    // ---------- CART ----------
    @GET("cart_get.php")
    Call<CartResponse> getCart(@Query("user_id") int userId);

    @FormUrlEncoded
    @POST("cart_add.php")
    Call<SimpleResponse> addToCart(
            @Field("user_id") int userId,
            @Field("item_id") int itemId,
            @Field("quantity") int quantity
    );

    @FormUrlEncoded
    @POST("cart_remove.php")
    Call<SimpleResponse> removeFromCart(
            @Field("user_id") int userId,
            @Field("item_id") int itemId
    );
}
