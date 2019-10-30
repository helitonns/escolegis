package br.leg.alrr.cursos.model;

import br.leg.alrr.cursos.util.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="frequencia_auditoria", schema = "escolegis_academico")
@Table(schema = "escolegis_academico")
@Entity
public class Frequencia implements Serializable, BaseEntity, Comparable<Frequencia> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Turma turma;

    @ManyToOne
    private Aluno aluno;

    private boolean presenca;
    private boolean faltaJustificada;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataFrequencia;

//==============================================================================
    public Frequencia() {
    }

    public Frequencia(Long id) {
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

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public boolean isPresenca() {
        return presenca;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }

    public Date getDataFrequencia() {
        return dataFrequencia;
    }

    public void setDataFrequencia(Date dataFrequencia) {
        this.dataFrequencia = dataFrequencia;
    }

    public boolean isFaltaJustificada() {
        return faltaJustificada;
    }

    public void setFaltaJustificada(boolean faltaJustificada) {
        this.faltaJustificada = faltaJustificada;
    }

    @Override
    public int compareTo(Frequencia f) {
        if (this.aluno.getNome().compareTo(f.getAluno().getNome()) > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
