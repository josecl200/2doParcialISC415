package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCon {
    private static DBCon instance;
    private String URL = "jdbc:h2:tcp://localhost/~/JDBC-Parc";

    private DBCon() {
        registerDriver();
    }

    public static DBCon getInstance() {
        if (instance == null) {
            instance = new DBCon();
        }
        return instance;
    }

    private void registerDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {

        }
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return con;
    }

    public void testConnection() {
        try {
            getConnection().close();
            System.out.println("Conexión realizada con éxito!!");
        } catch (SQLException ex) {

        }
    }
}