package database;

import models.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class ServUsuario extends CrudGenerico<Usuario> {

    private ServUsuario() {
        super(Usuario.class);
    }
    private static ServUsuario instance;
    public static ServUsuario getInstance(){
        if (instance==null)
            instance = new ServUsuario();
        return instance;
    }

    public List<Usuario> listarUsuario() {
        EntityManager em = getEntityManager();
        Query cuero = em.createQuery("SELECT U FROM USUARIO U");
        return (List<Usuario>) cuero.getResultList();
    }

    public Usuario getUser(Long id) {
        Usuario user = null;
        EntityManager em = getEntityManager();
        Query cuero = em.createQuery("SELECT U FROM USUARIO U WHERE U.ID = :ID");
        cuero.setParameter("ID", id);
        user = (Usuario) cuero.getResultList().get(0);
        return user;
    }

    public Usuario getUser(String username) {
        Usuario user = null;
        EntityManager em = getEntityManager();
        Query cuero = em.createQuery("SELECT U FROM USUARIO U WHERE U.USERNAME = :USERNAME");
        cuero.setParameter("USERNAME", username);
        user = (Usuario) cuero.getResultList().get(0);
        return user;
    }

    public boolean validateCredentials(String username, String password) throws NoSuchAlgorithmException {
        boolean login      = false;
        MessageDigest md   = MessageDigest.getInstance("SHA-224");
        byte[] hashPassEnt = md.digest(password.getBytes(StandardCharsets.UTF_8));
        Usuario user = getUser(username);
        if(user!=null)
            if(Arrays.equals(user.getPassword(),hashPassEnt))
                login=true;

        return login;
    }


}
