import database.Bootstrap;
import database.DBCon;
import spark.Spark;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {
public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
            Spark.port(8081);
            Spark.staticFileLocation("/public");
            Bootstrap.startDb();
            //Bootstrap.createTables();
            DBCon.getInstance().testConnection();
            //Filters.applyFilters();
            Routes.rutas();
        }

}
