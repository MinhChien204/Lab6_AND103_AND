package learn.fpoly.lab6_and103.server;

import java.util.ArrayList;
import java.util.Map;

import learn.fpoly.lab6_and103.model.Fruits;
import learn.fpoly.lab6_and103.model.Response;
import learn.fpoly.lab6_and103.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface APIService {
    String DOMAIN = "http://10.0.2.2:3000/";

    @GET("/list")
    Call<ArrayList<Fruits>> getFruits();

    @Multipart
    @POST("/add-fruits")
    Call<Fruits> addFruits(@PartMap Map<String, RequestBody> requestBodyMap,
                           @Part ArrayList<MultipartBody.Part> image);

    @Multipart
    @POST("/register-send-email")
    Call<Response<User>> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avatar
    );

    @POST("/login")
    Call<Response<User>> login (@Body User user);

    @DELETE("/delete/{id}")
    Call<Fruits> deleteFruits(@Path("id") String id);

    @Multipart
    @PUT("/update-fruit/{id}")
    Call<Fruits> update(@Path("id") String id,
                        @PartMap Map<String, RequestBody> requestBodyMap,
                        @Part MultipartBody.Part image);

    @Multipart
    @PUT("/update-no-image/{id}")
    Call<Fruits> updateNoImage(@Path("id") String id,
                               @PartMap Map<String, RequestBody> requestBodyMap);
}
