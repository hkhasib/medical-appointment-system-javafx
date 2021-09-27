package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;

import javafx.scene.control.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class PatientSettings implements Initializable {

    String dashboardUser=LoginPage.loginName;

    @FXML ComboBox<String> profileName;
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }

    @FXML private TextField usernameField, firstnameField, lastnameField, emailField, phoneField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox usernameBox, passwordBox, profileDetailsBox;
    @FXML private Button changeCredentialBTN, inforchangeBTN;
    @FXML private TextArea addressField;

    private Notification alert = new Notification();

    private String username, password, dbhost, dbname, dbport, dbpass, dbuser, firstname, lastname, email,phone,address;
    private int userID;

    public PatientSettings() {
        this.usernameField = new TextField();
        this.firstnameField = new TextField();
        this.lastnameField = new TextField();
        this.emailField = new TextField();
        this.phoneField = new TextField();
        this.passwordField = new PasswordField();
        this.usernameBox = new CheckBox();
        this.passwordBox = new CheckBox();
        this.profileDetailsBox = new CheckBox();
        this.changeCredentialBTN = new Button();
        this.inforchangeBTN = new Button();
        this.inforchangeBTN = new Button();
        this.addressField=new TextArea();
        this.profileName=new ComboBox<>();
    }

    @FXML private void credentialFieldEnabler(){
        if (passwordBox.isSelected()){
            passwordField.setDisable(false);
        }
        else {
            passwordField.setDisable(true);
        }
        if (usernameBox.isSelected()){
            usernameField.setDisable(false);
        }
        else {
            usernameField.setDisable(true);
        }
        if (usernameBox.isSelected()||passwordBox.isSelected()||(usernameBox.isSelected()&&passwordBox.isSelected())){
            changeCredentialBTN.setDisable(false);
        }
        else {
            changeCredentialBTN.setDisable(true);
        }
    }
    @FXML private void fieldEnabler(){

        if (profileDetailsBox.isSelected()){
            firstnameField.setDisable(false);
            lastnameField.setDisable(false);
            emailField.setDisable(false);
            phoneField.setDisable(false);
            addressField.setDisable(false);
            inforchangeBTN.setDisable(false);
        }
        else {
            usernameField.setDisable(true);
            firstnameField.setDisable(true);
            lastnameField.setDisable(true);
            emailField.setDisable(true);
            phoneField.setDisable(true);
            addressField.setDisable(true);
            inforchangeBTN.setDisable(true);
        }
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

    private void fieldFiller(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from patients where username='"+dashboardUser+"'");
            rs.next();
            usernameField.setText(rs.getString("username"));
            passwordField.setText(rs.getString("password"));
            firstnameField.setText(rs.getString("firstname"));
            lastnameField.setText(rs.getString("lastname"));
            emailField.setText(rs.getString("email"));
            phoneField.setText(rs.getString("phone"));
            addressField.setText(rs.getString("address"));
            userID=rs.getInt("id");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML private void changeCredentials() throws SQLException {
        firstname=firstnameField.getText();
        lastname=lastnameField.getText();
        username=usernameField.getText();
        password=passwordField.getText();
        email=emailField.getText();
        phone=phoneField.getText();
        address=addressField.getText();
        String postdata= "UPDATE patients Set id=" + userID + ", firstname='" + firstname + "',lastname='" + lastname + "',username='" + username + "',password='" + password + "'phone='" + phone + "',address='" + address + "'email='" + email + "' where id="+userID+"";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            if (firstname!=null&&lastname!=null&&username!=null&&phone!=null
            &&password!=null&&email!=null&&address!=null){
                statement.executeUpdate(postdata);
            }
            else {
                alert.emptyData();
            }

            connection.close();
            alert.informationUpdated();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML private void bookAppointment() throws IOException {
        App.setRoot("BookAppointmentPatient");
    }
    @FXML private void allAppointments() throws IOException {
        App.setRoot("AllAppointmentsPatient");
    }
    @FXML private void settingsPage() throws IOException {
        App.setRoot("PatientSettings");
    }
    @FXML private void returnDashboardAction() throws IOException {

            App.setRoot("PatientDashboard");
        }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            fetchDBData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fieldFiller();

        profileName.setPromptText(dashboardUser);


        profileName.setItems(profileOptions);
    }
}
