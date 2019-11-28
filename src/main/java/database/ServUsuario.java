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
        Query cuero = em.createQuery("SELECT U FROM Usuario U");
        return (List<Usuario>) cuero.getResultList();
    }

    public Usuario getUser(Long id) {
        Usuario user = null;
        EntityManager em = getEntityManager();
        Query cuero = em.createQuery("SELECT U FROM Usuario U WHERE U.id = :ID", Usuario.class);
        if(cuero.setParameter("ID", id).getResultList().size()>0)
            user = (Usuario) cuero.setParameter("ID", id).getResultList().get(0);
        return user;
    }

    public Usuario getUser(String username) {
        Usuario user = null;
        EntityManager em = getEntityManager();
        Query cuero = em.createQuery("SELECT U FROM Usuario U WHERE U.username = :USERNAME");
        if(cuero.setParameter("USERNAME", username).getResultList().size()>0)
            user = (Usuario) cuero.setParameter("USERNAME", username).getResultList().get(0);
        return user;
    }

    public void createAdmin() throws NoSuchAlgorithmException {
        Usuario admin=new Usuario();
        admin.setAdmin(true);
        admin.setNombre("Administrador");
        MessageDigest md   = MessageDigest.getInstance("SHA-224");
        byte[] hashPassEnt = md.digest("admin".getBytes(StandardCharsets.UTF_8));
        admin.setPassword(hashPassEnt);
        admin.setUsername("root");
        if(getUser(admin.getUsername())==null){
            crear(admin);
        }
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
