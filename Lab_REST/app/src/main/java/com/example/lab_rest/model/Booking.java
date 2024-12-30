package com.example.lab_rest.model;

import java.sql.Timestamp;

public class Booking {
    private int BookingId;
    private int UserId;
    private int CarId;
    private String BookingDate;
    private String Status;
    private String Remarks;
    private String AdminMsg;
    private String CreatedAt;

    public Booking() {
    }

    public Booking(int bookingId, int userId, int carId, String bookingDate, String status, String remarks, String adminMsg, String createdAt) {
        BookingId = bookingId;
        UserId = userId;
        CarId = carId;
        BookingDate = bookingDate;
        Status = status;
        Remarks = remarks;
        AdminMsg = adminMsg;
        CreatedAt = createdAt;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getCarId() {
        return CarId;
    }

    public void setCarId(int carId) {
        CarId = carId;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getAdminMsg() {
        return AdminMsg;
    }

    public void setAdminMsg(String adminMsg) {
        AdminMsg = adminMsg;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "BookingId=" + BookingId +
                ", UserId=" + UserId +
                ", CarId=" + CarId +
                ", BookingDate='" + BookingDate + '\'' +
                ", Status='" + Status + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", AdminMsg='" + AdminMsg + '\'' +
                ", CreatedAt='" + CreatedAt + '\'' +
                '}';
    }
}