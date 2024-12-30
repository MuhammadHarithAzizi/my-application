package com.example.lab_rest.model;

public class Customer {


    private int CustID;
    private int id;
    private String CustName;
    private String CustPassword;
    private String CustEmail;
    private String CustContact;
    private String CustAddress;

    public Customer() {
    }


    public Customer(int custID, int id, String custName, String custPassword, String custEmail, String custContact, String custAddress) {
        CustID = custID;
        this.id = id;
        CustName = custName;
        CustPassword = custPassword;
        CustEmail = custEmail;
        CustContact = custContact;
        CustAddress = custAddress;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getCustPassword() {
        return CustPassword;
    }

    public void setCustPassword(String custPassword) {
        CustPassword = custPassword;
    }

    public String getCustEmail() {
        return CustEmail;
    }

    public void setCustEmail(String custEmail) {
        CustEmail = custEmail;
    }

    public String getCustContact() {
        return CustContact;
    }

    public void setCustContact(String custContact) {
        CustContact = custContact;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "CustID=" + CustID +
                ", id=" + id +
                ", CustName='" + CustName + '\'' +
                ", CustPassword='" + CustPassword + '\'' +
                ", CustEmail='" + CustEmail + '\'' +
                ", CustContact='" + CustContact + '\'' +
                ", CustAddress='" + CustAddress + '\'' +
                '}';
    }
}


