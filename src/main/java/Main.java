import apis.ApiRest;
import apis.SOAP.ApiJabon;
import database.Bootstrap;
import database.CrudGenerico;
import database.DBCon;
import database.ServUsuario;
import models.Usuario;
import spark.Spark;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


//Hasta este commit llega 2do parcial
public class Main {
    public static void main(String[] args) throws Exception {
        Spark.port(42069);
        Bootstrap.startDb();
        DBCon.getInstance().testConnection();
        Spark.staticFileLocation("/public");
        ServUsuario.getInstance().createAdmin();
        Filters.applyFilters();
        Routes.rutas();
        ApiRest.restful();
        ApiJabon.init();
    }

}
