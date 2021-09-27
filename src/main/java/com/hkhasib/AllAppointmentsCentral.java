package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AllAppointmentsCentral implements Initializable {
    @FXML ComboBox<String> profileName;
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }

    private Notification alert = new Notification();

    private String dbhost, dbuser, dbname, dbport, dbpass, doctorName, patientName,phone,department,emergency;
    private LocalDate date, tempDate;
    private LocalTime time, tempTime;
    private int patientID, doctorID, tempPatientID, tempDoctorID;


    @FXML private Button cancelButton;

    @FXML private TableView<AppointmentListModel> appointmentTable;
    @FXML private TableColumn<AppointmentListModel, Integer> docIDCol;
    @FXML private TableColumn<AppointmentListModel, Integer> patIDCol;
    @FXML private TableColumn<AppointmentListModel, String> docNameCol;
    @FXML private TableColumn<AppointmentListModel, String> patNameCol;
    @FXML private TableColumn<AppointmentListModel, String> depCol;
    @FXML private TableColumn<AppointmentListModel, String> emergCol;
    @FXML private TableColumn<AppointmentListModel, LocalTime> timeCol;
    @FXML private TableColumn<AppointmentListModel, LocalDate> dateCol;

    private AppointmentListModel appointment;
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

    private void getAppointmentData(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs=statement.executeQuery("SELECT * from appointments, doctors, patients where appointments.doctorID=doctors.id and appointments.patientID=patients.id");
            while (rs.next()){
                doctorID=rs.getInt("doctorID");
                patientID=rs.getInt("patientID");
                doctorName=rs.getString("doctors.firstname")+" "+rs.getString("doctors.lastname");
                patientName=rs.getString("patients.firstname")+" "+rs.getString("patients.lastname");
                phone=rs.getString("doctors.phone");
                department=rs.getString("department");
                emergency=rs.getString("emergency");
                time=rs.getTime("time").toLocalTime().minusHours(6);
                date=rs.getDate("date").toLocalDate();
                appointment = new AppointmentListModel(doctorName, patientName, phone, department, emergency,time, date, doctorID,patientID);
                appointmentList.add(appointment);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML private void tableViewSelect(){
        appointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (appointment!=null){
            tempDoctorID=appointment.getDoctorID();
            tempPatientID=appointment.getPatientID();
            tempDate=appointment.getDate();
            tempTime=appointment.getTime();
            cancelButton.setDisable(false);
        }
    }
    private void cancelAppointment(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE from appointments where patientID="+tempPatientID+" and doctorID="+tempDoctorID+" and date='"+tempDate+"' and time='"+tempTime+"'");
            connection.close();
            appointmentList.remove(appointment);
            alert.appointmentCancelled();
            cancelButton.setDisable(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML private void cancelBTNAction(){
        cancelAppointment();
    }

    private ObservableList<AppointmentListModel> appointmentList = FXCollections.observableArrayList();

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
        getAppointmentData();
        docIDCol.setCellValueFactory(new PropertyValueFactory("doctorID"));
        patIDCol.setCellValueFactory(new PropertyValueFactory("patientID"));
        docNameCol.setCellValueFactory(new PropertyValueFactory("doctorName"));
        patNameCol.setCellValueFactory(new PropertyValueFactory("patientName"));
        depCol.setCellValueFactory(new PropertyValueFactory("department"));
        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory("time"));
        emergCol.setCellValueFactory(new PropertyValueFactory("emergency"));
        appointmentTable.setItems(appointmentList);

    }
}
