package com.hkhasib;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CreateAdmin {

    Notification alert = new Notification();

    private String id="1",firstname, lastname, email, username, password,
            dbhost, dbuser, dbname, dbport, dbpass;

    @FXML private TextField firstnameField;
    @FXML private TextField lastnameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public CreateAdmin(){
        this.firstnameField = new TextField();
        this.lastnameField = new TextField();
        this.emailField = new TextField();
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
    }

    @FXML private void createButtonAction() throws IOException {
        firstname = firstnameField.getText();
        lastname = lastnameField.getText();
        email = emailField.getText();
        username = usernameField.getText();
        password = passwordField.getText();
        int id=1;

        if(firstnameField.getText().isEmpty()|| lastnameField.getText().isEmpty()
        || emailField.getText().isEmpty()||usernameField.getText().isEmpty()
        ||passwordField.getText().isEmpty()){
            alert.emptyData();
        }
        else {
            File file = new File("dbConfig.properties");
            Properties p = new Properties();
            InputStream is = new FileInputStream("dbConfig.properties");
            p.load(is);
            dbhost = p.getProperty("dbhost");
            dbname = p.getProperty("dbname");
            dbuser = p.getProperty("dbuser");
            dbpass = p.getProperty("dbpass");
            if ((p.getProperty("port").isEmpty())||(p.getProperty("port")==null))
            dbport = "3306";
            else{
                dbport=p.getProperty("dbport");
            }
            String postdata="insert into CentralAdmin values(" + id + ",'" + firstname + "','" + lastname + "','" + username + "','" + password + "','" + email +"')";
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
                Statement statement = connection.createStatement();
                statement.executeUpdate(postdata);
                alert.accountCreated();
                connection.close();
                App.setRoot("LoginPage");
            } catch (SQLException throwables) {
                alert.emptyData();
                throwables.printStackTrace();
            }
        }
    }
}
