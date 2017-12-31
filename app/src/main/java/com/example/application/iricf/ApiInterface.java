package com.example.application.iricf;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/api/rakes/getall")
    Call<RakeRegister> getRakes(@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/rakes/{rake_num}/coaches")
    Call<CoachPerRakeRegister> getRakeCoaches(@Path("rake_num") String rake_num,@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/login")
    Call<LoginRegister> login(@Field("username") String username,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/coaches/{coach_num}/status")
    Call<CoachStatusRegister> getCoachStatus(@Path("coach_num") String coach_num,@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/position/getall")
    Call<PositionRegister> getPositions(@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/user/profile")
    Call<ProfileRegister> getUserProfile(@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/admin/newuser")
    Call<ResponseBody> createUser(@Field("name") String name,
                                  @Field("username") String username,
                                  @Field("password") String password,
                                  @Field("role") String role,
                                  @Field("token") String token);
}
