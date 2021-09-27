package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class CreateDoctor implements Initializable {
    @FXML ComboBox<String> profileName;
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }
    Notification alert = new Notification();

    private String firstname, lastname, email, username, password,
            designation, specialities, phone, department, dbhost, dbuser, dbname, dbport, dbpass;
    private int id;
    @FXML private TextField firstnameField;
    @FXML private TextField lastnameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;
    @FXML private TextField designationField;
    @FXML private TextField specialitiesField;
    @FXML private ComboBox<String> departmentOptions;
    ObservableList<String> doctorDepartments = FXCollections.observableArrayList();

    public CreateDoctor(){
        this.firstnameField = new TextField();
        this.lastnameField = new TextField();
        this.emailField = new TextField();
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        this.phoneField = new TextField();
        this.designationField = new TextField();
        this.specialitiesField = new TextField();
        this.profileName= new ComboBox<>();
    }
    private static int getRandomID(int min, int max){
        int x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }
    private ArrayList<String> phonelist = new ArrayList<>();
    private ArrayList<String> usernameList = new ArrayList<>();
    private void fetchDBData() throws IOException {
        DatabaseHandler dbdata = new DatabaseHandler();

        dbhost = dbdata.getDbhost();
        dbname = dbdata.getDbname();
        dbuser = dbdata.getDbuser();
        dbpass = dbdata.getDbpass();
        if ((dbdata.getDbport().isEmpty()||dbdata.getDbport()==null))
            dbport = "3306";
        else{
            dbport=dbdata.getDbport();
        }
    }
    private void getDepartmentNames(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from Departments");
            while (rs.next()){
                doctorDepartments.add(rs.getString("name"));
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML private void createBTNAction() throws IOException {
        firstname = firstnameField.getText();
        lastname = lastnameField.getText();
        email = emailField.getText();
        username = usernameField.getText();
        password = passwordField.getText();
        phone = phoneField.getText();
        designation=designationField.getText();
        specialities=specialitiesField.getText();
        id = getRandomID(1000,9999);
        department = departmentOptions.getValue();

        if(firstnameField.getText().isEmpty()|| lastnameField.getText().isEmpty()
                || emailField.getText().isEmpty()||usernameField.getText().isEmpty()
                ||passwordField.getText().isEmpty()||phoneField.getText().isEmpty()
        ||designationField.getText().isEmpty()||specialitiesField.getText().isEmpty()||departmentOptions.getValue().equals(null)){
            alert.emptyData();
        }
        else {
            fetchDBData();
            String postdata="insert into Doctors values(" + id + ",'" + firstname + "','" + lastname + "','" + username + "','" + password + "','" + phone +"','"+designation+"','"+specialities+"','"+email+"','"+department+"')";
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * from Doctors");
                while (rs.next()){
                    int tempid = rs.getInt("id");
                    phonelist.add(rs.getString("phone"));
                    usernameList.add(rs.getString("username"));
                    if (id==tempid){
                        id=getRandomID(1000,9999);
                    }
                }
                ResultSet rs2 = statement.executeQuery("SELECT * from Doctors");
                ArrayList<String> patientUsernames = new ArrayList<>();
                ArrayList<String> frontUsernames = new ArrayList<>();
                while (rs2.next()){
                    patientUsernames.add(rs2.getString("username"));
                }
                ResultSet rs3 = statement.executeQuery("SELECT * from Doctors");
                while (rs3.next()){
                    frontUsernames.add(rs3.getString("username"));
                }
                if (phonelist.contains(phoneField.getText())){
                    alert.duplicatePhone();
                }
                else if(usernameList.contains(usernameField.getText())||patientUsernames.contains(usernameField.getText())
                ||frontUsernames.contains(usernameField.getText())){
                    alert.duplicateUsername();
                }
                else {

                    firstnameField.clear();
                    lastnameField.clear();
                    emailField.clear();
                    phoneField.clear();
                    usernameField.clear();
                    passwordField.clear();
                    designationField.clear();
                    specialitiesField.clear();
                    statement.executeUpdate(postdata);
                    alert.accountCreated();


                }
                connection.close();
            } catch (SQLException throwables) {
                alert.emptyData();
                throwables.printStackTrace();
            }
        }
    }
    @FXML private void createDoctorBTNAction() throws IOException {
        App.setRoot("CreateDoctor");
    }
    @FXML private void createFrontBTNAction() throws IOException {
        App.setRoot("CreateFrontDeskUser");
    }
    @FXML private void createRoutineBTNAction() throws IOException {
        App.setRoot("CreateRoutineCentralAdmin");
    }
    @FXML private void settingsBTNAction() throws IOException {
        App.setRoot("SettingsCentralAdmin");
    }
    @FXML private void bookBTNAction() throws IOException {
        App.setRoot("BookAppointmentCentralAdmin");
    }
    @FXML private void createPatientBTNAction() throws IOException {
        App.setRoot("CreatePatientCentralAdmin");
    }
    @FXML private void createDepartmentBTNAction() throws IOException {
        App.setRoot("Departments");
    }
    @FXML private void dashboardBTNAction() throws IOException {
        App.setRoot("CentralDashboard");
    }
    @FXML private void appointmentsBTNAction() throws IOException {
        App.setRoot("AllAppointmentsCentral");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileName.setItems(profileOptions);
        try {
            fetchDBData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getDepartmentNames();

        departmentOptions.setItems(doctorDepartments);
    }
}
