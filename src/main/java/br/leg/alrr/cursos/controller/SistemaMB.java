package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Sistema;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.persistence.SistemaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author heliton
 */
@ViewScoped
@Named
public class SistemaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SistemaDAO sistemaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Sistema sistema;

    private ArrayList<Sistema> sistemas;

    private Sistema sistemaSelecionado;

    private boolean removerSistema = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarSistema() {
        try {
            if (sistema.getId() != null) {
                sistemaDAO.atualizar(sistema);
                FacesUtils.addInfoMessageFlashScoped("Sistema atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método SistemaMB.salvarSistema() para atualizar o país "+ sistema.getId()+".");
            } else {
                sistemaDAO.salvar(sistema);
                FacesUtils.addInfoMessageFlashScoped("Sistema salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método SistemaMB.salvarSistema() para salvar o país "+ sistema.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "sistema.xhtml" + "?faces-redirect=true";
    }

    public void listarSistemas() {
        try {
            sistemas = (ArrayList<Sistema>) sistemaDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerSistema() {
        try {
            if (removerSistema) {
                sistemaDAO.remover(sistemaSelecionado);
                FacesUtils.addInfoMessage("Sistema removido com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método SistemaMB.salvarSistema() para atualizar o país "+ sistema.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    private void limparForm(){
        sistema = new Sistema();
        sistema.setStatus(true);
        sistemas = new ArrayList<>();
        listarSistemas();
        removerSistema = false;
    }
    
    public String cancelar() {
        return "sistema.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Sistema getSistema() {
        return sistema;
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    public ArrayList<Sistema> getSistemas() {
        return sistemas;
    }

    public Sistema getSistemaSelecionado() {
        return sistemaSelecionado;
    }

    public void setSistemaSelecionado(Sistema sistemaSelecionado) {
        this.sistemaSelecionado = sistemaSelecionado;
    }

    public boolean isRemoverSistema() {
        return removerSistema;
    }

    public void setRemoverSistema(boolean removerSistema) {
        this.removerSistema = removerSistema;
    }

}
