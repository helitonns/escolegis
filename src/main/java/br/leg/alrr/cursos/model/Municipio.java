package br.leg.alrr.cursos.model;

import br.leg.alrr.cursos.util.BaseEntity;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="municipio_auditoria", schema = "escolegis_academico")
@Table(schema = "escolegis_academico")
@Entity
public class Municipio implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nome;

    @OneToMany(mappedBy = "municipio")
    private List<Bairro> bairros;

    //========================================================================//
    public Municipio() {
        // TODO Auto-generated constructor stub
    }

    public Municipio(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long i) {
        this.id = i;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Bairro> getBairros() {
        return bairros;
    }

    public void setBairros(List<Bairro> bairros) {
        this.bairros = bairros;
    }

    //========================================================================//
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
        Municipio other = (Municipio) obj;
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
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "ID: " + id + " -- Nome: " + nome;
    }

}
