package database;

import models.Estadisticas;
import models.UrlCorta;
import models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class ServEstadistica extends CrudGenerico<Estadisticas> {

    private ServEstadistica() {
        super(Estadisticas.class);
    }
    private static ServEstadistica instance;
    public static ServEstadistica getInstance(){
        if (instance==null)
            instance = new ServEstadistica();
        return instance;
    }

    public List<Estadisticas> getStatsForURL(long idUrl){
        EntityManager em = getEntityManager();
        Query stats = em.createQuery("SELECT E FROM Estadisticas E WHERE E.url_corta = :URL", Estadisticas.class);
        stats.setParameter("URL", new CrudGenerico<>(UrlCorta.class).encontrar(idUrl));
        return stats.getResultList();
    }

    public HashMap<String,Long> getBrowsers(long idUrl){
        HashMap<String,Long> browserCount = new HashMap<String, Long>();
        EntityManager em = getEntityManager();
        Query browsers = em.createQuery("SELECT e.navegador, count(e) FROM Estadisticas e WHERE e.url_corta = :URL GROUP BY e.navegador");
        browsers.setParameter("URL", new CrudGenerico<>(UrlCorta.class).encontrar(idUrl));
        for (Object res : browsers.getResultList()){
            Object[] fila = (Object[]) res;
            browserCount.put((String) fila[0],(Long) fila[1]);
        }
        return browserCount;
    }

    public HashMap<String,Long> getOss(long idUrl){
        HashMap<String,Long> osCount = new HashMap<String, Long>();
        EntityManager em = getEntityManager();
        Query browsers = em.createQuery("SELECT e.sistema_op, count(e) FROM Estadisticas e WHERE e.url_corta = :URL GROUP BY e.sistema_op");
        browsers.setParameter("URL", new CrudGenerico<>(UrlCorta.class).encontrar(idUrl));
        for (Object res : browsers.getResultList()){
            Object[] fila = (Object[]) res;
            osCount.put((String) fila[0],(Long) fila[1]);
        }
        return osCount;
    }


}
