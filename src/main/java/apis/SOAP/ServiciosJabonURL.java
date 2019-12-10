package apis.SOAP;
import database.CrudGenerico;
import database.ServUrlCorta;
import database.ServUsuario;
import models.UrlCorta;
import models.Usuario;

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
        return ServUrlCorta.getInstance().getAllUrls();
    }
    @WebMethod
    public List<UrlCorta> getUserURLS(String user){
        Usuario u = ServUsuario.getInstance().getUser(user);
        if (u==null)
            return null;
        return ServUrlCorta.getInstance().getURLsByUser(u);
    }
    @WebMethod
    public UrlCorta insertarURL(String url, String user){
        Usuario u = ServUsuario.getInstance().getUser(user);
        UrlCorta newUrl = new UrlCorta();
        newUrl.setCreador(u);
        newUrl.setFecha(new Date());
        newUrl.setUrl_orig(url);
        ServUrlCorta.getInstance().crear(newUrl);

        return newUrl;
    }

}
