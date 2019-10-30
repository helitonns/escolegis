package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Municipio;
import br.leg.alrr.cursos.persistence.MunicipioDAO;
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
public class MunicipioMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MunicipioDAO municipioDAO;

    private Municipio municipio;

    private ArrayList<Municipio> municipios;

    private Municipio municipioSelecionado;

    private boolean removerMunicipio = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarMunicipio() {
        try {
            if (municipio.getId() != null) {
                municipioDAO.atualizar(municipio);
                FacesUtils.addInfoMessageFlashScoped("Município atualizado com sucesso!");
            } else {
                municipioDAO.salvar(municipio);
                FacesUtils.addInfoMessageFlashScoped("Município salvo com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "municipio.xhtml" + "?faces-redirect=true";
    }

    public void listarMunicipios() {
        try {
            municipios = (ArrayList<Municipio>) municipioDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerMunicipio() {
        try {
            if (removerMunicipio) {
                System.out.println("");
                municipioDAO.remover(municipioSelecionado);
                FacesUtils.addInfoMessage("Município removido com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    private void limparForm(){
        municipio = new Municipio();
        municipios = new ArrayList<>();
        listarMunicipios();
        removerMunicipio = false;
    }
    
    public String cancelar() {
        return "municipio.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public Municipio getMunicipioSelecionado() {
        return municipioSelecionado;
    }

    public void setMunicipioSelecionado(Municipio municipioSelecionado) {
        this.municipioSelecionado = municipioSelecionado;
    }

    public boolean isRemoverMunicipio() {
        return removerMunicipio;
    }

    public void setRemoverMunicipio(boolean removerMunicipio) {
        this.removerMunicipio = removerMunicipio;
    }

}
