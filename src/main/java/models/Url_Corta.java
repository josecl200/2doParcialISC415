package models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class Url_Corta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue
    private long id;

    @ManyToOne
    private Usuario creador;

    @Column(name = "URL_ORIG", columnDefinition = "TEXT")
    private String url_orig;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp fecha;

    public Url_Corta() {
    }

    public Url_Corta(Usuario creador, String url_orig, Timestamp fecha) {
        this.creador = creador;
        this.url_orig = url_orig;
        this.fecha = fecha;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public String getUrl_orig() {
        return url_orig;
    }

    public void setUrl_orig(String url_orig) {
        this.url_orig = url_orig;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
