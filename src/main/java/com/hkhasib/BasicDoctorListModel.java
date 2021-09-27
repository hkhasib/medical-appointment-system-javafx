package com.hkhasib;

public class BasicDoctorListModel {
    private String name, phone, department;
    private int id;

    public BasicDoctorListModel(int id, String name, String phone, String department) {
        this.id=id;
        this.name = name;
        this.phone = phone;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
