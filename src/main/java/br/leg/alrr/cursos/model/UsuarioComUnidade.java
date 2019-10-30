package br.leg.alrr.cursos.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * @Organization ALRR
 * @author Heliton Nascimento
 * @Matricula 14583
 */
@Table(schema = "escolegis_academico")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class UsuarioComUnidade extends Usuario implements Serializable{

    
    @ManyToOne
    private Unidade unidade;
    //========================================================================//
    public UsuarioComUnidade() {
        super();
    }

    public UsuarioComUnidade(Long id) {
        super(id);
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
    
}
