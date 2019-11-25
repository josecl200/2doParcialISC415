package database;

import models.UrlCorta;

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

    public List<UrlCorta> getURLsByUser(long idUser){
        EntityManager em = getEntityManager();
        Query stats = em.createQuery("SELECT E FROM URL_CORTA E WHERE E.CREADOR = :IDUSER", UrlCorta.class);
        stats.setParameter("IDUSER", idUser);
        return stats.getResultList();
    }

}
