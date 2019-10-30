package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Sistema;
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

    private Sistema sistema;

    private ArrayList<Sistema> sistemas;

    private Sistema sistemaSelecionado;

    private boolean removerSistema = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarSistema() {
        try {
            if (sistema.getId() != null) {
                sistemaDAO.atualizar(sistema);
                FacesUtils.addInfoMessageFlashScoped("Sistema atualizado com sucesso!");
            } else {
                sistemaDAO.salvar(sistema);
                FacesUtils.addInfoMessageFlashScoped("Sistema salvo com sucesso!");
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
