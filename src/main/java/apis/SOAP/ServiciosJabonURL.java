package apis.SOAP;
import database.CrudGenerico;
import database.ServEstadistica;
import database.ServUrlCorta;
import database.ServUsuario;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import models.Estadisticas;
import models.UrlCorta;
import models.Usuario;
import kong.unirest.HttpResponse;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@WebService
public class ServiciosJabonURL {
    @WebMethod
    public List<UrlCorta> getAllURLS(){
        List<UrlCorta> urls = ServUrlCorta.getInstance().getAllUrls();;
        for(int i=0;i<urls.size();i++){
            String linkPreviewAPI = "https://api.linkpreview.net/?key=5df79533328289b6e0fb52eba76006d3d11ead82fad4d&q=" + urls.get(i).getUrl_orig();
            HttpResponse<JsonNode> linkPreviewResult = Unirest.get(linkPreviewAPI).asJson();
            String image = linkPreviewResult.getBody().getObject().getString("image");
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            urls.get(i).setB64img(base64Image);
        }
        return urls;
    }

    @WebMethod
    public List<Estadisticas> getStatsByURL(long idUrl){
        return ServEstadistica.getInstance().getStatsForURL(idUrl);
    }

    @WebMethod
    public List<UrlCorta> getUserURLS(String user){
        Usuario u = ServUsuario.getInstance().getUser(user);
        if (u==null)
            return null;
        List<UrlCorta> urls = ServUrlCorta.getInstance().getURLsByUser(u);;
        for(int i=0;i<urls.size();i++){
            String linkPreviewAPI = "https://api.linkpreview.net/?key=5df79533328289b6e0fb52eba76006d3d11ead82fad4d&q=" + urls.get(i).getUrl_orig();
            HttpResponse<JsonNode> linkPreviewResult = Unirest.get(linkPreviewAPI).asJson();
            String image = linkPreviewResult.getBody().getObject().getString("image");
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            urls.get(i).setB64img(base64Image);
        }
        return urls;
    }

    @WebMethod
    public UrlCorta insertarURL(String url, String user){
        Usuario u = ServUsuario.getInstance().getUser(user);
        UrlCorta newUrl = new UrlCorta();
        newUrl.setCreador(u);
        newUrl.setFecha(new Date());
        newUrl.setUrl_orig(url);
        String linkPreviewAPI = "https://api.linkpreview.net/?key=5df79533328289b6e0fb52eba76006d3d11ead82fad4d&q=" + newUrl.getUrl_orig();
        HttpResponse<JsonNode> linkPreviewResult = Unirest.get(linkPreviewAPI).asJson();
        String image = linkPreviewResult.getBody().getObject().getString("image");
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        ServUrlCorta.getInstance().crear(newUrl);
        newUrl.setB64img(base64Image);
        return newUrl;
    }

}
