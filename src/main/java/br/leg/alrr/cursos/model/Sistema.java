package br.leg.alrr.cursos.model;

import java.io.Serializable;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;


/**
 * @Organization ALRR
 * @author Heliton Nascimento
 * @Matricula 14583
 */
@Entity
@Table(schema = "autenticacao")
@Inheritance(strategy = InheritanceType.JOINED)
public class Sistema implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    
    @Column(unique = true)
    private String chave;
    
    private boolean status;

    //========================================================================//
    public Sistema() {
    }

    public Sistema(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    
}
