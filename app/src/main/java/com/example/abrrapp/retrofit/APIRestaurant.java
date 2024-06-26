package com.example.abrrapp.retrofit;


import com.example.abrrapp.models.CategoryModel;
import com.example.abrrapp.models.ChatbotModel;
import com.example.abrrapp.models.CheckModel;
import com.example.abrrapp.models.DefaultModel;
import com.example.abrrapp.models.DetailRestaurantModel;
import com.example.abrrapp.models.DishFeatureModel;
import com.example.abrrapp.models.DishModel;
import com.example.abrrapp.models.DishSuggestModel;
import com.example.abrrapp.models.LikeRestaurantModel;
import com.example.abrrapp.models.NumModel;
import com.example.abrrapp.models.OrderCartModel;
import com.example.abrrapp.models.OrderItemModel;
import com.example.abrrapp.models.OrderModel;
import com.example.abrrapp.models.ProvinceModel;
import com.example.abrrapp.models.RatingModel;
import com.example.abrrapp.models.RestaurantModel;
import com.example.abrrapp.models.SearchAI;
import com.example.abrrapp.models.SearchAIModel;
import com.example.abrrapp.models.StatisticModel;
import com.example.abrrapp.models.TableModel;
import com.example.abrrapp.models.Token;
import com.example.abrrapp.models.UserModel;

import org.json.JSONArray;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIRestaurant {
    @GET("api/restaurant-hot/")
    Observable<RestaurantModel> getRestaurantHot();

    @GET("api/restaurant/")
    Observable<RestaurantModel> getRestaurant();

    @GET("api/dish/")
    Observable<DishModel> getDish();

    @GET("api/get-profile/{uid}/")
    Observable<UserModel> getProfile(
            @Path("uid") String uid,
            @Header("Authorization") String credentials
    );

    @GET("api/list-like/{uid}/")
    Observable<LikeRestaurantModel> getListLikeRes(
            @Path("uid") String uid,
            @Header("Authorization") String credentials
    );

    @GET("api/list-order/{uid}/")
    Observable<OrderModel> getListOrder(
            @Path("uid") String uid,
            @Header("Authorization") String credentials
    );

    @GET("api/order-detail/{oid}/")
    Observable<OrderItemModel> getOrderDetail(
            @Path("oid") String oid,
            @Header("Authorization") String credentials
    );

    @GET("api/dishes-of-restaurant/{rid}/")
    Observable<DishFeatureModel> getFeatureDish(
            @Path("rid") String rid
    );

    @GET("api/get-table/{rid}/")
    Observable<TableModel> getTable(
            @Path("rid") String rid
    );

    @GET("api/category/")
    Observable<CategoryModel> getCategory();

    @GET("api/restaurant/{rid}/")
    Observable<DetailRestaurantModel> getDetailRestaurant(
            @Path("rid") String rid
    );

    @GET("api/order-cart/{uid}/{rid}/")
    Observable<OrderCartModel> getOrderCart(
            @Path("uid") String uid,
            @Path("rid") String rid,
            @Header("Authorization") String credentials
    );

    @POST("auth/api/login/")
    @FormUrlEncoded
    Observable<UserModel> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("auth/api/register/")
    @FormUrlEncoded
    Observable<UserModel> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("auth/api/token/verify/")
    @FormUrlEncoded
    Observable<CheckModel> checkToken(
            @Field("token") String refresh
    );

    @POST("auth/api/token/refresh/")
    @FormUrlEncoded
    Observable<Token> refreshToken(
            @Field("refresh") String refresh
    );

    @POST("auth/api/send-email/")
    @FormUrlEncoded
    Observable<DefaultModel> sendEmail(
            @Field("email") String email
    );

    @POST("api/contact-us/")
    @FormUrlEncoded
    Observable<DefaultModel> contactUs(
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("subject") String subject,
            @Field("message") String message
    );

    @POST("api/add-order-cart/{uid}/{rid}/")
    @FormUrlEncoded
    Observable<DefaultModel> addOrderCart(
            @Path("uid") String uid,
            @Path("rid") String rid,
            @Field("full_name") String full_name,
            @Field("phone") String phone,
            @Field("tid") String tid,
            @Field("time_from") String time_from,
            @Field("time_to") String time_to,
            @Field("number_people") String number_people,
            @Field("items") JSONArray items,
            @Field("order_date") String order_date,
            @Header("Authorization") String credentials
    );

    @POST("api/update-order-cart/{uid}/{rid}/")
    @FormUrlEncoded
    Observable<DefaultModel> updateOrderCart(
            @Path("uid") String uid,
            @Path("rid") String rid,
            @Field("full_name") String full_name,
            @Field("phone") String phone,
            @Field("tid") String tid,
            @Field("time_from") String time_from,
            @Field("time_to") String time_to,
            @Field("number_people") String number_people,
            @Field("items") JSONArray items,
            @Field("order_date") String order_date,
            @Header("Authorization") String credentials
    );

    @POST("api/add-order/{uid}/{rid}/")
    @FormUrlEncoded
    Observable<DefaultModel> addOrder(
            @Path("uid") String uid,
            @Path("rid") String rid,
            @Field("full_name") String full_name,
            @Field("phone") String phone,
            @Field("tid") String tid,
            @Field("deposit") float deposit,
            @Field("price") float price,
            @Field("time_from") String time_from,
            @Field("time_to") String time_to,
            @Field("number_people") int number_people,
            @Field("items") String items,
            @Field("order_date") String order_date,
            @Header("Authorization") String credentials
    );

    @GET("api/delete-order-cart/{uid}/{rid}/")
    Observable<DefaultModel> deleteOrderCart(
            @Path("uid") String uid,
            @Path("rid") String rid
    );

    @POST("auth/api/send-email/")
    @FormUrlEncoded
    Observable<DefaultModel> forgetPassword(
            @Field("email") String email
    );

    @POST("api/check-order/{rid}/")
    @FormUrlEncoded
    Observable<DefaultModel> checkOrder(
            @Path("rid") String rid,
            @Field("time_from") String time_from,
            @Field("time_to") String time_to,
            @Field("tid") String tid,
            @Field("order_date") String order_date,
            @Header("Authorization") String credentials
    );

    @GET("api/count/{uid}/")
    Observable<NumModel> num(
            @Path("uid") String uid
    );

    @GET("api/delete-like/{uid}/{rid}/")
    Observable<LikeRestaurantModel> delLike(
            @Path("uid") String uid,
            @Path("rid") String rid,
            @Header("Authorization") String credentials
    );

    @GET("api/add-like/{uid}/{rid}/")
    Observable<DefaultModel> addLike(
            @Path("uid") String uid,
            @Path("rid") String rid,
            @Header("Authorization") String credentials
    );

    @GET("api/check-like/{uid}/{rid}/")
    Observable<DefaultModel> checkLike(
            @Path("uid") String uid,
            @Path("rid") String rid,
            @Header("Authorization") String credentials
    );

    @GET("api/province/")
    Observable<ProvinceModel> getProvince();

    @POST("auth/api/login/google/")
    @FormUrlEncoded
    Observable<UserModel> loginGoogle(
            @Field("username") String username,
            @Field("email") String email,
            @Field("image") String image,
            @Field("full_name") String full_name,
            @Field("id") String id
    );

    @POST("auth/api/login/facebook/")
    @FormUrlEncoded
    Observable<UserModel> loginFacebook(
            @Field("username") String username,
            @Field("email") String email,
            @Field("image") String image,
            @Field("full_name") String full_name,
            @Field("id") String id
    );
    @Multipart
    @POST("api/search-ai/")
    Observable<SearchAIModel> searchAI(
            @Part MultipartBody.Part image
    );

    @POST("auth/api/update-password/{uid}/")
    @FormUrlEncoded
    Observable<DefaultModel> updatePassword(
            @Path("uid") String uid,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password,
            @Header("Authorization") String credentials
    );

    @GET("api/suggest-food/{uid}/{rid}/")
    Observable<DishSuggestModel> suggestFood(
            @Path("uid") String uid,
            @Path("rid") String rid
    );

    @GET("chatbot/list-chat/{uid}/")
    Observable<ChatbotModel> listChatbot(
            @Path("uid") String uid
    );

    @POST("chatbot/response/{uid}/")
    @FormUrlEncoded
    Observable<ChatbotModel> sendChatbot(
            @Path("uid") String uid,
            @Field("body") String body
    );

    @GET("api/check-order-cart/{uid}/{rid}/")
    Observable<DefaultModel> checkOrderCart(
            @Path("uid") String uid,
            @Path("rid") String rid
    );

    @POST("api/edit-profile/{uid}/")
    @FormUrlEncoded
    Observable<DefaultModel> updateProfileNotImage(
            @Path("uid") String uid,
            @Field("full_name") String full_name,
            @Field("phone") String phone,
            @Field("address") String address,
            @Header("Authorization") String credentials
    );

    @POST("api/edit-profile/{uid}/")
    @Multipart
    Observable<DefaultModel> updateProfile(
            @Path("uid") String uid,
            @Part MultipartBody.Part image,
            @Part("full_name") RequestBody full_name,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Header("Authorization") String credentials
    );

    @POST("api/search-restaurant/")
    @FormUrlEncoded
    Observable<RestaurantModel> searchRestaurant(
            @Field("q") String q
    );

    @GET("api/rating-all/")
    Observable<RatingModel> getRatingAll();

    @GET("api/statistics-user/{uid}/")
    Observable<StatisticModel> getStatistic(
            @Path("uid") String uid,
            @Header("Authorization") String credentials
    );

    @GET("api/cancel-order/{oid}/")
    Observable<DefaultModel> cancelOrder(
            @Path("oid") String oid,
            @Header("Authorization") String credentials
    );
}
