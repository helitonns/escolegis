package br.leg.alrr.cursos.model;

import br.leg.alrr.cursos.util.BaseEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * @Organization ALRR
 * @author Heliton Nascimento
 * @Matricula 14583
 */
@Entity
@Table(schema = "autenticacao")
public class Autorizacao implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    private Privilegio privilegio;
    
    private boolean status;
    //========================================================================//

    public Autorizacao() {
    }

    public Autorizacao(Long id) {
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Privilegio getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(Privilegio privilegio) {
        this.privilegio = privilegio;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public UsuarioComUnidade getUsuarioComUnidade() {
        return (UsuarioComUnidade) usuario;
    }

}
