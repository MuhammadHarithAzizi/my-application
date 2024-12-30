package com.example.lab_rest.model;

public class Maintenance {

    private int maintenanceID;

    private String maintenanceDate;
    private String updateMaintenanceDate;

    private String type;
    private double cost;

    public Maintenance() {
    }

    public Maintenance(int maintenanceID, String maintenanceDate, String updateMaintenanceDate, String type, double cost) {
        this.maintenanceID = maintenanceID;
        this.maintenanceDate = maintenanceDate;
        this.updateMaintenanceDate = updateMaintenanceDate;
        this.type = type;
        this.cost = cost;
    }

    public int getMaintenanceID() {
        return maintenanceID;
    }

    public void setMaintenanceID(int maintenanceID) {
        this.maintenanceID = maintenanceID;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getUpdateMaintenanceDate() {
        return updateMaintenanceDate;
    }

    public void setUpdateMaintenanceDate(String updateMaintenanceDate) {
        this.updateMaintenanceDate = updateMaintenanceDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return type;
    }
}