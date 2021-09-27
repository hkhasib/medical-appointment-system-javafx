package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookAppointmentPatient implements Initializable {
    String dashboardUser=LoginPage.loginName;
    @FXML ComboBox<String> profileName;
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }

    private String dbhost, dbuser, dbname, dbport, dbpass, dayName, doctorUsername, day, firstName, lastName, phone, specialities;
    private LocalTime from,to;
    private String note ="N/A", emergency="No";
    private String readRoutine="Select * from routines";
    private int doctorID, userID, docID, patID;
    private LocalDate tempDate;
    private LocalTime tempTime;
    Notification alert = new Notification();
    ObservableList<Routine> routineList;

    @FXML private DatePicker datePicker;
    @FXML private Button bookButton;
    @FXML private CheckBox emergencyChecker;
    @FXML private ComboBox<String> doctorSelector;
    @FXML private TextField nameField, specialityField, phoneField, statusText;
    @FXML private TextArea noteField;
    @FXML private TableView<Routine> routineTable;
    @FXML private TableColumn<Routine, String> dayCol;
    @FXML private TableColumn<Routine, String> fromCol;
    @FXML private TableColumn<Routine, String> toCol;
    @FXML private TableColumn<Routine, String> idCol;

    private LocalDate date;


    public BookAppointmentPatient() {
        this.datePicker = new DatePicker();
        this.nameField = new TextField();
        this.phoneField = new TextField();
        this.specialityField = new TextField();
        this.noteField = new TextArea();
        this.statusText = new TextField();
        this.emergencyChecker = new CheckBox();
    }

    ObservableList<String> doctorNames = FXCollections.observableArrayList();

    //private ArrayList<Appointments>

    private Appointments appointments;
    private Routine routine;

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
    private void fetchDoctorID(){
        doctorUsername = doctorSelector.getValue();
    }

    @FXML private void getDoctorDetails(){
        doctorUsername=doctorSelector.getValue();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * from doctors where username='"+doctorUsername+"'");
            while(rs.next()){
                firstName=rs.getString(2);
                lastName=rs.getString(3);
                phone=rs.getString(6);
                specialities=rs.getString(8);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        nameField.setText(firstName+" "+lastName);
        phoneField.setText(phone);
        specialityField.setText(specialities);
        routineTable.getItems().clear();
        datePicker.getEditor().clear();
        date=null;
        bookButton.setDisable(true);
    }

    @FXML private void timeSlotMaker(){

    }

    @FXML private void checkRoutine() throws IOException {
        fetchDBData();
        routineList = FXCollections.observableArrayList();
        dayName = datePicker.getValue().getDayOfWeek().toString();
        doctorUsername = doctorSelector.getValue();
        date = datePicker.getValue();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();

            ResultSet rd = statement.executeQuery("Select * from doctors where username='"+doctorUsername+"'");
            while(rd.next()){
                doctorID=rd.getInt("id");
            }
            ResultSet rs = statement.executeQuery("Select * from routines where doctorID='"+doctorID+"' and day='"+dayName+"'");
            while(rs.next()){
                routine = new Routine(rs.getInt(1),rs.getTime(2).toLocalTime().minusHours(6),rs.getTime(3).toLocalTime().minusHours(6),rs.getString(4));
                routineList.add(routine);
            }
            dayCol.setCellValueFactory(new PropertyValueFactory("day"));
            idCol.setCellValueFactory(new PropertyValueFactory("doctorID"));
            fromCol.setCellValueFactory(new PropertyValueFactory("from"));
            toCol.setCellValueFactory(new PropertyValueFactory("to"));
            routineTable.setItems(routineList);
            connection.close();
            bookButton.setDisable(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void getUserID() throws IOException {
        fetchDBData();
        String readdata="Select * from Patients where username='"+dashboardUser+"'";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(readdata);
            while(rs.next()){
                userID= rs.getInt("id");
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML private void tableViewClickAction(){
        routine = routineTable.getSelectionModel().getSelectedItem();
        if(routine!=null){

            from=routine.getFrom();
            to=routine.getTo();
            day=routine.getDay();

            int unavailable=0;


            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("Select * from appointments where doctorID='"+doctorID+"'");
                while(rs.next()){
                    docID=rs.getInt("doctorID");
                    patID=rs.getInt("patientID");
                    tempTime=rs.getTime("time").toLocalTime().minusHours(6);
                    tempDate=rs.getDate("date").toLocalDate();
                    String emerg=rs.getString("emergency");
                    String tempNote=rs.getString("note");
                    //System.out.println(tempTime);
                    if (docID==doctorID&&tempTime.equals(from)
                            &&day.equalsIgnoreCase(tempDate.getDayOfWeek().toString())&&tempDate.equals(date)){
                        unavailable=1;
                        break;
                    }
                    unavailable=0;
                    continue;

                }
                System.out.println(unavailable);
                if (unavailable==1){
                    statusText.setText("Unavailable!");
                    bookButton.setDisable(true);
                }
                else {
                    statusText.setText("Available");
                    bookButton.setDisable(false);
                }
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else {
            bookButton.setDisable(true);
        }
    }

    @FXML private void postAppointment() throws IOException {
        fetchDBData();
        getUserID();
        if (emergencyChecker.isSelected()){
            emergency="Yes";
        }
        else {
            emergency="No";
        }
        if (!noteField.getText().isEmpty()){
            note=noteField.getText();
        }
        else{
            note="N/A";
        }
        String postdata="insert into Appointments values(" + userID + "," + doctorID + ",'" + date + "','" + from + "','" + note + "','" + emergency + "')";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();

            LocalDateTime localDateTime = from.atDate(date);

            if (localDateTime.isBefore(LocalDateTime.now())){
                alert.olddateAlert();
            }
            else {
                statement.executeUpdate(postdata);
                alert.appointmentAdded();
            }
            connection.close();
            bookButton.setDisable(true);

        } catch (SQLException throwables) {

            throwables.printStackTrace();
        }

    }

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
        String readdata="Select * from Doctors";

        try {
            fetchDBData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(readdata);
            while(rs.next()){
                doctorNames.add(rs.getString("username"));
                firstName=rs.getString(2);
                lastName=rs.getString(3);
                phone=rs.getString(6);
                specialities=rs.getString(8);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        doctorSelector.setItems(doctorNames);
        profileName.setPromptText(dashboardUser);

    }
}
