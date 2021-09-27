module com.hkhasib {
    requires java.sql;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires java.desktop;
    //requires javafx.controls;
    //requires javafx.fxml;
    //requires java.sql;

    opens com.hkhasib to javafx.fxml;
    //opens com.sun.javafx.control.behavior to javafx.controls;
    //exports com.sun.javafx.control.behavior to com.jfeonix;
    //exports javafx.controls to com.sun.javafx.scene.control.behavior;
    exports com.hkhasib;
}