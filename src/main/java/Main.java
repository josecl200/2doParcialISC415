import database.Bootstrap;
import database.DBCon;
import spark.Spark;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Spark.port(8081);
        Bootstrap.startDb();

        DBCon.getInstance().testConnection();
        Spark.staticFileLocation("/public");
        Filters.applyFilters();
        Routes.rutas();
    }

}
