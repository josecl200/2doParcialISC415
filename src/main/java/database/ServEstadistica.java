package database;

import models.Estadisticas;
import models.UrlCorta;
import models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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


}
