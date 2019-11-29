package br.leg.alrr.cursos.model;

import br.leg.alrr.cursos.util.BaseEntity;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entidade usada para gerar estatísticas sobre o acesso da aplicação.
 *
 * @author Heliton Nascimento
 * @since 2019-11-27
 * @version 1.0
 */
@Table(schema = "escolegis_academico")
@Entity
public class Acesso implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataDeAcesso;

    private LocalTime momentoDoAcesso;

    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    //========================================================================//
    public Acesso() {
    }

    public Acesso(Long id) {
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

    public LocalDate getDataDeAcesso() {
        return dataDeAcesso;
    }

    public void setDataDeAcesso(LocalDate dataDeAcesso) {
        this.dataDeAcesso = dataDeAcesso;
    }

    public LocalTime getMomentoDoAcesso() {
        return momentoDoAcesso;
    }

    public void setMomentoDoAcesso(LocalTime momentoDoAcesso) {
        this.momentoDoAcesso = momentoDoAcesso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Acesso other = (Acesso) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
