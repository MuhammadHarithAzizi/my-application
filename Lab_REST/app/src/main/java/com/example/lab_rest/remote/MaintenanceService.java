package com.example.lab_rest.remote;

import com.example.lab_rest.model.Book;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.Maintenance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MaintenanceService {

    @GET("maintenance")
    Call<List<Maintenance>> getAllMaintenances(@Header("api-key") String api_key);

    @GET("maintenance/{maintenanceID}")
    Call<Maintenance> getMaintenance(@Header("api-key") String api_key, @Path("maintenanceID") int maintenanceID);

    @FormUrlEncoded
    @POST("maintenance")
    Call<Maintenance> addMaintenance(@Header ("api-key") String apiKey, @Field("type") String type,
                                     @Field("cost")  double cost,
                                     @Field("maintenanceDate") String maintenanceDate,
                                     @Field("updateMaintenanceDate") String updateMaintenanceDate);
    @DELETE("maintenance/{maintenanceID}")
    Call<DeleteResponse> deleteMaintenance(@Header ("api-key") String apiKey, @Path("maintenanceID") int maintenanceID);

    @FormUrlEncoded
    @POST("maintenance/{maintenanceID}")
    Call<Maintenance> updateMaintenance(@Header ("api-key") String apiKey, @Path("maintenanceID")int maintenanceID,
                                        @Field("type") String type, @Field("cost")  double cost,
                                        @Field("maintenanceDate") String maintenanceDate,
                                        @Field("updateMaintenanceDate") String updateMaintenanceDate);

}