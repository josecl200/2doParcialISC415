package database;

import models.UrlCorta;
import models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServUrlCorta extends CrudGenerico<UrlCorta> {

    private ServUrlCorta() {
        super(UrlCorta.class);
    }
    private static ServUrlCorta instance;
    public static ServUrlCorta getInstance(){
        if (instance==null)
            instance = new ServUrlCorta();
        return instance;
    }

    public List<UrlCorta> getURLsByUser(Usuario u){
        EntityManager em = getEntityManager();
        Query stats = em.createQuery("SELECT E FROM UrlCorta E WHERE E.creador = :USER", UrlCorta.class);
        return stats.setParameter("USER", u).getResultList();
    }

    public List<UrlCorta> getAllUrls(){
        EntityManager em = getEntityManager();
        Query stats = em.createQuery("SELECT E FROM UrlCorta E", UrlCorta.class);
        return stats.getResultList();
    }

}
