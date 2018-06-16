package icf.gov.in.iricf;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    /*@FormUrlEncoded
    @POST("/api/rakes/{rake_num}/coaches")
    Call<CoachPerRakeRegister> getRakeCoaches(@Path(value = "rake_num", encoded = true) String rake_num,
                                              @Field("token") String token);


    @FormUrlEncoded
    @POST("/api/coaches/{coach_num}/status")
    Call<CoachStatusRegister> getCoachStatus(@Path(value = "coach_num", encoded = true) String coach_num,
                                             @Field("token") String token);

    @FormUrlEncoded
    @POST("api/coaches/{coach_num}/position")
    Call<CoachPositionRegister> getCoachPosition(@Path(value = "coach_num", encoded = true) String coachNum,
                                                 @Field("token") String token);

    @FormUrlEncoded
    @POST("/api/rakes/getall")
    Call<RakeRegister> getRakes(@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/rakes/despatched")
    Call<RakeRegister> getDespatchRakes(@Field("token") String token);


    @FormUrlEncoded
    @POST("/api/login")
    Call<LoginRegister> login(@Field("username") String username,
                              @Field("password") String password);


    @FormUrlEncoded
    @POST("/api/user/profile")
    Call<SingleProfileRegister> getUserProfile(@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/admin/newuser")
    Call<PostResponse> createUser(@Field("name") String name,
                                  @Field("username") String username,
                                  @Field("password") String password,
                                  @Field("role") String role,
                                  @Field("token") String token);

    @FormUrlEncoded
    @POST("/api/status/new")
    Call<PostResponse> updateStatus(@Field("token") String token,
                                    @Field("coach_num") String coachNum,
                                    @Field("shell_rec") String shellReceived,
                                    @Field("intake") String intake,
                                    @Field("agency") String agency,
                                    @Field("coupler") String coupler,
                                    @Field("ew_panel") String ewPanel,
                                    @Field("roof_tray") String roofTray,
                                    @Field("ht_tray") String htTray,
                                    @Field("ht_equip") String htEquip,
                                    @Field("high_dip") String highDip,
                                    @Field("uf_tray") String ufTray,
                                    @Field("uf_trans") String ufTrans,
                                    @Field("uf_wire") String ufWire,
                                    @Field("off_roof") String offRoof,
                                    @Field("roof_clear") String roofClear,
                                    @Field("off_ew") String offEw,
                                    @Field("ew_clear") String ewClear,
                                    @Field("mech_pan") String mechPan,
                                    @Field("off_tf") String offTf,
                                    @Field("tf_clear") String tfClear,
                                    @Field("tf_prov") String tfProv,
                                    @Field("lf_load") String lfLoad,
                                    @Field("off_pow") String offPower,
                                    @Field("power_hv") String powerHv,
                                    @Field("off_dip") String offDip,
                                    @Field("dip_clear") String dipClear,
                                    @Field("lower") String lower,
                                    @Field("off_cont") String offCont,
                                    @Field("cont_hv") String contHv,
                                    @Field("load_test") String loadTest,
                                    @Field("pcp_clear") String pcpClear,
                                    @Field("bu_form") String buForm,
                                    @Field("rake_form") String rakeForm,
                                    @Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("/api/rakes/new")
    Call<PostResponse> createRake(@Field("token") String token,
                                  @Field("railway") String railway,
                                  @Field("rake_num") String rakeNum,
                                  @Field("despatch") String despatch);

    @FormUrlEncoded
    @POST("/api/coaches/new")
    Call<PostResponse> createCoach(@Field("token") String token,
                                   @Field("coach_num") String coachNum,
                                   @Field("rake_num") String rakeNum,
                                   @Field("type") String type);


    @FormUrlEncoded
    @POST("/api/position/new")
    Call<PostResponse> updatePosition(@Field("token") String token,
                                      @Field("coach_num") String coachNum,
                                      @Field("linename") String lineName,
                                      @Field("lineno") Integer lineNo,
                                      @Field("stage") Integer stage);

    @FormUrlEncoded
    @POST("/api/position/getall")
    Call<PositionRegister> getAllPosition(@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/logout")
    Call<PostResponse> logOut(@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/admin/getall")
    Call<AllProfileRegister> getAllUsers(@Field("token") String token);

    @FormUrlEncoded
    @POST("/api/rakes/{rake_num}/delete")
    Call<PostResponse> deleteRake(@Path("rake_num") String rakeNum, @Field("token") String token);

    @FormUrlEncoded
    @POST("/api/coaches/{coach_num}/delete")
    Call<PostResponse> deleteCoach(@Path("coach_num") String coachNum, @Field("token") String token);

    @FormUrlEncoded
    @POST("/api/rakes/edit")
    Call<PostResponse> editRake(@Field("token") String token,
                                @Field("old_rakenum") String oldRakeNum,
                                @Field("railway") String railway,
                                @Field("rake_num") String rakeNum,
                                @Field("despatch") String despatch);

    @FormUrlEncoded
    @POST("/api/coaches/edit")
    Call<PostResponse> editCoach(@Field("token") String token,
                                 @Field("old_coachnum") String oldCoachNum,
                                 @Field("coach_num") String coachNum,
                                 @Field("rake_num") String rakeNum,
                                 @Field("type") String type);*/


    @FormUrlEncoded
    @POST("/emuprod2/api/rakes/{rake_num}/coaches")
    Call<CoachPerRakeRegister> getRakeCoaches(@Path(value = "rake_num", encoded = true) String rake_num,
                                              @Field("token") String token);


    @FormUrlEncoded
    @POST("/emuprod2/api/coaches/{coach_num}/status")
    Call<CoachStatusRegister> getCoachStatus(@Path(value = "coach_num", encoded = true) String coach_num,
                                             @Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/coaches/{coach_num}/position")
    Call<CoachPositionRegister> getCoachPosition(@Path(value = "coach_num", encoded = true) String coachNum,
                                                 @Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/rakes/getall")
    Call<RakeRegister> getRakes(@Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/rakes/despatched")
    Call<RakeRegister> getDespatchRakes(@Field("token") String token);


    @FormUrlEncoded
    @POST("/emuprod2/api/login")
    Call<LoginRegister> login(@Field("username") String username,
                              @Field("password") String password);


    @FormUrlEncoded
    @POST("/emuprod2/api/user/profile")
    Call<SingleProfileRegister> getUserProfile(@Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/admin/newuser")
    Call<PostResponse> createUser(@Field("name") String name,
                                  @Field("username") String username,
                                  @Field("password") String password,
                                  @Field("role") String role,
                                  @Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/status/new")
    Call<PostResponse> addStatus(@Field("token") String token,
                                    @Field("coach_num") String coachNum,
                                    @Query("shell_rec") String shellReceived,
                                    @Query("intake") String intake,
                                    @Field("agency") String agency,
                                    @Field("coupler") String coupler,
                                    @Field("ew_panel") String ewPanel,
                                    @Field("roof_tray") String roofTray,
                                    @Field("ht_tray") String htTray,
                                    @Field("ht_equip") String htEquip,
                                    @Field("high_dip") String highDip,
                                    @Field("uf_tray") String ufTray,
                                    @Field("uf_trans") String ufTrans,
                                    @Field("uf_wire") String ufWire,
                                    @Field("off_roof") String offRoof,
                                    @Field("roof_clear") String roofClear,
                                    @Field("off_ew") String offEw,
                                    @Field("ew_clear") String ewClear,
                                    @Field("mech_pan") String mechPan,
                                    @Field("off_tf") String offTf,
                                    @Field("tf_clear") String tfClear,
                                    @Field("tf_prov") String tfProv,
                                    @Field("lf_load") String lfLoad,
                                    @Field("off_pow") String offPower,
                                    @Field("power_hv") String powerHv,
                                    @Field("off_dip") String offDip,
                                    @Field("dip_clear") String dipClear,
                                    @Field("lower") String lower,
                                    @Field("off_cont") String offCont,
                                    @Field("cont_hv") String contHv,
                                    @Field("load_test") String loadTest,
                                    @Field("pcp_clear") String pcpClear,
                                    @Field("bu_form") String buForm,
                                    @Field("rake_form") String rakeForm,
                                    @Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("/emuprod2/api/status/edit/{field_name}")
    Call<PostResponse> editStatus(@Path("field_name") String fieldName,
                                  @Field("token") String token,
                                  @Field("coach_num")String coachNum,
                                  @QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST("/emuprod2/api/rakes/new")
    Call<PostResponse> createRake(@Field("token") String token,
                                  @Field("railway") String railway,
                                  @Field("rake_num") String rakeNum,
                                  @Field("despatch") String despatch);

    @FormUrlEncoded
    @POST("/emuprod2/api/coaches/new")
    Call<PostResponse> createCoach(@Field("token") String token,
                                   @Field("coach_num") String coachNum,
                                   @Field("rake_num") String rakeNum,
                                   @Field("type") String type);


    @FormUrlEncoded
    @POST("/emuprod2/api/position/new")
    Call<PostResponse> updatePosition(@Field("token") String token,
                                      @Field("coach_num") String coachNum,
                                      @Field("linename") String lineName,
                                      @Field("lineno") Integer lineNo,
                                      @Field("stage") Integer stage);

    @FormUrlEncoded
    @POST("/emuprod2/api/position/getall")
    Call<PositionRegister> getAllPosition(@Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/logout")
    Call<PostResponse> logOut(@Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/admin/getall")
    Call<AllProfileRegister> getAllUsers(@Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/rakes/{rake_num}/delete")
    Call<PostResponse> deleteRake(@Path("rake_num") String rakeNum, @Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/coaches/{coach_num}/delete")
    Call<PostResponse> deleteCoach(@Path("coach_num") String coachNum, @Field("token") String token);

    @FormUrlEncoded
    @POST("/emuprod2/api/rakes/edit/{field_name}")
    Call<PostResponse> editRake(@Path("field_name") String fieldName,
                                @Field("token") String token,
                                @Field("old_rakenum")String oldRakeNum,
                                @QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST("/emuprod2/api/coaches/edit")
    Call<PostResponse> editCoach(@Field("token") String token,
                                 @Field("old_coachnum") String oldCoachNum,
                                 @Field("coach_num") String coachNum,
                                 @Field("rake_num") String rakeNum,
                                 @Field("type") String type);
}
