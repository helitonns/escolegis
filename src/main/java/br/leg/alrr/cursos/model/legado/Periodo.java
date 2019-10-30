package br.leg.alrr.cursos.model.legado;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Heliton
 */
@Entity
@Table(name = "periodo_legado")
public class Periodo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date dataDeInicio;
    
    @Temporal(TemporalType.DATE)
    private Date dataDeTermino;
    
    private boolean status;

    public Periodo() {
    }
    
    public Periodo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataDeInicio() {
        return dataDeInicio;
    }

    public void setDataDeInicio(Date dataDeInicio) {
        this.dataDeInicio = dataDeInicio;
    }

    public Date getDataDeTermino() {
        return dataDeTermino;
    }

    public void setDataDeTermino(Date dataDeTermino) {
        this.dataDeTermino = dataDeTermino;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public String getDataConcatenada(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dataDeInicio)+" - "+sdf.format(dataDeTermino);
    }
    
}
