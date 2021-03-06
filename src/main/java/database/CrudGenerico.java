package database;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Supplier;

public class CrudGenerico<T> {

    private static EntityManagerFactory emf;
    private Class<T> claseEntidad;

    public CrudGenerico(Class<T> claseEntidad) {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        }

        this.claseEntidad = claseEntidad;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private Object getValorCampo(T entidad) {
        for (Field f : entidad.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                try {
                    f.setAccessible(true);
                    Object valorCampo = f.get(entidad);
                    return valorCampo;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public void crear(T entidad) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        try {
            if (getValorCampo(entidad) != null && em.find(claseEntidad, getValorCampo(entidad)) == null) {
                em.persist(entidad);
                em.flush();
                em.refresh(entidad);
                em.getTransaction().commit();
            } else {
                System.out.println("Ya existe esa entidad.");
            }
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void editar(T entidad) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        try {
            em.merge(entidad);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void eliminar(Object entidadId) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        try {
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public T encontrar(Object id) {
        EntityManager em = getEntityManager();

        try {
            return em.find(claseEntidad, id);
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<T> listar() {
        EntityManager em = getEntityManager();

        try {
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return em.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }


}