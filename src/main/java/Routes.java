import database.CrudGenerico;
import models.Url_Corta;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class Routes {
    public static rutas(){
        Spark.get("/",(request, response) -> new FreeMarkerEngine().render(new ModelAndView(null,"index.fml")));

        Spark.put("/acortar",(request, response) -> {
            Map<String,Object> atributos = new HashMap<>();
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"create.fml"));
        });

        //Spark.get("/urlCortada",(request, response) -> {});

        Spark.get("/:idUrl",(request, response) -> {
            CrudGenerico<Url_Corta>(Url_Corta.class).
        });
        Spark.get("/stats/:idUrl",(request, response) -> {

        });
        Spark.get("/login",(request, response) -> {

        });
        Spark.get("/register",(request, response) -> {

        });
        Spark.put("/login",(request, response) -> {

        });
        Spark.put("/register",(request, response) -> {

        });
        Spark.get("/users",(request, response) -> {

        });
        Spark.post("/adminRights",(request, response) -> {

        });
        Spark.get("/myUrls",(request, response) -> {

        });
        Spark.get("/allUrls",(request, response) -> {

        });
        Spark.delete("/delUrl",(request, response) -> {

        });
    }
}
