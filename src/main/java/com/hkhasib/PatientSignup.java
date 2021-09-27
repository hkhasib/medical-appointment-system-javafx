package com.hkhasib;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class PatientSignup implements Initializable {
    @FXML private TextField firstnameField, lastnameField, usernameField, emailField, phoneField, answerField;
    @FXML private TextArea addressField;
    @FXML private PasswordField passwordField;
    @FXML private Text captchaText;
    public PatientSignup() {
        this.firstnameField = new TextField();
        this.lastnameField = new TextField();
        this.usernameField = new TextField();
        this.emailField = new TextField();
        this.phoneField = new TextField();
        this.answerField = new TextField();
        this.addressField = new TextArea();
        this.passwordField = new PasswordField();
        this.captchaText = new Text();
    }

    private static int randomNumberGenerator(int min, int max){
        int x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }

    private int a, b, ans, id, duplicatePhone=0, duplicateUser=0;

    private String firstname, lastname, username, password, email, phone, answer, question, address;
    private String dbhost,dbname,dbport,dbuser,dbpass;

    private Notification alert = new Notification();

    private void createCaptcha(){
        a=randomNumberGenerator(0,100);
        b=randomNumberGenerator(0,100);
        question=a+" + "+b+" = ?";
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

    private void createPatient() throws IOException {
        id = randomNumberGenerator(10000,99999999);
        ArrayList<String> phonelist= new ArrayList<>();
        ArrayList<String> usernamelist= new ArrayList<>();
        fetchDBData();
        Patient patient = new Patient(id,firstname,lastname,username,password,phone,address,email);
        String postPatientData="insert into Patients values(" + id + ",'" + firstname + "','" + lastname + "','" + username + "','" + password + "','" + phone +"','"+address+"','"+email+"')";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement= connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from Patients");
            while (rs.next()){
                int tempID=rs.getInt("id");
                if (tempID==id){
                    id = randomNumberGenerator(10000,99999999);
                }
                String tempUsername=rs.getString("username").toLowerCase();
                String tempPhone=rs.getString("phone");
                usernamelist.add(tempUsername);
                phonelist.add(tempPhone);
                if (phonelist.contains(phoneField.getText())){
                    duplicatePhone=1;
                }
                if (usernamelist.contains(usernameField.getText().toLowerCase())){
                    duplicateUser=1;
                }
            }
            if (duplicateUser==1){
                alert.duplicateUsername();
            }
            else if (duplicatePhone==1){
                alert.duplicatePhone();
            }
            else {
                statement.executeUpdate(postPatientData);
                alert.accountCreated();
                duplicatePhone=0;
                duplicateUser=0;
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void resetFields(){
        usernameField.clear();
        firstnameField.clear();
        lastnameField.clear();
        passwordField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        answerField.clear();
        captchaText.setText(null);
        createCaptcha();
        captchaText.setText(question);
    }

    @FXML private void signupButtonAction() throws IOException {
        firstname=firstnameField.getText();
        lastname=lastnameField.getText();
        username=usernameField.getText();
        password=passwordField.getText();
        email=emailField.getText();
        phone=phoneField.getText();
        address=addressField.getText();
        answer=answerField.getText();
        if (firstnameField.getText().isEmpty()||lastnameField.getText().isEmpty()||usernameField.getText().isEmpty()
        ||passwordField.getText().isEmpty()||emailField.getText().isEmpty()||phoneField.getText().isEmpty()
        ||addressField.getText().isEmpty()||answerField.getText().isEmpty()){
            alert.emptyData();
        }
        else if(a+b!=Integer.parseInt(answer)){
            alert.wrongCaptcha();
        }
        else {
            createPatient();
            resetFields();
        }
    }

    @FXML private void returnLoginPage() throws IOException {
        App.setRoot("LoginPage");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createCaptcha();
        captchaText.setText(question);
    }
}
