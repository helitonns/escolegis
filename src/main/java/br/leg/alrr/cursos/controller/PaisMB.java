package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Pais;
import br.leg.alrr.cursos.persistence.PaisDAO;
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
@Named
@ViewScoped
public class PaisMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PaisDAO paisDAO;

    private Pais pais;

    private ArrayList<Pais> paises;

    private Pais paisSelecionado;

    private boolean removerPaisSelecionado = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarPais() {
        try {
            if (pais.getId() != null) {
                paisDAO.atualizar(pais);
                FacesUtils.addInfoMessageFlashScoped("Pais atualizado com sucesso!");
            } else {
                paisDAO.salvar(pais);
                FacesUtils.addInfoMessageFlashScoped("Pais salvo com sucesso!");
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
