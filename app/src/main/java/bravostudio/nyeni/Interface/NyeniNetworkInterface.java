package bravostudio.nyeni.Interface;

import bravostudio.nyeni.Model.Feed;
import bravostudio.nyeni.Model.FileUpload;
import bravostudio.nyeni.Model.UserFeed;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by jouvyap on 7/25/16.
 */
public interface NyeniNetworkInterface {
    @GET("getAll.php")
    Call<Feed> getFeed();

    @GET("getByUsername.php")
    Call<UserFeed> getUserFeed(@Query("username") String username);

    @Multipart
    @POST("fileUpload.php")
    Call<FileUpload> updateUser(@Part MultipartBody.Part image,
                                @Part("username") RequestBody username,
                                @Part("judul") RequestBody judul,
                                @Part("author") RequestBody author,
                                @Part("medium") RequestBody medium,
                                @Part("tanggal_buat") RequestBody tanggal_buat,
                                @Part("dimensi") RequestBody dimensi,
                                @Part("harga") RequestBody harga);
}
