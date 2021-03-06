package models;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

@Entity
public class UrlCorta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Usuario creador;

    @Column(name = "URL_ORIG", columnDefinition = "TEXT")
    private String url_orig;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Transient
    private String b64img;

    public UrlCorta() {
    }

    public UrlCorta(Usuario creador, String url_orig, Timestamp fecha) {
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String idAsb64(){
        return Base64.getEncoder().encodeToString(ByteBuffer.allocate(8).putLong(id).array());
    }

    public String getB64img() {
        return b64img;
    }

    public void setB64img(String b64img) {
        this.b64img = b64img;
    }
}
