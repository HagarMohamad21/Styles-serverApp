package styles.zonetech.net.styles.server.Server;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IServer {

    @FormUrlEncoded
    @POST("login")
    Call<String> login(@Field("userdevice") String userdevice,
                       @Field("usertoken") String usertoken,
                       @Field("useremail") String email,
                       @Field("password") String password);


    @FormUrlEncoded
    @POST("orders")
    Call<String>orders(@Field("saloonid") String saloonid);

    @FormUrlEncoded
    @POST("approve")
    Call<String>approve(@Field("orderid") String orderid);

    @FormUrlEncoded
    @POST("reject")
    Call<String>reject(@Field("orderid") String orderid,@Field("reasons") String reasons);


    @FormUrlEncoded
    @POST("recover")
    Call<String>recoverPassword  (@Field("useremail") String useremail,@Field("language")String language);

    @FormUrlEncoded
    @POST("account")
    Call<String>editAccount      (@Field("saloonid") String saloonid,
                                  @Field("username") String username,
                                  @Field("useremail") String useremail,
                                  @Field("userphone") String userphone,
                                  @Field("password") String password);


}
