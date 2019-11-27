import database.ServUsuario;
import models.Usuario;
import spark.Session;
import spark.Spark;

public class Filters {
    public static void applyFilters(){
        Spark.before((request, response) -> {
            Usuario user = request.session().attribute("usuario");
            if(request.cookie("USER") != null && user == null){ //If the user is not logged, try to get the cookie to set a session
                long userUID = Long.parseLong(request.cookie("USER"));
                user = ServUsuario.getInstance().getUser(userUID);
                Session session = request.session(true);
                session.attribute("usuario", user);
            }
        });
        Spark.before("/adminRights/*", (request, response) -> {
            Usuario user = request.session().attribute("usuario");
            if(user!=null) {
                if (!user.isAdmin())
                    Spark.halt(403, "<h1>Su usuario no tiene los privilegios necesariospara esta operacion</h1>");
            }else{
                Spark.halt(401,"<h1>Usted no está autenticado en el servidor</h1>");
            }
        });
        Spark.before("/myUrls/*", (request, response) -> {
            Usuario user = request.session().attribute("usuario");
            if(user==null)
                Spark.halt(401,"<h1>Usted no está autenticado en el servidor</h1>");
        });
        Spark.before("/url/:idUrl", (request, response) -> {
            System.out.println(request.params("idUrl"));
            Usuario user = request.session().attribute("usuario");
            if(user!=null) {
                if (!user.isAdmin())
                    Spark.halt(403, "<h1>Su usuario no tiene los privilegios necesariospara esta operacion</h1>");
            }else{
                Spark.halt(401,"<h1>Usted no está autenticado en el servidor</h1>");
            }
        });
    }
}
