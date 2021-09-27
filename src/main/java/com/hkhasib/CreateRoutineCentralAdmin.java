package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.Properties;
import java.util.ResourceBundle;
import com.jfoenix.controls.*;

public class CreateRoutineCentralAdmin implements Initializable {

    @FXML ComboBox<String> profileName;



    Notification alert = new Notification();

    private String dbhost, dbuser, dbname, dbport, dbpass;

    private String weekDay, tempWeekDay, doctorUsername, tempDoctorID, doctorID, firstname, lastname, phone, email;
    private LocalTime fromTime, toTime, tempFromTime, tempToTime, finalFromTime;


    @FXML private TextField idField, nameField, usernameField, phoneField, emailField;

    public CreateRoutineCentralAdmin(){
        this.idField=new TextField();
        this.nameField=new TextField();
        this.usernameField = new TextField();
        this.phoneField = new TextField();
        this.emailField = new TextField();
        this.profileName = new ComboBox<>();
    }
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");


    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }

    @FXML private ComboBox<String> doctorSelector, daySelector, fromTimeSelector, toTimeSelector;

    ObservableList<String> doctorNames = FXCollections.observableArrayList();

    ObservableList<String> daynames = FXCollections.observableArrayList("Saturday","Sunday","Monday",
            "Tuesday","Wednesday","Thursday","Friday");
    ObservableList<String> fromTimes = FXCollections.observableArrayList("00:00:00","01:00:00","02:00:00",
            "03:00:00","04:00:00","05:00:00","06:00:00","07:00:00","08:00:00","09:00:00","10:00:00",
            "11:00:00","12:00:00","13:00:00","14:00:00","15:00:00","16:00:00","17:00:00","18:00:00",
            "19:00:00","20:00:00","21:00:00","22:00:00","23:00:00");
    ObservableList<String> toTimes = FXCollections.observableArrayList("00:00:00","01:00:00","02:00:00",
            "03:00:00","04:00:00","05:00:00","06:00:00","07:00:00","08:00:00","09:00:00","10:00:00",
            "11:00:00","12:00:00","13:00:00","14:00:00","15:00:00","16:00:00","17:00:00","18:00:00",
            "19:00:00","20:00:00","21:00:00","22:00:00","23:00:00");

    private void getDBData() throws IOException {
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
    }

    private void timeDetector(){
        fromTime = LocalTime.parse(fromTimeSelector.getValue());
        toTime = LocalTime.parse(toTimeSelector.getValue());
    }



    @FXML private void doctorDetails(){
        doctorUsername=doctorSelector.getValue();
        //String fetchDoctorID="Select id from Doctors where username="+doctorUsername+"";
        String getDoctorDetails="Select id, firstname, lastname, phone, email from Doctors where username='"+doctorUsername+"'";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getDoctorDetails);

            while (rs.next()){
                doctorID = rs.getString("id");
                firstname = rs.getString("firstname");
                lastname = rs.getString("lastname");
                phone =rs.getString("phone");
                email =rs.getString("email");
            }

            idField.setText(doctorID);
            nameField.setText(firstname+" "+lastname);
            usernameField.setText(doctorUsername);
            phoneField.setText(phone);
            emailField.setText(email);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML private void addPatientButton() throws IOException {
        App.setRoot("CreatePatient");
    }

    private void postRoutine() throws IOException {



        String postRoutine="insert into Routines values(" + doctorID + ",'" + fromTime + "','" + toTime + "','" + weekDay + "')";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            statement.executeUpdate(postRoutine);
            alert.accountCreated();
            connection.close();
            alert.routineAdded();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML private void addButtonAction() throws IOException {

        if (doctorSelector.getValue()==null||daySelector.getValue()==null
                ||fromTimeSelector.getValue()==null||toTimeSelector.getValue()==null){
            alert.emptyData();
        }
        else {
            doctorUsername=doctorSelector.getValue();
            weekDay = daySelector.getValue();
            getDBData();
            timeDetector();
            if (fromTime.isAfter(toTime)){
                alert.timeConflict();
            }
            else {
                String getRoutineDetails="Select doctorID, fromTime, toTime, day from Routines where doctorID='"+doctorID+"'";
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(getRoutineDetails);

                    int duplicate = 0;

                    while (rs.next()){
                        tempDoctorID = rs.getString("doctorID");
                        tempFromTime = rs.getTime("fromTime").toLocalTime();
                        tempToTime = rs.getTime("toTime").toLocalTime();
                        tempWeekDay = rs.getString("day");
                        if (tempDoctorID.equals(doctorID)&&tempWeekDay.equals(weekDay)){
                            duplicate =1;
                        }
                    }
                    if (duplicate==1){
                        alert.duplicateRoutine();
                    }
                    else {
                        LocalTime i = fromTime;
                        finalFromTime=fromTime;

                        while (i.isBefore(toTime)){
                            i=i.plusMinutes(30);
                            statement.executeUpdate("insert into Routines values(" + doctorID + ",'" + finalFromTime + "','" + i + "','" + weekDay + "')");
                            finalFromTime=i;
                            System.out.println(finalFromTime);
                        }
                        alert.routineAdded();

                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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
        daySelector.setItems(daynames);
        fromTimeSelector.setItems(fromTimes);
        toTimeSelector.setItems(toTimes);
        String readdata="Select * from Doctors";

        try {
            getDBData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(readdata);
            while(rs.next()){
                doctorNames.add(rs.getString("username"));
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        doctorSelector.setItems(doctorNames);

    }
}
