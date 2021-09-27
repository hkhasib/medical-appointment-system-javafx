package com.hkhasib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//To get the database credentials from dbConfig.properties
public class DatabaseHandler {
    private String dbhost, dbname, dbuser, dbport, dbpass;

    private void fetchDBData() throws IOException {
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
            dbport=p.getProperty("port");
        }
        is.close();
    }

    public DatabaseHandler() throws IOException {
        fetchDBData();
    }



    public String getDbhost() {
        return dbhost;
    }

    public void setDbhost(String dbhost) {
        this.dbhost = dbhost;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDbuser() {
        return dbuser;
    }

    public void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public String getDbport() {
        return dbport;
    }

    public void setDbport(String dbport) {
        this.dbport = dbport;
    }

    public String getDbpass() {
        return dbpass;
    }

    public void setDbpass(String dbpass) {
        this.dbpass = dbpass;
    }
}
