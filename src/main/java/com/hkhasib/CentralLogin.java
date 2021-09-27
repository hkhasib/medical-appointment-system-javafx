package com.hkhasib;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;

public class CentralLogin {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private Notification alert= new Notification();

    private int securityRisk=0;

    public CentralLogin() {
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
    }

    private String username, password,dbhost, dbuser, dbname, dbport, dbpass, dayName;
    private String tempUsername, tempPassword;
    private void fetchDBData() throws IOException {
        DatabaseHandler dbdata = new DatabaseHandler();

        dbhost = dbdata.getDbhost();
        dbname = dbdata.getDbname();
        dbuser = dbdata.getDbuser();
        dbpass = dbdata.getDbpass();
        if ((dbdata.getDbport().isEmpty() || dbdata.getDbport() == null))
            dbport = "3306";
        else {
            dbport = dbdata.getDbport();
        }
    }

    @FXML private void loginBTNAction() throws IOException {
        username=usernameField.getText();
        password=passwordField.getText();

        fetchDBData();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from CentralAdmin");
            while (rs.next()){
                tempUsername=rs.getString("username");
                tempPassword=rs.getString("password");
            }
            if (tempUsername.equalsIgnoreCase(username)&&tempPassword.equals(password)){
                App.setRoot("CentralDashboard");
            }
            else {
             alert.wrongCredentials();
             securityRisk++;
            }
            if (securityRisk==3){
                alert.maximumAttemptWarning();
            }
            if (securityRisk>3){
                System.exit(0);
            }

            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML private void normalLoginPage() throws IOException {
        App.setRoot("LoginPage");
    }
}
