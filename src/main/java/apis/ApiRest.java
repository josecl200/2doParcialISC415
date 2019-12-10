package apis;

import com.google.gson.*;
import database.CrudGenerico;
import database.ServEstadistica;
import database.ServUrlCorta;
import database.ServUsuario;
import models.UrlCorta;
import spark.Spark;
import io.jsonwebtoken.*;
import utilidades.JsonUtilidades;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.security.Key;
import java.util.*;


public class ApiRest {
    private static String privKey="wqFRdcOpIGFsZWdyw61hIGN1YW5kbyBtZSBkaWplcm9uClZhbW9zIGEgbGEgY2FzYSBkZWwgU2XD" +
            "sW9yIQpZYSBlc3TDoW4gcGlzYW5kbwpOdWVzdHJvcyBwaWVzIHR1cwpVbWJyYWxlcyBKZXJ1c2Fs" +
            "w6lu"; //k alegria qan2 m di g ron jjjjjjj
    public static void restful(){
        Spark.post("/token", (request, response) -> {
            String usuario = request.queryParams("usuario");
            String pass = request.queryParams("pass");
            if(ServUsuario.getInstance().validateCredentials(usuario,pass)){
                JsonObject json = new JsonObject();
                json.addProperty("token", createJWT(UUID.randomUUID().toString(), "https://short.josecl200.me", "Access Token", 0));
                return json;
            }else{
                return null;
            }
        });

        Spark.path("/rest",()->{
            /*Spark.before("/*",(request, response) ->{
                String token = request.headers("token") != null ? request.headers("token") : request.headers("TOKEN");
                if (token == null || token.isEmpty() || decodeJWT(token)==null) {
                    Spark.halt(401);
                }
            });*///TODO: probar JWT con login de usuario
            Spark.afterAfter("/*", (request, response) ->
                    response.header("Content-Type", "application/json")
            );

            Spark.get("/urls",(request, response) -> {
                List<UrlCorta> urls = ServUrlCorta.getInstance().getAllUrls();
                String urlJsonString = JsonUtilidades.toJson(urls);
                JsonParser parser = new JsonParser();
                JsonElement urlElement = parser.parse(urlJsonString);
                JsonArray urlArray = urlElement.getAsJsonArray();
                for (int i=0;i<urlArray.size();i++) {
                    JsonObject urlJson = urlArray.get(i).getAsJsonObject();
                    long idUrl = urlJson.get("id").getAsLong();
                    HashMap<String,Object> stats = new HashMap<>();
                    stats.put("OSs",ServEstadistica.getInstance().getOss(idUrl));
                    stats.put("Browsers",ServEstadistica.getInstance().getBrowsers(idUrl));
                    stats.put("ActPerHour",ServEstadistica.getInstance().getActivityPerHour(idUrl));
                    urlJson.add("urlCorta",parser.parse(JsonUtilidades.toJson("https://short.josecl200.me/r/"+ Base64.getEncoder().encodeToString(ByteBuffer.allocate(8).putLong(idUrl).array()))));
                    urlJson.add("stats",parser.parse(JsonUtilidades.toJson(stats)));
                    if(!urlJson.get("creador").isJsonNull())
                        urlJson.add("creador",parser.parse(JsonUtilidades.toJson(ServUrlCorta.getInstance().encontrar(idUrl).getCreador().getUsername())));
                    urlArray.set(i,urlJson);
                }
                return urlArray.toString();
            });
            Spark.get("/urls/:usuario",(request, response) -> {
                List<UrlCorta> urls = ServUrlCorta.getInstance().getURLsByUser(ServUsuario.getInstance().getUser(request.params("usuario")));
                String urlJsonString = JsonUtilidades.toJson(urls);
                JsonParser parser = new JsonParser();
                JsonElement urlElement = parser.parse(urlJsonString);
                JsonArray urlArray = urlElement.getAsJsonArray();
                for (int i=0;i<urlArray.size();i++) {
                    JsonObject urlJson = urlArray.get(i).getAsJsonObject();
                    long idUrl = urlJson.get("id").getAsLong();
                    HashMap<String,Object> stats = new HashMap<>();
                    stats.put("OSs",ServEstadistica.getInstance().getOss(idUrl));
                    stats.put("Browsers",ServEstadistica.getInstance().getBrowsers(idUrl));
                    stats.put("ActPerHour",ServEstadistica.getInstance().getActivityPerHour(idUrl));
                    urlJson.add("urlCorta",parser.parse(JsonUtilidades.toJson("https://short.josecl200.me/r/"+ Base64.getEncoder().encodeToString(ByteBuffer.allocate(8).putLong(idUrl).array()))));
                    urlJson.add("stats",parser.parse(JsonUtilidades.toJson(stats)));
                    if(!urlJson.get("creador").isJsonNull())
                        urlJson.add("creador",parser.parse(JsonUtilidades.toJson(ServUrlCorta.getInstance().encontrar(idUrl).getCreador().getUsername())));
                    urlArray.set(i,urlJson);
                }
                return urlArray.toString();
            });

            Spark.post("/newUrl/",(request, response) -> {
                JsonParser parser = new JsonParser();
                JsonObject urlnueva = parser.parse(request.body()).getAsJsonObject();
                System.out.println(urlnueva);
                urlnueva.add("creador",parser.parse(JsonUtilidades.toJson(ServUsuario.getInstance().getUser(urlnueva.get("creador").getAsString()))));
                UrlCorta newUrlObject = new Gson().fromJson(urlnueva,UrlCorta.class);
                new CrudGenerico<>(UrlCorta.class).crear(newUrlObject);

                return newUrlObject;
            },JsonUtilidades.json());
        });
    }

    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(privKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }
    public static Claims decodeJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(privKey))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
