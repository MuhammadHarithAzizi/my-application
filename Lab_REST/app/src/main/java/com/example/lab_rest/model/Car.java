package com.example.lab_rest.model;

import java.sql.Timestamp;

public class Car {
    private int CarID;
    private String Model;
    private String Brand;
    private String PlateNumber;
    private String availability;
    private String image;
    private String CreatedAt;
    private Maintenance maintenance;
    private int maintenance_id; // Added field

    public Car() {
    }

    public Car(int carID, String model, String brand, String plateNumber, String availability, String image, String createdAt, Maintenance maintenance, int maintenance_id) {
        this.CarID = carID;
        this.Model = model;
        this.Brand = brand;
        this.PlateNumber = plateNumber;
        this.availability = availability;
        this.image = image;
        this.CreatedAt = createdAt;
        this.maintenance = maintenance;
        this.maintenance_id = maintenance_id;
    }

    public int getCarID() {
        return CarID;
    }

    public void setCarID(int carID) {
        CarID = carID;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public int getMaintenance_id() {
        return maintenance_id;
    }

    public void setMaintenance_id(int maintenance_id) {
        this.maintenance_id = maintenance_id;
    }
    public boolean hasMaintenance() {
        return maintenance != null; // or maintenance.getId() != 0 if maintenance is an ID
    }
    @Override
    public String toString() {
        return Model;
    }
}
