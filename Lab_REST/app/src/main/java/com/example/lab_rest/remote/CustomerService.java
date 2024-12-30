package com.example.lab_rest.remote;

import com.example.lab_rest.model.Customer;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CustomerService {

    @GET("customer")
    Call<List<Customer>> getAllCustomer(@Header("api-key") String api_key);

    @GET("customer/{CustID}")
    Call<Customer> getCustomer(@Header("api-key") String api_key, @Path("CustID") int CustID);


    @DELETE("customer/{CustID}")
    Call<DeleteResponse> deleteCustomer(@Header ("api-key") String apiKey, @Path("CustID") int CustID);

    @FormUrlEncoded
    @POST("customer")
    Call<Customer> register (@Field("CustName") String CustName,
                        @Field("CustPassword") String CustPassword,
                        @Field("CustEmail") String CustEmail,
                        @Field("CustContact") String CustContact,
                        @Field("CustAddress") String CustAddress);



    @FormUrlEncoded
    @POST("customer/{CustID}")
    Call<Customer> updateCustomer(@Header ("api-key") String apiKey, @Path("CustID") int CustID,
                        @Field("CustName") String CustName,
                        @Field("CustPassword") String CustPassword,
                        @Field("CustEmail") String CustEmail,
                        @Field("CustContact") String CustContact,
                        @Field("CustAddress") String CustAddress);


}
