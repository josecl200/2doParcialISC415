import database.CrudGenerico;
import database.ServEstadistica;
import database.ServUrlCorta;
import database.ServUsuario;
import models.Estadisticas;
import models.UrlCorta;
import models.Usuario;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Routes {
    public static void rutas(){
        Spark.get("/",(request, response) -> new FreeMarkerEngine().render(new ModelAndView(null,"index.fml")));

        Spark.post("/acortar",(request, response) -> {
            UrlCorta url = new UrlCorta();
            url.setCreador(request.session().attribute("user"));
            url.setFecha(new Date());
            url.setUrl_orig(request.queryParams("url"));
            new CrudGenerico<>(UrlCorta.class).crear(url);
            return null;
        });

        //Spark.get("/urlCortada",(request, response) -> {});

        Spark.get("/r/:idUrl",(request, response) -> {
            ByteBuffer byteId = ByteBuffer.wrap(Base64.getDecoder().decode(request.params("idUrl")));
            long idUrl = byteId.getLong();
            String userAgent = request.userAgent();
            String user = userAgent.toLowerCase();
            String os="unknown";
            String browser = "unknown";
            if (userAgent.toLowerCase().contains("windows")){
                os = "Windows";
            } else if(userAgent.toLowerCase().contains("mac")){
                os = "Mac";
            } else if(userAgent.toLowerCase().contains("x11")){
                os = "Unix";
            } else if(userAgent.toLowerCase().contains("android")){
                os = "Android";
            } else if(userAgent.toLowerCase().contains("iphone")){
                os = "IPhone";
            }
            if (user.contains("msie")){
                String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
                browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
            } else if (user.contains("safari") && user.contains("version")){
                browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if ( user.contains("opr") || user.contains("opera")){
                if(user.contains("opera"))
                    browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
                else if(user.contains("opr"))
                    browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            } else if (user.contains("chrome")){
                browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))  || (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) || (user.contains("mozilla/4.08")) || (user.contains("mozilla/3")) ){
                //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
                browser = "Netscape-?";
            } else if (user.contains("firefox")){
                browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if(user.contains("rv")){
                browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
            }

            UrlCorta url = ServUrlCorta.getInstance().encontrar(idUrl);
            Estadisticas est = new Estadisticas();
            est.setFecha(new Timestamp(System.currentTimeMillis()));
            est.setIp(request.ip());
            est.setSistema_op(os);
            est.setNavegador(browser);
            est.setUrl_corta(url);
            ServEstadistica.getInstance().crear(est);
            String redir = url.getUrl_orig();
            if(redir.contains("http://") || redir.contains("https://"))
                response.redirect(redir);
            else
                response.redirect("http://"+redir);

            return null;
        });
        //Spark.get("/stats/:idUrl",(request, response) -> {

        //});
        Spark.get("/login",(request, response) -> new FreeMarkerEngine().render(new ModelAndView(null, "login.fml")));
        Spark.get("/register",(request, response) -> new FreeMarkerEngine().render(new ModelAndView(null, "register.fml")));
        Spark.post("/login",(request, response) -> {
            String username = request.queryParams("usuario");
            String password = request.queryParams("contrasena");
            if(!ServUsuario.getInstance().validateCredentials(username,password)){
                Map<String,Object> atributos = new HashMap<>();
                atributos.put("mensaje", "Credenciales incorrectas, intentelo de nuevo");
                return new FreeMarkerEngine().render(new ModelAndView(atributos,"login.fml"));
            }else{
                Session session = request.session(true);
                Usuario user    = ServUsuario.getInstance().getUser(username);
                session.attribute("usuario", user);
                Boolean rememberMe = false;
                if(request.queryParams("rememberMe") != null) {
                    rememberMe = true;
                }
                if(rememberMe){
                    response.cookie("USER", Long.toString(user.getId()), 604800);
                }
                response.redirect("/");
            }
            return null;
        });
        Spark.put("/register",(request, response) -> {
            String username = request.queryParams("usuario");
            String password = request.queryParams("contrasena");
            String nombre   = request.queryParams("nombre");
            boolean admin = false;
            MessageDigest md   = MessageDigest.getInstance("SHA-224");
            byte[] hashPassEnt = md.digest(password.getBytes(StandardCharsets.UTF_8));
            Usuario newUser = new Usuario(username,nombre,hashPassEnt,admin);
            try{
                new CrudGenerico<Usuario>(Usuario.class).crear(newUser);
                Map<String,Object> atributos = new HashMap<>();
                atributos.put("mensaje", "Registrado con exito, proceda a autenticarse");
                return new FreeMarkerEngine().render(new ModelAndView(atributos,"login.fml"));
            }catch (Exception ex){
                Map<String,Object> atributos = new HashMap<>();
                atributos.put("mensaje", "algo salió mal, intentelo de nuevo");
                return new FreeMarkerEngine().render(new ModelAndView(atributos,"register.fml"));
            }
        });
        Spark.get("/users",(request, response) -> {
            Map<String,Object> atributos = new HashMap<>();
            atributos.put("users", ServUsuario.getInstance().listarUsuario() );
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"users.fml"));
        });
        Spark.post("/adminRights/:idUser",(request, response) -> {
            Usuario user = ServUsuario.getInstance().getUser(Long.parseLong(request.params("idUser")));
            user.setAdmin(true);
            ServUsuario.getInstance().editar(user);
            response.redirect("/users", 307);
            return null;
        });

        Spark.get("/myUrls",(request, response) -> {
            Map<String,Object> atributos = new HashMap<>();
            Usuario u = request.session().attribute("usuario");
            atributos.put("urls", ServUrlCorta.getInstance().getURLsByUser(u.getId()));
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"urls.fml"));
        });

        Spark.get("/allUrls",(request, response) -> {
            Map<String,Object> atributos = new HashMap<>();
            atributos.put("urls", ServUrlCorta.getInstance().getAllUrls());
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"urls.fml"));
        });


        Spark.delete("/delUrl/:idUrl",(request, response) -> {
            new CrudGenerico<>(UrlCorta.class).eliminar(Long.parseLong(request.params("idUrl")));
            if(request.queryParams("user")!=null)
                response.redirect("/myUrls");
            else
                response.redirect("allUrls");
            return null;
        });
    }
}
