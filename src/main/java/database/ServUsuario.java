package database;

import models.Usuario;

import javax.persistence.EntityManager;
import java.util.List;

public class ServUsuario extends CrudGenerico<Usuario> {

    private ServUsuario() {
        super(Usuario.class);
    }
    private static ServUsuario instance;
    public ServUsuario getInstance(){
        if (instance==null)
            instance = new ServUsuario();
        return instance;
    }


}
