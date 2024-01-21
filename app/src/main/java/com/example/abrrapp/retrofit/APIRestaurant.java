package com.example.abrrapp.retrofit;


import com.example.abrrapp.models.DishModel;
import com.example.abrrapp.models.RestaurantModel;
import com.example.abrrapp.models.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIRestaurant {
    @GET("restaurant-hot/")
    Observable<RestaurantModel> getRestaurantHot();

    @GET("restaurant/")
    Observable<RestaurantModel> getRestaurant();

    @GET("dish/")
    Observable<DishModel> getDish();

    @GET("get-profile/{uid}/")
    Observable<UserModel> getProfile(
            @Path("uid") String uid,
            @Header("Authorization") String credentials
    );
}
