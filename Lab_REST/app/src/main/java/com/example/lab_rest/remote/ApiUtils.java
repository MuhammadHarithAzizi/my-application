package com.example.lab_rest.remote;

public class ApiUtils {

    // REST API server URL
    public static final String BASE_URL = "http://178.128.220.20/2023189433/api/";

    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }


    public static BookingService getBookingService() {
        return RetrofitClient.getClient(BASE_URL).create(BookingService.class);
    }
    public static CarService getCarService() {
        return RetrofitClient.getClient(BASE_URL).create(CarService.class);
    }
    public static MaintenanceService getMaintenanceService() {
        return RetrofitClient.getClient(BASE_URL).create(MaintenanceService.class);
    }

    public static CustomerService getCustomerService() {
        return RetrofitClient.getClient(BASE_URL).create(CustomerService.class);
    }



}
