package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class CreatePatientCentralAdmin implements Initializable {
    @FXML ComboBox<String> profileName;
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }
    Notification alert = new Notification();
    private String firstname, lastname, username, email, phone, password, address,dbhost,dbname,dbport,dbuser,dbpass;
    Random rd = new Random();
    private int id=rd.nextInt();
    @FXML private TextField firstnameField, lastnameField, emailField,
            phoneField, usernameField;
    @FXML private TextArea addressField;
    @FXML private PasswordField passwordField;


    public CreatePatientCentralAdmin() {
        this.firstnameField = new TextField();
        this.lastnameField = new TextField();
        this.emailField = new TextField();
        this.phoneField = new TextField();
        this.addressField = new TextArea();
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
    }
    private static int getRandomID(int min, int max){
        int x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }

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

    @FXML private void createBTNAction() throws IOException, SQLException {
        firstname=firstnameField.getText();
        lastname=lastnameField.getText();
        username=usernameField.getText();
        email=emailField.getText();
        phone=phoneField.getText();
        address=addressField.getText();
        password=passwordField.getText();

        if (firstnameField.getText().isEmpty()||lastnameField.getText().isEmpty()
                ||usernameField.getText().isEmpty()||emailField.getText().isEmpty()||phoneField.getText().isEmpty()
                ||addressField.getText().isEmpty()||passwordField.getText().isEmpty()){
            alert.emptyData();
        }
        else {
            postPatient();
            alert.accountCreated();
            firstnameField.clear();
            lastnameField.clear();
            emailField.clear();
            phoneField.clear();
            usernameField.clear();
            passwordField.clear();
            addressField.clear();
        }
    }

    private void postPatient() throws SQLException, IOException {
        id = getRandomID(10000,99999999);
        fetchDBData();
        Patient patient = new Patient(id,firstname,lastname,username,password,phone,address,email);
        String postPatientData="insert into Patients values(" + id + ",'" + firstname + "','" + lastname + "','" + username + "','" + password + "','" + phone +"','"+address+"','"+email+"')";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement= connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id from Patients");
            while (rs.next()){
                int tempID=rs.getInt("id");
                if (tempID==id){
                    id = getRandomID(10000,99999999);
                }
            }
            statement.executeUpdate(postPatientData);
            alert.accountCreated();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
    }
}
