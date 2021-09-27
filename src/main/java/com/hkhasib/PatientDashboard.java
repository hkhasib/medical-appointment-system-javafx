package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.LocalDateTimeStringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

public class PatientDashboard implements Initializable {

    private String dbhost, dbuser, dbname, dbport, dbpass, date, time, note, emergency, firstName, lastName, doctorName, designation, phone;
    private LocalTime timeAppointment;
    private LocalDate dateAppointment;
    private int doctorID2;
    private String doctorName2,a,b;

    private ArrayList<LocalDateTime> localtimes = new ArrayList<>();
    private ArrayList<Integer> doctorIDList = new ArrayList<>();

    private int janData=0, febData=0, marData=0, aprData=0, mayData=0, junData=0, julData=0, augData=0, sepData=0, octData=0, novData=0, decData=0;

    private int userID, doctorID;

    private String nextAppTime;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDateTime appointmentTimeDate;
    BasicAppointmentModel basicAppointment;

    @FXML ComboBox<String> profileName;
    @FXML Pane nextAppointmentCard;
    @FXML private Text nextAppointmentTime, docNameText, designationText, phoneText;
    @FXML private BarChart<?,?> appointmentChart;
    @FXML private CategoryAxis x;
    @FXML private NumberAxis y;
    @FXML private TableView<BasicAppointmentModel> appointmentTable;
    @FXML private TableColumn<BasicAppointmentModel, LocalDate> dateCol;
    @FXML private TableColumn<BasicAppointmentModel, LocalTime> timeCol;
    @FXML private TableColumn<BasicAppointmentModel, String> docNameCol;

    ObservableList<BasicAppointmentModel> appointmentList=FXCollections.observableArrayList();



    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");

    String dashboardUser=LoginPage.loginName;

    public PatientDashboard(){
        this.nextAppointmentTime = new Text();
        this.docNameText = new Text();
        this.designationText = new Text();
        this.phoneText = new Text();
    }

    private void appointmentStats() throws IOException {

        fetchDBData();
        getUserID();
        //appointmentList = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select * from appointments where patientID='"+userID+"' order by date ASC, time ASC");

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
            ResultSet rd=statement.executeQuery("SELECT  * from appointments, doctors where appointments.patientID='"+userID+"' and appointments.doctorID=doctors.id");
            while (rd.next()){
                if (rd.getTime("time").toLocalTime().atDate(rd.getDate("date").toLocalDate()).isBefore(LocalDateTime.now())){
                    dateAppointment = rd.getDate("date").toLocalDate();
                    timeAppointment = rd.getTime("time").toLocalTime().minusHours(6);
                    doctorName2=rd.getString("firstname")+" "+rd.getString("lastName");

                    basicAppointment =new BasicAppointmentModel(dateAppointment,timeAppointment, doctorName2);

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
            ResultSet rs = statement.executeQuery("Select * from patients where username='"+dashboardUser+"'");
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
            ResultSet rs = statement.executeQuery("Select * from appointments where patientID='"+userID+"' order by date ASC, time ASC");
            while(rs.next()){
                int id = rs.getInt("doctorID");
                date = rs.getString("date");
                time = rs.getString("time");
                appointmentDate =LocalDate.parse(date);
                appointmentTime= LocalTime.parse(time);
                appointmentTimeDate=appointmentTime.atDate(appointmentDate);
                if (appointmentTimeDate.isAfter(LocalDateTime.now())){
                    doctorIDList.add(id);
                    localtimes.add(appointmentTimeDate);
                }
            }
            if (!localtimes.isEmpty()){
                nextAppTime = localtimes.get(0).getDayOfWeek()+"  "+DateTimeFormatter.ofPattern("hh:mm a").format(localtimes.get(0).toLocalTime())+"  "+localtimes.get(0).getDayOfMonth()+" "+localtimes.get(0).getMonth()+" "+localtimes.get(0).getYear();
                doctorID=doctorIDList.get(0);
            }


            ResultSet rs1= statement.executeQuery("SELECT * from doctors where id='"+doctorID+"'");

            while (rs1.next()){
                firstName=rs1.getString("firstname");
                lastName=rs1.getString("lastname");
                designation=rs1.getString("designation");
                phone=rs1.getString("phone");
            }
            doctorName=firstName+" "+lastName;
            connection.close();

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
        if (profileName.getValue().equals("Logout")){
            App.setRoot("PatientDashboard");
        }
    }

    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }
    /*@FXML private void callButtonAction() throws Exception {
        open("tel:+"+emergencyPhoneNumber+"");
    }
    public void open(String url) throws Exception{
        URI u = new URI(url);
        java.awt.Desktop.getDesktop().browse(u);
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //String dashboardUser=LoginPage.loginName;

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

        if (!doctorName.equals("null null")){
            nextAppointmentCard.setVisible(true);
            docNameText.setText(doctorName);
            nextAppointmentTime.setText(nextAppTime);
            designationText.setText(designation);
            phoneText.setText("Call: "+phone);
        }
        else {
            nextAppointmentCard.setVisible(false);
        }

    }
}
