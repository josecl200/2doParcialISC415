package models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Estadisticas implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private UrlCorta url_corta;
    private String sistema_op;
    private String navegador;
    private Date fecha;
    @Column(name = "IPV4ADDR")
    private String ip;

    public Estadisticas() {
    }

    public Estadisticas(UrlCorta url_corta, String sistema_op, String navegador, Timestamp fecha, String ip) {
        this.url_corta = url_corta;
        this.sistema_op = sistema_op;
        this.navegador = navegador;
        this.fecha = fecha;
        this.ip = ip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UrlCorta getUrl_corta() {
        return url_corta;
    }

    public void setUrl_corta(UrlCorta url_corta) {
        this.url_corta = url_corta;
    }

    public String getSistema_op() {
        return sistema_op;
    }

    public void setSistema_op(String sistema_op) {
        this.sistema_op = sistema_op;
    }

    public String getNavegador() {
        return navegador;
    }

    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
