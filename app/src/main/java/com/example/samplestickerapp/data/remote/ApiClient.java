package com.example.samplestickerapp.data.remote;

import com.google.gson.JsonElement;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiClient {


    @GET("apps/{appId}/stickers")
    Call<ResponseBody> stickers(@Path("appId") String appId);

    @GET("app_version")
    Call<JsonElement> appVersion();

    @POST("apps/{appId}/group/{groupId}/increase_download_count")
    Call<JsonElement> incDownloadCount(@Path("appId") String appId,@Path("groupId") String groupId);


}
