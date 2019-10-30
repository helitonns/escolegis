package br.leg.alrr.cursos.model;

import br.leg.alrr.cursos.util.BaseEntity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="curso_auditoria", schema = "escolegis_academico")
@Table(schema = "escolegis_academico")
@Entity
public class Curso implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private boolean status;
    private boolean iniciado;
    
    @Temporal(TemporalType.DATE)
    private Date dataDeInicio;
    
    @Temporal(TemporalType.DATE)
    private Date dataDeTermino;
    
    @Column(length = 2276)
    private String conteudoProgramatico;
            
    @ManyToOne(fetch = FetchType.EAGER)
    private Unidade unidade;
    
    @Transient
    private int cargaHoraria;
    //========================================================================//

    public Curso() {
    }

    public Curso(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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


    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getConteudoProgramatico() {
        return conteudoProgramatico;
    }

    public void setConteudoProgramatico(String conteudoProgramatico) {
        this.conteudoProgramatico = conteudoProgramatico;
    }

    
    public boolean getAlcancouTermoFinal(){
        GregorianCalendar g1 = new GregorianCalendar();
        g1.setTime(dataDeTermino);
        g1.set(GregorianCalendar.HOUR, 23);
        g1.set(GregorianCalendar.MINUTE, 59);
        g1.set(GregorianCalendar.SECOND, 59);
        
        GregorianCalendar g2 = new GregorianCalendar();
        g2.set(GregorianCalendar.HOUR, 0);
        g2.set(GregorianCalendar.MINUTE, 0);
        g2.set(GregorianCalendar.SECOND, 0);
        
        return g1.before(g2);
    }
    
    public String getNomeComPeriodo(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        
        try {
            return sb.append(nome)
                    .append(" | ")
                    .append(sdf.format((Date) dataDeInicio))
                    .append(" - ")
                    .append(sdf.format((Date) dataDeTermino))
                    .toString();
        } catch (Exception e) {
        }
        return "curso";
    }
}
