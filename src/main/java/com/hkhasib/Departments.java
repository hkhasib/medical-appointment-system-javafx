package com.hkhasib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Departments implements Initializable {
    @FXML ComboBox<String> profileName;
    ObservableList<String> profileOptions = FXCollections.observableArrayList("Logout");
    @FXML private void profileNameAction() throws IOException {
        if (profileName.getValue().equals("Logout")){
            App.setRoot("LoginPage");
        }
    }

    private String departmentName;
    private DepartmentModel department;

    private ArrayList<String> depnames = new ArrayList<>();

    @FXML private TextField nameField;
    @FXML private Button deleteButton;

    private Notification alert = new Notification();

    public Departments(){
        this.nameField=new TextField();
    }
    @FXML private TableView<DepartmentModel> departmentTable;
    @FXML private TableColumn<DepartmentModel, String> nameCol;

    private String dbhost, dbuser, dbname, dbport, dbpass;

    private ObservableList<DepartmentModel> departmentList = FXCollections.observableArrayList();
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

    private void getDepartmentNames(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from Departments");
            while (rs.next()){
                departmentList.add(new DepartmentModel(rs.getString("name")));
                depnames.add(rs.getString("name"));

            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML private void tableselectAction(){
        department = departmentTable.getSelectionModel().getSelectedItem();
        if (department!=null){
            departmentName=department.getName();
            deleteButton.setDisable(false);
        }
    }
    private void deleteDepartment(){

        String postDelete = "delete from Departments where name='"+departmentName+"'";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            statement.executeUpdate(postDelete);
            connection.close();
            alert.dataDeleted();
            departmentList.remove(department);
            depnames.remove(departmentName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void addButtonAction(){
        String tempname= nameField.getText().toUpperCase();
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",dbuser,dbpass);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from departments");
            /*while (rs.next()){
                depnames.add(rs.getString("name"));
            }*/
            if (nameField.getText().isEmpty()){
                alert.emptyData();
            }
            else if (depnames.contains(tempname)){
                alert.duplicateData();
            }
            else {
                statement.executeUpdate("INSERT into departments values ('"+nameField.getText().toUpperCase()+"')");
                alert.informationUpdated();
                departmentList.add(new DepartmentModel(nameField.getText().toUpperCase()));
                depnames.add(nameField.getText().toUpperCase());
                nameField.clear();
                System.out.println(depnames);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML private void deleteBTNAction(){
        Alert alt=new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete the following department?\n" +
                "Name: "+department.getName()+"", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> btnType = alt.showAndWait();
        if(btnType.get()==ButtonType.YES){
            deleteDepartment();
        }
        else{
            alert.operationCancelled();
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
        try {
            fetchDBData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getDepartmentNames();
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        departmentTable.setItems(departmentList);
    }
}
