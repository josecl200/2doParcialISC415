package database;

import models.Usuario;
import org.h2.tools.Server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Bootstrap{

    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9094", "-tcpAllowOthers").start();
    }

    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9094", "contracts", true, true);
    }

    public static void createTables() throws SQLException, NoSuchAlgorithmException {
        System.out.println("Creating tables...");
        String UsuarioDB = "CREATE TABLE IF NOT EXISTS USUARIO" +
                "(" +
                "ID BIGINT PRIMARY KEY NOT NULL," +
                "USERNAME VARCHAR(30) UNIQUE," +
                "NOMBRE VARCHAR(30)," +
                "PASSWORD TINYBLOB," +
                "ADMINISTRATOR BOOLEAN," +
                "UNIQUE KEY USERNAME_UNIQUE(USERNAME)" +
                ");";

        String urlCortaDB = "CREATE TABLE IF NOT EXISTS URL_CORTA" +
                "(" +
                "ID BIGINT PRIMARY KEY NOT NULL," +
                "CREADOR BIGINT," +
                "URL_ORIG TEXT NOT NULL," +
                "CREADOR BIGINT NOT NULL REFERENCES USUARIO(ID)," +
                "FECHA_CREACION TIMESTAMP NOT NULL" +
                ");";
        String estadisticaDB = "CREATE TABLE IF NOT EXISTS ESTADISTICAS" +
                "(" +
                "ID BIGINT PRIMARY KEY NOT NULL," +
                "URL_CORTA BIGINT NOT NULL REFERENCES URL_CORTA(ID)," +
                "SISTEMA_OP VARCHAR2 NOT NULL," +
                "NAVEGADOR VARCHAR2 NOT NULL," +
                "FECHA TIMESTAMP NOT NULL,"+
                "IPV4ADDR VARCHAR(16)"+
                ");";

        Connection con = DBCon.getInstance().getConnection();
        Statement statement = con.createStatement();
        statement.execute(UsuarioDB);
        statement.execute(urlCortaDB);
        statement.execute(estadisticaDB);

        statement.close();
        MessageDigest md   = MessageDigest.getInstance("SHA-224");
        byte[] hashPassEnt = md.digest("toor".getBytes(StandardCharsets.UTF_8));
        new CrudGenerico<Usuario>(Usuario.class).crear(new Usuario("root","admin",hashPassEnt,true));
        con.close();
    }
}