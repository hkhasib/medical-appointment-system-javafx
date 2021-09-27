package com.hkhasib;
import javafx.scene.control.Alert;
public class Notification {

    public void emptyData(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert!!!");
        alert.setHeaderText("Blank Data Warning");
        String s ="Please don't leave any required fields empty!";
        alert.setContentText(s);
        alert.show();

    }
    public void successDB(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert!!!");
        alert.setHeaderText("Database Connected Successfully");
        String s ="You have successfully Connected the Database";
        alert.setContentText(s);
        alert.show();

    }
    public void failureDB(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert!!!");
        alert.setHeaderText("Error Establishing Database Connection");
        String s ="Please use correct credentials and trt again!";
        alert.setContentText(s);
        alert.show();

    }
    public void wrongCredentials(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert!!!");
        alert.setHeaderText("Wrong Credentials");
        String s ="Please use correct credentials and trt again!";
        alert.setContentText(s);
        alert.show();

    }
    public void accountCreated(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert!!!");
        alert.setHeaderText("Successfully created your account!");
        String s ="You have successfully created your account!";
        alert.setContentText(s);
        alert.show();
    }
    public void routineAdded(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert!!!");
        alert.setHeaderText("Successfully Added the Schedule!");
        String s ="You have successfully added the Schedule in Doctor's Routine!";
        alert.setContentText(s);
        alert.show();
    }
    public void duplicateRoutine(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!!!");
        alert.setHeaderText("Duplicate Schedule Detected!");
        String s ="This schedule already exists in the routine";
        alert.setContentText(s);
        alert.show();
    }
    public void duplicatePhone(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!!!");
        alert.setHeaderText("Duplicate Phone Number Detected!");
        String s ="This phone number already exists in the database! Please use the correct/different one.";
        alert.setContentText(s);
        alert.show();
    }
    public void duplicateUsername(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!!!");
        alert.setHeaderText("Duplicate Username Detected!");
        String s ="This username already exists in the database! Please use the correct/different one.";
        alert.setContentText(s);
        alert.show();
    }
    public void duplicateData(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!!!");
        alert.setHeaderText("Duplicate data Detected!");
        String s ="One or more of your Input(s) already exists in the database";
        alert.setContentText(s);
        alert.show();
    }
    public void wrongCaptcha(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!!!");
        alert.setHeaderText("Incorrect Answer");
        String s ="Prove you are not Robot!";
        alert.setContentText(s);
        alert.show();
    }
    public void appointmentAdded(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert!!!");
        alert.setHeaderText("Successfully Booked Appointment!");
        String s ="You have successfully booked the appointment!!!";
        alert.setContentText(s);
        alert.show();
    }
    public void timeConflict(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert!!!");
        alert.setHeaderText("'From time' can't be after 'To Time'!!");
        String s ="Please select correct time interval!";
        alert.setContentText(s);
        alert.show();
    }
    public void informationUpdated(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!!!");
        alert.setHeaderText("Information Successfully Updated");
        String s ="You have successfully changed/updated the information/credentials!";
        alert.setContentText(s);
        alert.show();
    }
    public void dataDeleted(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!!!");
        alert.setHeaderText("Successfully Deleted");
        String s ="You have successfully deleted the information!";
        alert.setContentText(s);
        alert.show();
    }
    public void operationCancelled(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cancelled!");
        alert.setHeaderText("Successfully Cancelled!");
        String s ="You cancelled the operation!";
        alert.setContentText(s);
        alert.show();
    }
    public void maximumAttemptWarning(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!!");
        alert.setHeaderText("One more Login Attempt Remaining!");
        String s ="You have tried too many times. One more login attempt left! Additional failure will close the system!";
        alert.setContentText(s);
        alert.show();
    }
    public void appointmentCancelled(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Done!!");
        alert.setHeaderText("An Appointment has been Cancelled!");
        String s ="You have successfully cancelled an Appointment!";
        alert.setContentText(s);
        alert.show();
    }
    public void olddateAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Don't Choose Old Date");
        String s ="Please choose correct date and try again";
        alert.setContentText(s);
        alert.show();
    }


}
