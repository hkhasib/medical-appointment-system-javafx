package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateFrontDeskUser implements Initializable {
    @FXML
    ComboBox<String> profileName;
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }
    Notification alert = new Notification();

    private String firstname, lastname, email, username, password, phone, dbhost, dbuser, dbname, dbport, dbpass;
    private int id;
    @FXML
    private TextField firstnameField;
    @FXML private TextField lastnameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;

    public CreateFrontDeskUser(){
        this.firstnameField = new TextField();
        this.lastnameField = new TextField();
        this.emailField = new TextField();
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        this.phoneField = new TextField();
        this.profileName= new ComboBox<>();
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
    @FXML private void createBTNAction() throws IOException {



        firstname = firstnameField.getText();
        lastname = lastnameField.getText();
        email = emailField.getText();
        username = usernameField.getText();
        password = passwordField.getText();
        phone = phoneField.getText();
        id = getRandomID(10000,99999);

        if(firstnameField.getText().isEmpty()|| lastnameField.getText().isEmpty()
                || emailField.getText().isEmpty()||usernameField.getText().isEmpty()
                ||passwordField.getText().isEmpty()||phoneField.getText().isEmpty()){
            alert.emptyData();
        }
        else {
            fetchDBData();
            String postdata="insert into frontdeskusers values(" + id + ",'" + firstname + "','" + lastname + "','" + username + "','" + password + "','" + phone +"','"+email+"')";
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT id from frontDeskUsers");
                while (rs.next()){
                    int tempid = rs.getInt("id");
                    if (id==tempid){
                        id=getRandomID(10000,99999);
                    }
                }
                statement.executeUpdate(postdata);
                alert.accountCreated();
                firstnameField.clear();
                lastnameField.clear();
                emailField.clear();
                phoneField.clear();
                usernameField.clear();
                passwordField.clear();
                alert.accountCreated();
                connection.close();
            } catch (SQLException throwables) {
                alert.emptyData();
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileName.setItems(profileOptions);
    }
}
