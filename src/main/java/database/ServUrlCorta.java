package database;

import models.Usuario;

public class ServUrlCorta extends CrudGenerico<Usuario> {

    private ServUrlCorta() {
        super(Usuario.class);
    }
    private static ServUrlCorta instance;
    public ServUrlCorta getInstance(){
        if (instance==null)
            instance = new ServUrlCorta();
        return instance;
    }


}
