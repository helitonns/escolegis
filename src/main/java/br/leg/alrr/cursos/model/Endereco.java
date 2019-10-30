package br.leg.alrr.cursos.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="endereco_auditoria", schema = "escolegis_academico")
@Table(schema = "escolegis_academico")
@Entity
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String rua;
    private String complemento;
    private String numero;
    private String cep;

    @ManyToOne
    private Bairro bairro;

    //========================================================================//
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        
        if (bairro != null) {
            sb.append(bairro.getMunicipio().getNome()).append(", ");;
            sb.append(bairro.getNome()).append(", ");
        }
        
        if (rua != null) {
            sb.append(rua).append(", ");
        }
        if (numero != null) {
            sb.append(numero);
        }
        
        return sb.toString();
    }
}
