package com.hkhasib;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javafx.scene.layout.Pane;

public class DatabaseConnector {
    private String dbhost, dbname, dbuser, dbpassword, dbport="3306";

    Notification notification = new Notification();

    @FXML private TextField dbhostField, dbnameField, dbuserField, dbportField;
    @FXML private PasswordField dbpasswordField;
    @FXML private Pane profilePic;


    public DatabaseConnector(){
        this.dbhostField =  new TextField();
        this.dbuserField = new TextField();
        this.dbnameField = new TextField();
        this.dbportField = new TextField();
        this.dbpasswordField = new PasswordField();
        this.profilePic = new Pane();
    }

    private void dbdataSave() throws IOException {
        Properties properties = new Properties();
        OutputStream os = new FileOutputStream("dbConfig.properties");
        properties.setProperty("dbhost",dbhost);
        properties.setProperty("dbname",dbname);
        properties.setProperty("port",dbport);
        properties.setProperty("dbuser",dbuser);
        properties.setProperty("dbpass",dbpassword);
        properties.store(os,null);
        os.close();
    }
    private void clearData(){
        dbhostField.clear();
        dbuserField.clear();
        dbpasswordField.clear();
        dbportField.clear();
        dbnameField.clear();
    }

    /*@FXML private void changePic(){
        String image = "https://cdn.pixabay.com/photo/2017/06/13/12/53/profile-2398782_960_720.png";
        profilePic.setStyle("-fx-background-image: url('" + image + "');\n" +
                "    -fx-background-size: 100%;\n" +
                "    -fx-background-position: center;\n" +
                "    -fx-background-repeat: no-repeat;\n" +
                "    -fx-background-radius: 18 18 18 18;\n" +
                "    -fx-border-radius: 18 18 18 18;");
    }*/
    @FXML private void connectDatabase(){
        dbhost=dbhostField.getText();
        dbname=dbnameField.getText();
        dbport=dbportField.getText();
        dbuser=dbuserField.getText();
        dbpassword=dbpasswordField.getText();
        if (dbhostField.getText().isEmpty()||dbnameField.getText().isEmpty()
        ||dbuserField.getText().isEmpty()||dbuserField.getText().isEmpty()){
            notification.emptyData();
        }
        else {
            //System.out.println();
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpassword);
                String centralAdminTable = "CREATE TABLE CentralAdmin (\n" +
                        "id INT(6) NOT NULL,\n" +
                        "firstname VARCHAR(30) NOT NULL,\n" +
                        "lastname VARCHAR(30) NOT NULL,\n" +
                        "username VARCHAR(30) NOT NULL,\n" +
                        "password VARCHAR(30) NOT NULL,\n" +
                        "email VARCHAR(50))";
                String adminTable = "CREATE TABLE Admins (\n" +
                        "id INT(6) NOT NULL,\n" +
                        "firstname VARCHAR(30) NOT NULL,\n" +
                        "lastname VARCHAR(30) NOT NULL,\n" +
                        "username VARCHAR(30) NOT NULL,\n" +
                        "password VARCHAR(30) NOT NULL,\n" +
                        "email VARCHAR(50))";
                String patientTable = "CREATE TABLE Patients (\n" +
                        "id INT(10) NOT NULL,\n" +
                        "firstname VARCHAR(30) NOT NULL,\n" +
                        "lastname VARCHAR(30) NOT NULL,\n" +
                        "username VARCHAR(30) NOT NULL,\n" +
                        "password VARCHAR(30) NOT NULL,\n" +
                        "phone VARCHAR(30) NOT NULL,\n" +
                        "address VARCHAR(100) NOT NULL,\n" +
                        "email VARCHAR(50))";
                String doctorTable = "CREATE TABLE Doctors (\n" +
                        "id INT(10) NOT NULL,\n" +
                        "firstname VARCHAR(30) NOT NULL,\n" +
                        "lastname VARCHAR(30) NOT NULL,\n" +
                        "username VARCHAR(30) NOT NULL,\n" +
                        "password VARCHAR(30) NOT NULL,\n" +
                        "phone VARCHAR(30) NOT NULL,\n" +
                        "designation VARCHAR(30) NOT NULL,\n" +
                        "specialities VARCHAR(150) NOT NULL,\n" +
                        "email VARCHAR(100) NOT NULL,\n"+
                        "department VARCHAR(50))";
                String frontDeskTable = "CREATE TABLE FrontDeskUsers (\n" +
                        "id INT(10) NOT NULL,\n" +
                        "firstname VARCHAR(30) NOT NULL,\n" +
                        "lastname VARCHAR(30) NOT NULL,\n" +
                        "username VARCHAR(30) NOT NULL,\n" +
                        "password VARCHAR(30) NOT NULL,\n" +
                        "phone VARCHAR(30) NOT NULL,\n" +
                        "email VARCHAR(50))";
                String appointmentTable = "CREATE TABLE Appointments (\n" +
                        "patientID INT(10) NOT NULL,\n" +
                        "doctorID INT(10) NOT NULL,\n" +
                        "date date NOT NULL,\n" +
                        "time time NOT NULL,\n" +
                        "note VARCHAR(300) NOT NULL,\n" +
                        "emergency VARCHAR(10))";
                String departmentTable = "CREATE TABLE Departments (\n" +
                        "name VARCHAR(30) NOT NULL)";
                String doctorRoutine = "CREATE TABLE Routines (\n" +
                        "doctorID INT(10) NOT NULL,\n" +
                        "fromTime time NOT NULL,\n" +
                        "toTime time NOT NULL,\n" +
                        "day VARCHAR(15))";
                Statement statement = connection.createStatement();
                statement.executeUpdate(centralAdminTable);
                statement.executeUpdate(patientTable);
                statement.executeUpdate(doctorTable);
                statement.executeUpdate(frontDeskTable);
                statement.executeUpdate(appointmentTable);
                statement.executeUpdate(doctorRoutine);
                statement.executeUpdate(adminTable);
                statement.executeUpdate(departmentTable);
                statement.executeUpdate("INSERT into departments values ('OTHERS'),('CARDIOLOGY'),('GYNAECOLOGY'),('NEPHROLOGY')");
                connection.close();
                dbdataSave();
                clearData();
                notification.successDB();
                App.setRoot("CreateAdmin");
            } catch (SQLException | IOException throwables) {
                notification.failureDB();
                throwables.printStackTrace();
            }
        }
    }
}
