package com.example.lab_rest.remote;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.FileInfo;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CarService {

    @GET("Cars")
    Call<List<Car>> getAllCars(@Header("api-key") String api_key);

    @GET("Cars/{CarID}")
    Call<Car> getCar(@Header("api-key") String api_key, @Path("CarID") int CarID);
    @FormUrlEncoded
    @POST("Cars")
    Call<Car> addCar(@Header ("api-key") String apiKey, @Field("Model") String Model,
                     @Field("Brand") String Brand, @Field("PlateNumber") String PlateNumber,
                     @Field("availability") String availability, @Field("CreatedAt") String CreatedAt,
                     @Field("image") String image);

    @DELETE("Cars/{CarID}")
    Call<DeleteResponse> deleteCar(@Header ("api-key") String apiKey, @Path("CarID") int CarID);
    @FormUrlEncoded
    @POST("Cars/{CarID}")
    Call<Car> updateCar(@Header("api-key") String apiKey, @Path("CarID") int CarID, @Field("maintenance_id") int maintenanceID,
                        @Field("Model") String Model,
                        @Field("Brand") String Brand, @Field("PlateNumber") String PlateNumber,
                        @Field("availability") String availability, @Field("CreatedAt") String CreatedAt,
                        @Field("image") String image);

    @Multipart
    @POST("files")
    Call<FileInfo> uploadFile(@Header ("api-key") String apiKey, @Part MultipartBody.Part file);

}
