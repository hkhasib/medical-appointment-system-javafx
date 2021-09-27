package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DoctorDashboard implements Initializable {
    private String dbhost, dbuser, dbname, dbport, dbpass, date, time, note, firstName, lastName, patientName, designation, phone;
    private LocalTime timeAppointment;
    private LocalDate dateAppointment;
    private int patientID2;
    private String patientName2,a,b;

    private ArrayList<LocalDateTime> localtimes = new ArrayList<>();
    private ArrayList<Integer> patientIDList = new ArrayList<>();

    private int janData=0, febData=0, marData=0, aprData=0, mayData=0, junData=0, julData=0, augData=0, sepData=0, octData=0, novData=0, decData=0;

    private int userID, patientID;

    private String nextAppTime;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDateTime appointmentTimeDate;
    BasicAppointmentModel basicAppointment;

    @FXML
    ComboBox<String> profileName;
    @FXML
    Pane nextAppointmentCard;
    @FXML private Text nextAppointmentTime, patientNameText, phoneText;
    @FXML private TextArea patientNoteField;
    @FXML private BarChart<?,?> appointmentChart;
    @FXML private CategoryAxis x;
    @FXML private NumberAxis y;
    @FXML private TableView<BasicAppointmentModel> appointmentTable;
    @FXML private TableColumn<BasicAppointmentModel, LocalDate> dateCol;
    @FXML private TableColumn<BasicAppointmentModel, LocalTime> timeCol;
    @FXML private TableColumn<BasicAppointmentModel, String> docNameCol;

    ObservableList<BasicAppointmentModel> appointmentList= FXCollections.observableArrayList();



    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");

    String dashboardUser=LoginPage.loginName;

    public DoctorDashboard(){
        this.nextAppointmentTime = new Text();
        this.patientNameText = new Text();
        this.phoneText = new Text();
        this.patientNoteField = new TextArea();
    }

    private void appointmentStats() throws IOException {

        fetchDBData();
        getUserID();
        //appointmentList = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * from appointments where doctorID='"+userID+"' order by date ASC, time ASC");

            while(rs.next()){
                LocalDate appointDate = rs.getDate("date").toLocalDate();
                if (appointDate.getMonth().toString().equalsIgnoreCase("january")){
                    janData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("february")){
                    febData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("march")){
                    marData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("april")){
                    aprData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("may")){
                    mayData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("june")){
                    junData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("july")){
                    julData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("august")){
                    augData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("september")){
                    sepData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("october")){
                    octData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("november")){
                    novData++;
                }
                if (appointDate.getMonth().toString().equalsIgnoreCase("december")){
                    decData++;
                }
            }
            ResultSet rd=statement.executeQuery("SELECT  * from appointments, patients where appointments.doctorID='"+userID+"' and appointments.patientID=patients.id");
            while (rd.next()){
                if (rd.getTime("time").toLocalTime().atDate(rd.getDate("date").toLocalDate()).isAfter(LocalDateTime.now())){
                    dateAppointment = rd.getDate("date").toLocalDate();
                    timeAppointment = rd.getTime("time").toLocalTime().minusHours(6);
                    patientName2=rd.getString("firstname")+" "+rd.getString("lastName");

                    basicAppointment =new BasicAppointmentModel(dateAppointment,timeAppointment, patientName2);

                    appointmentList.add(basicAppointment);
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

    private void getUserID() throws IOException {
        fetchDBData();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * from doctors where username='"+dashboardUser+"'");
            while(rs.next()){
                userID=rs.getInt("id");
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void nextAppointment() throws IOException {
        fetchDBData();
        getUserID();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * from appointments where doctorID='"+userID+"' order by date ASC, time ASC");
            while(rs.next()){
                int id = rs.getInt("patientID");
                date = rs.getString("date");
                time = rs.getString("time");
                note=rs.getString("note");
                appointmentDate =LocalDate.parse(date);
                appointmentTime= LocalTime.parse(time);
                appointmentTimeDate=appointmentTime.atDate(appointmentDate);
                if (appointmentTimeDate.isAfter(LocalDateTime.now())){
                    patientIDList.add(id);
                    localtimes.add(appointmentTimeDate);
                }
            }
            if (!localtimes.isEmpty()){
                nextAppTime = localtimes.get(0).getDayOfWeek()+"  "+ DateTimeFormatter.ofPattern("hh:mm a").format(localtimes.get(0).toLocalTime())+"  "+localtimes.get(0).getDayOfMonth()+" "+localtimes.get(0).getMonth()+" "+localtimes.get(0).getYear();
                patientID=patientIDList.get(0);
            }


            ResultSet rs1= statement.executeQuery("SELECT * from patients where id='"+patientID+"'");

            while (rs1.next()){
                firstName=rs1.getString("firstname");
                lastName=rs1.getString("lastname");
                //designation=rs1.getString("designation");
                phone=rs1.getString("phone");
            }
            patientName=firstName+" "+lastName;
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointmentStats();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory("time"));
        docNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        appointmentTable.setItems(appointmentList);

        XYChart.Series set1 = new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("January",janData));
        set1.getData().add(new XYChart.Data("February",febData));
        set1.getData().add(new XYChart.Data("March",marData));
        set1.getData().add(new XYChart.Data("April",aprData));
        set1.getData().add(new XYChart.Data("May",mayData));
        set1.getData().add(new XYChart.Data("June",junData));
        set1.getData().add(new XYChart.Data("July",julData));
        set1.getData().add(new XYChart.Data("August",augData));
        set1.getData().add(new XYChart.Data("September",sepData));
        set1.getData().add(new XYChart.Data("October",octData));
        set1.getData().add(new XYChart.Data("November",novData));
        set1.getData().add(new XYChart.Data("December",decData));
        appointmentChart.getData().addAll(set1);

        profileName.setPromptText(dashboardUser);

        try {
            nextAppointment();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileName.setItems(profileOptions);

        System.out.println(patientName);

        if (!patientName.equals("null null")){
            nextAppointmentCard.setVisible(true);
            patientNameText.setText(patientName);
            nextAppointmentTime.setText(nextAppTime);
            patientNoteField.setText(note);
            phoneText.setText("Call: "+phone);
        }
        else {
            nextAppointmentCard.setVisible(false);
        }

    }
    }

