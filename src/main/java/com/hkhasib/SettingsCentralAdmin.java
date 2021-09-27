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

public class SettingsCentralAdmin implements Initializable {
    @FXML private TextField usernameField, firstnameField, lastnameField, emailField, phoneField, dbhostField, dbnameField, dbportField, dbuserField;
    @FXML private PasswordField passwordField, dbpasswordField;
    @FXML private CheckBox usernameBox, passwordBox, dbBox, adminBox;
    @FXML private Button adminchangeBTN, inforchangeBTN, dbchangebTN;
    @FXML ComboBox<String> profileName;
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }

    private Notification alert = new Notification();

    private String username, password, dbhost, dbname, dbport, dbpass, dbuser, firstname, lastname, email,phone;
    private int adminID;

    public SettingsCentralAdmin() {
        this.usernameField = new TextField();
        this.firstnameField = new TextField();
        this.lastnameField = new TextField();
        this.emailField = new TextField();
        this.phoneField = new TextField();
        this.dbhostField = new TextField();
        this.dbnameField = new TextField();
        this.dbportField = new TextField();
        this.dbuserField = new TextField();
        this.passwordField = new PasswordField();
        this.dbpasswordField = new PasswordField();
        this.usernameBox = new CheckBox();
        this.passwordBox = new CheckBox();
        this.dbBox = new CheckBox();
        this.adminBox = new CheckBox();
        this.adminchangeBTN = new Button();
        this.inforchangeBTN = new Button();
        this.dbchangebTN = new Button();
    }

    @FXML private void adminCredentialFieldEnabler(){
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
            adminchangeBTN.setDisable(false);
        }
        else {
            adminchangeBTN.setDisable(true);
        }
    }
    @FXML private void fieldEnabler(){
        if (dbBox.isSelected()){
            dbhostField.setDisable(false);
            dbnameField.setDisable(false);
            dbportField.setDisable(false);
            dbpasswordField.setDisable(false);
            dbuserField.setDisable(false);
            dbchangebTN.setDisable(false);
        }
        else {
            dbhostField.setDisable(true);
            dbnameField.setDisable(true);
            dbportField.setDisable(true);
            dbpasswordField.setDisable(true);
            dbuserField.setDisable(true);
            dbchangebTN.setDisable(true);
        }
        if (adminBox.isSelected()){
            firstnameField.setDisable(false);
            lastnameField.setDisable(false);
            emailField.setDisable(false);
            phoneField.setDisable(false);
            inforchangeBTN.setDisable(false);
        }
        else {
            usernameField.setDisable(true);
            firstnameField.setDisable(true);
            lastnameField.setDisable(true);
            emailField.setDisable(true);
            phoneField.setDisable(true);
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
            ResultSet central = statement.executeQuery("SELECT * from centraladmin");
            central.next();
            usernameField.setText(central.getString("username"));
            passwordField.setText(central.getString("password"));
            firstnameField.setText(central.getString("firstname"));
            lastnameField.setText(central.getString("lastname"));
            emailField.setText(central.getString("email"));
            adminID=central.getInt("id");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dbuserField.setText(dbuser);
        dbpasswordField.setText(dbpass);
        dbportField.setText(dbport);
        dbnameField.setText(dbname);
        dbhostField.setText(dbhost);
    }

    @FXML private void changeDB() throws IOException {
        Properties properties = new Properties();
        OutputStream os = new FileOutputStream("dbConfig.properties");
        properties.setProperty("dbhost",dbhostField.getText());
        properties.setProperty("dbname",dbnameField.getText());
        properties.setProperty("port",dbportField.getText());
        properties.setProperty("dbuser",dbuserField.getText());
        properties.setProperty("dbpass",dbpasswordField.getText());
        properties.store(os,null);
        os.close();
    }

    @FXML private void changeAdminCredentials() throws SQLException {
        firstname=firstnameField.getText();
        lastname=lastnameField.getText();
        username=usernameField.getText();
        password=passwordField.getText();
        email=emailField.getText();
        String postdata= "UPDATE CentralAdmin Set id=" + adminID + ", firstname='" + firstname + "',lastname='" + lastname + "',username='" + username + "',password='" + password + "',email='" + email + "' where id="+adminID+"";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            statement.executeUpdate(postdata);
            connection.close();
            alert.informationUpdated();
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
        try {
            fetchDBData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fieldFiller();
    }
}
