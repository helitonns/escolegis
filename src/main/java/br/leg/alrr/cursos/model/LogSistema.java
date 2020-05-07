package br.leg.alrr.cursos.model;

import br.leg.alrr.cursos.business.TipoAcao;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classe usada para fazer o log do Sitema.
 * 
 * @author Heliton Nascimento
 * @since 2020-05-01
 * @version 1.0
 * @see TipoAcao
 */

@Entity
@Table(schema = "escolegis_academico")
public class LogSistema implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    private LocalDateTime dataOperacao;
    
    @Enumerated(EnumType.STRING)
    private TipoAcao tipoAcao;
    
    private String mensagemDeEntrada;
    
    private String mensagemDeSaida;

    //--------------------------------------------------------------------------
    public LogSistema() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(LocalDateTime dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public TipoAcao getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(TipoAcao tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public String getMensagemDeEntrada() {
        return mensagemDeEntrada;
    }

    public void setMensagemDeEntrada(String mensagemDeEntrada) {
        this.mensagemDeEntrada = mensagemDeEntrada;
    }

    public String getMensagemDeSaida() {
        return mensagemDeSaida;
    }

    public void setMensagemDeSaida(String mensagemDeSaida) {
        this.mensagemDeSaida = mensagemDeSaida;
    }
    
    
    
}
