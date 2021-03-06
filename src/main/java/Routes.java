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
import ua_parser.Parser;
import ua_parser.Client;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class Routes {
    public static void rutas(){
        Spark.get("/",(request, response) -> {
            Map<String,Object> atributos = new HashMap<>();
            atributos.put("usuario", request.session().attribute("usuario"));
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"index.fml"));

        });

        Spark.post("/acortar",(request, response) -> {
            UrlCorta url = new UrlCorta();
            url.setCreador(request.session().attribute("usuario"));
            url.setFecha(new Date());
            url.setUrl_orig(request.queryParams("url"));
            ServUrlCorta.getInstance().crear(url);
            if (url.getCreador()==null){
                List<UrlCorta> tempUrls = (List<UrlCorta>) request.session().attribute("tempUrls");
                if(tempUrls==null)
                    tempUrls=new ArrayList<UrlCorta>();
                tempUrls.add(url);
                request.session().attribute("tempUrls",tempUrls);
            }
            return "https://short.josecl200.me/r/"+Base64.getEncoder().encodeToString(ByteBuffer.allocate(8).putLong(url.getId()).array());
        });

        Spark.get("/r/:idUrl",(request, response) -> {
            ByteBuffer byteId = ByteBuffer.wrap(Base64.getDecoder().decode(request.params("idUrl")));
            long idUrl = byteId.getLong();
            String os = "unknown";
            String browser = "unknown";
            Parser uaParser = new Parser();
            Client c = uaParser.parse(request.userAgent());
            os = c.os.family;
            browser = c.userAgent.family;
            UrlCorta url = ServUrlCorta.getInstance().encontrar(idUrl);
            Estadisticas est = new Estadisticas();
            est.setFecha(new Date());
            est.setIp(request.ip());
            est.setSistema_op(os);
            est.setNavegador(browser);
            est.setUrl_corta(url);
            ServEstadistica.getInstance().crear(est);
            String redir = url.getUrl_orig();
            response.redirect(redir);
            return null;
        });

        Spark.get("/stats/:idUrl",(request, response) -> {
            ByteBuffer byteId = ByteBuffer.wrap(Base64.getDecoder().decode(request.params("idUrl")));
            long idUrl = byteId.getLong();
            List<Estadisticas> stats = ServEstadistica.getInstance().getStatsForURL(idUrl);
            UrlCorta url = ServUrlCorta.getInstance().encontrar(idUrl);
            Map<String,Long> browsers = ServEstadistica.getInstance().getBrowsers(idUrl);
            Map<String,Long> oss = ServEstadistica.getInstance().getOss(idUrl);
            Map<String,Long> actPorHora = ServEstadistica.getInstance().getActivityPerHour(idUrl);
            Map<String,Object> atributos = new HashMap<>();
            atributos.put("stats", stats);
            atributos.put("browsers",browsers);
            atributos.put("oss",oss);
            atributos.put("actPorHora",actPorHora);
            atributos.put("usuario", request.session().attribute("usuario"));
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"estadisticas.fml"));
        });

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
        Spark.post("/register",(request, response) -> {
            String username    = request.queryParams("usuario");
            String password    = request.queryParams("contrasena");
            String nombre      = request.queryParams("nombre");
            MessageDigest md   = MessageDigest.getInstance("SHA-224");
            byte[] hashPassEnt = md.digest(password.getBytes(StandardCharsets.UTF_8));
            Usuario newUser    = new Usuario(username,nombre,hashPassEnt, false);
            List<UrlCorta> tempUrls = request.session().attribute("tempUrls");
            try{
                ServUsuario.getInstance().crear(newUser);
                if(tempUrls!=null){
                    for (UrlCorta u: tempUrls) {
                        u.setCreador(newUser);
                        ServUrlCorta.getInstance().editar(u);
                    }
                }
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
            atributos.put("usuario", request.session().attribute("usuario"));
            atributos.put("users", ServUsuario.getInstance().listarUsuario() );
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"listar.fml"));
        });

        Spark.post("/adminRights/:idUser",(request, response) -> {
            Usuario user = ServUsuario.getInstance().getUser(Long.parseLong(request.params("idUser")));
            if(user.isAdmin()){
                user.setAdmin(false);
            }else{
                user.setAdmin(true);
            }
            ServUsuario.getInstance().editar(user);
            response.redirect("/users");
            return null;
        });

        Spark.get("/myUrls",(request, response) -> {
            Usuario u = request.session().attribute("usuario");
            List<UrlCorta> tempUrls = request.session().attribute("tempUrls");
            Map<String,Object> atributos = new HashMap<>();
            atributos.put("usuario", request.session().attribute("usuario"));
            if(u!=null){
                atributos.put("urls", ServUrlCorta.getInstance().getURLsByUser(u));
            }else{
                atributos.put("urls", tempUrls);
            }
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"listar.fml"));
        });

        Spark.get("/allUrls",(request, response) -> {
            Map<String,Object> atributos = new HashMap<>();
            atributos.put("usuario", request.session().attribute("usuario"));
            atributos.put("urls", ServUrlCorta.getInstance().getAllUrls());
            return new FreeMarkerEngine().render(new ModelAndView(atributos,"listar.fml"));
        });


        Spark.post("/delUrl/:idUrl",(request, response) -> {
            ByteBuffer byteId = ByteBuffer.wrap(Base64.getDecoder().decode(request.params("idUrl")));
            long idUrl = byteId.getLong();
            List<Estadisticas> ests = ServEstadistica.getInstance().getStatsForURL(idUrl);
            for (Estadisticas e:ests) {
                ServEstadistica.getInstance().eliminar(e.getId());
            }
            if(request.session().attribute("usuario")==null){
                List<UrlCorta> tempUrls = request.session().attribute("tempUrls");
                UrlCorta url = ServUrlCorta.getInstance().encontrar(idUrl);
                for(int i=0;i<tempUrls.size();i++)
                    if(tempUrls.get(i).getId()==idUrl)
                        tempUrls.remove(i);
                request.session().attribute("tempUrls",tempUrls);
            }
            ServUrlCorta.getInstance().eliminar(idUrl);


            if(request.queryParams("path")=="all")
                response.redirect("/allUrls");
            else
                response.redirect("/myUrls");
            return null;
        });

        Spark.post("/delUsr/:idUsr",(request, response) -> {
            List<UrlCorta> list = ServUrlCorta.getInstance().getURLsByUser(ServUsuario.getInstance().getUser(Long.parseLong(request.params("idUsr"))));
            for (UrlCorta u: list) {
                u.setCreador(null);
                ServUrlCorta.getInstance().editar(u);
            }
            ServUsuario.getInstance().eliminar(Long.parseLong(request.params("idUsr")));
            response.redirect("//users");
            return null;
        });

        Spark.get("/logout",(request, response) -> {
            request.session().invalidate();
            response.removeCookie("USER");
            response.redirect("/");
            return null;
        });
    }
}
