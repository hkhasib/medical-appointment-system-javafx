package com.hkhasib;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {

    private int loginType =0;
    public static String loginName;

    Notification alert = new Notification();

    @FXML private CheckBox doctorChecker, patientChecker, frontChecker;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBTN, redButton, signupBTN, adminBTN;
    private String username, password,tempusername, tempPassword, dbhost,dbname,dbuser,dbpass,dbport,readDB;
    private String nextPage;

    public LoginPage(){
        this.patientChecker = new CheckBox();
        this.doctorChecker = new CheckBox();
        this.frontChecker = new CheckBox();
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        this.loginBTN = new Button();
        this.redButton = new Button();
        this.signupBTN= new Button();
        this.adminBTN=new Button();
    }

    @FXML private void loginTypeChecker() throws IOException {
        if (patientChecker.isSelected()){
            doctorChecker.setDisable(true);
            frontChecker.setDisable(true);
            usernameField.setDisable(false);
            passwordField.setDisable(false);
            loginBTN.setDisable(false);
            loginType=1;
            readDB="Select password,username from Patients where username='"+username+"'";
            nextPage="PatientDashboard";
        }

        else if (doctorChecker.isSelected()){
            patientChecker.setDisable(true);
            frontChecker.setDisable(true);
            usernameField.setDisable(false);
            passwordField.setDisable(false);
            loginBTN.setDisable(false);
            loginType=2;
            readDB="Select * from Doctors where username='"+username+"'";
            nextPage="DoctorDashboard";
        }
        else if (frontChecker.isSelected()){
            patientChecker.setDisable(true);
            doctorChecker.setDisable(true);
            usernameField.setDisable(false);
            passwordField.setDisable(false);
            loginBTN.setDisable(false);
            loginType=3;
            readDB="Select * from FrontDeskUsers where username='"+username+"'";
            nextPage="FrontDeskDashboard";
        }
        else {
            patientChecker.setDisable(false);
            doctorChecker.setDisable(false);
            frontChecker.setDisable(false);
            usernameField.setDisable(true);
            passwordField.setDisable(true);
            loginBTN.setDisable(true);
            loginType=0;
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


    private void login() throws SQLException, IOException {
        fetchDBData();
        loginTypeChecker();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(readDB);
            while (resultSet.next()){
                tempPassword=resultSet.getString("password");
                tempusername=resultSet.getString("username");
            }
            if (username.equals(tempusername)&&password.equals(tempPassword)){
                App.setRoot(""+nextPage+"");

            }
            else {
                alert.wrongCredentials();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            alert.wrongCredentials();
        }
    }

    @FXML private void loginBTNAction() throws IOException, SQLException {
        username=usernameField.getText();
        password=passwordField.getText();

        if (usernameField.getText().isEmpty()||passwordField.getText().isEmpty()){
         alert.emptyData();
        }
        else {
            loginName = usernameField.getText();
            login();
        }
    }
    @FXML private void connectDB() throws IOException {
            App.setRoot("DatabaseConnector");
    }
    private void configCheck(){
        File file = new File("dbConfig.properties");
        if (!file.exists()){
            redButton.setVisible(true);
            patientChecker.setDisable(true);
            doctorChecker.setDisable(true);
            frontChecker.setDisable(true);
            adminBTN.setDisable(true);
            signupBTN.setDisable(true);
        }
        else{
            redButton.setVisible(false);
            adminBTN.setDisable(false);
            signupBTN.setDisable(false);
            patientChecker.setDisable(false);
            doctorChecker.setDisable(false);
            frontChecker.setDisable(false);
        }
    }

    @FXML private void signupPage() throws IOException {
        App.setRoot("PatientSignup");
    }
    @FXML private void centralLoinPage() throws IOException {
        App.setRoot("centralLogin");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configCheck();

    }
}
