package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CentralDashboard implements Initializable {

    private String dbhost, dbuser, dbname, dbport, dbpass, doctorName, designation, phone, day;
    private LocalTime fromTime, toTime;
    private int doctorID;
    private int doctorNumber=0, appointmentNumber=0, patientNumber=0;

    private BasicDoctorListModel basicDoctorListModel;
    private Routine routine;

    @FXML private Text totalDoctorText, upcomingAppointmentText, totalPatientsText;

    public CentralDashboard(){
        this.totalDoctorText = new Text();
        this.upcomingAppointmentText = new Text();
        this.totalPatientsText = new Text();
    }

    @FXML private TableView<BasicDoctorListModel> doctorTable;
    @FXML private TableColumn<BasicDoctorListModel, String> nameCol;
    @FXML private TableColumn<BasicDoctorListModel, String> phoneCol;
    @FXML private TableColumn<BasicDoctorListModel, String> depCol;
    private ObservableList<BasicDoctorListModel> doctorlist = FXCollections.observableArrayList();
    @FXML private TableView<Routine> routineTable;
    @FXML private TableColumn<Routine, String> dayCol;
    @FXML private TableColumn<Routine, LocalTime> fromCol;
    @FXML private TableColumn<Routine, LocalTime> toCol;
    private ObservableList<Routine> routines = FXCollections.observableArrayList();

    @FXML
    ComboBox<String> profileName;

    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }
    private void getStats(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet doctor = statement.executeQuery("SELECT * from Doctors");

            while (doctor.next()){
                doctorNumber++;
            }
            ResultSet patient = statement.executeQuery("SELECT * from patients");
            while (patient.next()){
                patientNumber++;
            }
            ResultSet appointment = statement.executeQuery("SELECT * from appointments where date>='"+LocalDate.now()+"'");
            while (appointment.next()){
                LocalDate date = appointment.getDate("date").toLocalDate();
                LocalTime time = appointment.getTime("time").toLocalTime().minusHours(6);
                LocalDateTime localDateTime = time.atDate(date);
                if (localDateTime.isAfter(LocalDateTime.now())){
                    appointmentNumber++;
                }
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

    private void getDoctorsData(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from Doctors");
            while (rs.next()){
                doctorName = rs.getString("firstname")+" "+rs.getString("lastname");
                basicDoctorListModel = new BasicDoctorListModel(rs.getInt("id"),doctorName,rs.getString("phone"),rs.getString("department"));
                doctorlist.add(basicDoctorListModel);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void routinChecker(){
        basicDoctorListModel = doctorTable.getSelectionModel().getSelectedItem();
        if (basicDoctorListModel!=null){
            phone=basicDoctorListModel.getPhone();
            doctorID=basicDoctorListModel.getId();
        }
        System.out.println(doctorID);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from Routines where doctorID="+doctorID+"");
            while (rs.next()){
                fromTime = rs.getTime("fromTime").toLocalTime().minusHours(6);
                toTime = rs.getTime("toTime").toLocalTime().minusHours(6);
                day = rs.getString("day");
                routine = new Routine(doctorID, fromTime,toTime,day);
                routines.add(routine);
            }
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML private void tableClickAction(){
        routineTable.getItems().clear();
        routinChecker();
        fromCol.setCellValueFactory(new PropertyValueFactory("from"));
        toCol.setCellValueFactory(new PropertyValueFactory("to"));
        dayCol.setCellValueFactory(new PropertyValueFactory("day"));
        routineTable.setItems(routines);
    }

    @FXML private void appointmentsBTNAction() throws IOException {
        App.setRoot("AllAppointmentsCentral");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileName.setItems(profileOptions);
        try {
            fetchDBData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getDoctorsData();
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory("phone"));
        depCol.setCellValueFactory(new PropertyValueFactory("department"));
        doctorTable.setItems(doctorlist);
        getStats();
        totalDoctorText.setText(String.valueOf(doctorNumber));
        totalPatientsText.setText(String.valueOf(patientNumber));
        upcomingAppointmentText.setText(String.valueOf(appointmentNumber));
    }

}
