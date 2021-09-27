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

public class AllAppointmentsPatient implements Initializable {
    String dashboardUser=LoginPage.loginName;

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
    @FXML private TableColumn<AppointmentListModel, String> docNameCol;
    @FXML private TableColumn<AppointmentListModel, String> phoneCol;
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
            ResultSet rd = statement.executeQuery("SELECT * from Patients where username='"+dashboardUser+"'");
            while (rd.next()){
                patientID=rd.getInt("id");
                patientName=rd.getString("firstname")+" "+rd.getString("lastname");
            }
            ResultSet rs=statement.executeQuery("SELECT * from appointments, doctors where appointments.patientID="+patientID+" and doctors.id=appointments.doctorID");
            while (rs.next()){
                doctorID=rs.getInt("doctorID");
                patientID=rs.getInt("patientID");
                doctorName=rs.getString("firstname")+" "+rs.getString("lastname");
                phone=rs.getString("phone");
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
        profileName.setPromptText(dashboardUser);


        profileName.setItems(profileOptions);
        try {
            fetchDBData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getAppointmentData();
        docNameCol.setCellValueFactory(new PropertyValueFactory("doctorName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory("phone"));
        depCol.setCellValueFactory(new PropertyValueFactory("department"));
        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory("time"));
        emergCol.setCellValueFactory(new PropertyValueFactory("emergency"));
        appointmentTable.setItems(appointmentList);

    }
}
