package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Acesso;
import br.leg.alrr.cursos.persistence.AcessoDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class EstatisticaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AcessoDAO acessoDAO;

    private ArrayList<Acesso> acessos;
    
    private Long quantidadeDeAcesso;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        contarAcessosDoDia();
        listarAcessosDoDia();
    }

    private void contarAcessosDoDia() {
        try {
            quantidadeDeAcesso = acessoDAO.contarAcessosPorData(LocalDate.now());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }
    
    private void listarAcessosDoDia(){
        try {
            acessos = (ArrayList<Acesso>) acessoDAO.listarAcessosPorData(LocalDate.now());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        acessos = new ArrayList<>();
    }

    public String cancelar() {
        return "estatistica.xhtml" + "?faces-redirect=true";
    }

    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        acessoDAO = null;
        acessos = null;
        quantidadeDeAcesso = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }

    //==========================================================================

    public ArrayList<Acesso> getAcessos() {
        return acessos;
    }

    public Long getQuantidadeDeAcesso() {
        return quantidadeDeAcesso;
    }
}
