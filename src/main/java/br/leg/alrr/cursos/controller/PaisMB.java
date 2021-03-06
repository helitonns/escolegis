package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Pais;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.persistence.PaisDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
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
public class PaisMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PaisDAO paisDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Pais pais;

    private ArrayList<Pais> paises;

    private Pais paisSelecionado;

    private boolean removerPaisSelecionado = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarPais() {
        try {
            if (pais.getId() != null) {
                paisDAO.atualizar(pais);
                FacesUtils.addInfoMessageFlashScoped("Pais atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método PaisMB.salvarPais() para atualizar o país "+ pais.getId()+".");
            } else {
                paisDAO.salvar(pais);
                FacesUtils.addInfoMessageFlashScoped("Pais salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método PaisMB.salvarPais() para salvar o país "+ pais.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "pais.xhtml" + "?faces-redirect=true";
    }

    public void listarPaises() {
        try {
            paises = (ArrayList<Pais>) paisDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerPais() {
        try {
            if (removerPaisSelecionado) {
                paisDAO.remover(paisSelecionado);
                FacesUtils.addInfoMessage("País removido com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método PaisMB.removerPais() para excluir o país "+ pais.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    private void limparForm() {
        pais = new Pais();
        pais.setStatus(true);
        paises = new ArrayList<>();
        removerPaisSelecionado = false;
        listarPaises();
    }

    public String cancelar() {
        return "pais.xhtml" + "?faces-redirect=true";
    }

    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        paisDAO = null;
        pais = null;
        paises = null;
        paisSelecionado = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }
    // ==========================================================================

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public ArrayList<Pais> getPaises() {
        return paises;
    }

    public Pais getPaisSelecionado() {
        return paisSelecionado;
    }

    public void setPaisSelecionado(Pais paisSelecionado) {
        this.paisSelecionado = paisSelecionado;
    }

    public boolean isRemoverPaisSelecionado() {
        return removerPaisSelecionado;
    }

    public void setRemoverPaisSelecionado(boolean removerPaisSelecionado) {
        this.removerPaisSelecionado = removerPaisSelecionado;
    }

}
