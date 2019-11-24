package database;

import models.Estadisticas;
import models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Queue;

public class ServEstadistica extends CrudGenerico<Usuario> {

    private ServEstadistica() {
        super(Usuario.class);
    }
    private static ServEstadistica instance;
    public ServEstadistica getInstance(){
        if (instance==null)
            instance = new ServEstadistica();
        return instance;
    }

    public List<Estadisticas> getStatsForURL(long idUrl){
        EntityManager em = getEntityManager();
        Query stats = em.createQuery("SELECT E FROM ESTADISTICA E WHERE E.URL_CORTA = :URL", Estadisticas.class);
        stats.setParameter("URL", idUrl);
        return stats.getResultList();
    }


}
