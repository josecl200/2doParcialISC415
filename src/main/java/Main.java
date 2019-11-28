import database.Bootstrap;
import database.CrudGenerico;
import database.DBCon;
import database.ServUsuario;
import models.Usuario;
import spark.Spark;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        Spark.port(8081);
        Bootstrap.startDb();
        DBCon.getInstance().testConnection();
        Spark.staticFileLocation("/public");
        ServUsuario.getInstance().createAdmin();
        Filters.applyFilters();
        Routes.rutas();
    }

}
