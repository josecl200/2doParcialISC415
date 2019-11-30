import database.CrudGenerico;
import database.ServUrlCorta;
import database.ServUsuario;
import models.UrlCorta;
import models.Usuario;
import spark.Session;
import spark.Spark;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;

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
                    Spark.halt(403, "<h1>Su usuario no tiene los privilegios necesarios para esta operacion</h1>");
            }else{
                Spark.halt(401,"<h1>Usted no está autenticado en el servidor</h1>");
            }
        });

        Spark.before("/users", (request, response) -> {
            Usuario user = request.session().attribute("usuario");
            if(user!=null) {
                if (!user.isAdmin())
                    Spark.halt(403, "<h1>Su usuario no tiene los privilegios necesarios para esta operacion</h1>");
            }else{
                Spark.halt(401,"<h1>Usted no está autenticado en el servidor</h1>");
            }
        });

        Spark.before("/myUrls", (request, response) -> {
            Usuario user = request.session().attribute("usuario");
            if(user==null)
                if(request.session().attribute("tempUrls")==null)
                    Spark.halt(401,"<h1>Usted no está autenticado en el servidor</h1>");
        });

        Spark.before("/stats/:idUrl", (request, response) -> {
            System.out.println(request.params("idUrl"));
            ByteBuffer byteId = ByteBuffer.wrap(Base64.getDecoder().decode(request.params("idUrl")));
            long idUrl = byteId.getLong();
            Usuario user = request.session().attribute("usuario");
            if(user!=null) {
                UrlCorta url = new CrudGenerico<>(UrlCorta.class).encontrar(idUrl);
                if(url.getCreador()!= null)
                    if(url.getCreador().getId()!=user.getId())
                        if (!user.isAdmin())
                            Spark.halt(403, "<h1>Su usuario no tiene los privilegios necesarios para esta operacion</h1>");
            }else{
                List<UrlCorta> tempUrls = request.session().attribute("tempUrls");
                if(tempUrls!=null){
                    UrlCorta url = new CrudGenerico<>(UrlCorta.class).encontrar(idUrl);
                    boolean found=false;
                    for (UrlCorta u: tempUrls) {
                        if (u.getId()==url.getId()){
                            found=true;
                            break;
                        }
                    }
                    if(!found)
                        Spark.halt(401,"<h1>Usted no está autenticado en el servidor</h1>");
                }else{
                    Spark.halt(401,"<h1>Usted no está autenticado en el servidor</h1>");
                }
            }
        });
    }
}
