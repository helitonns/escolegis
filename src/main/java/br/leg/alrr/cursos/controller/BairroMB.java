package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Bairro;
import br.leg.alrr.cursos.model.Municipio;
import br.leg.alrr.cursos.persistence.BairroDAO;
import br.leg.alrr.cursos.persistence.MunicipioDAO;
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
public class BairroMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BairroDAO bairroDAO;

    @EJB
    private MunicipioDAO municipioDAO;

    private Bairro bairro;
    private Municipio municipio;

    private ArrayList<Bairro> bairros;
    private ArrayList<Municipio> municipios;

    private boolean removerBairro = false;

    //==========================================================================
    @PostConstruct
    public void init() {
        listarMunicipio();
        limparForm();
    }

    private void listarBairro() {
        try {
            bairros = (ArrayList<Bairro>) bairroDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarMunicipio() {
        try {
            municipios = (ArrayList<Municipio>) municipioDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarBairro() {
        try {
            bairro.setMunicipio(municipio);

            if (bairro.getId() != null) {
                bairroDAO.atualizar(bairro);
                FacesUtils.addInfoMessageFlashScoped("Bairro atualizado com sucesso!");
            } else {
                bairroDAO.salvar(bairro);
                FacesUtils.addInfoMessageFlashScoped("Bairro salvo com sucesso!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "bairro.xhtml" + "?faces-redirect=true";
    }

    public void removerBairro() {
        try {
            if (removerBairro) {
                bairroDAO.remover(bairro);
                FacesUtils.addInfoMessage("Bairro removido com sucesso!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        municipio = new Municipio();
        bairro = new Bairro();
        bairros = new ArrayList<>();
        removerBairro = false;
        listarBairro();
    }

    public String cancelar() {
        return "bairro.xhtml" + "?faces-redirect=true";
    }

    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        bairroDAO = null;
        municipioDAO = null;
        bairro = null;
        municipio = null;
        bairros = null;
        municipios = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }

    //==========================================================================
    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public ArrayList<Bairro> getBairros() {
        return bairros;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public boolean isRemoverBairro() {
        return removerBairro;
    }

    public void setRemoverBairro(boolean removerBairro) {
        this.removerBairro = removerBairro;
    }

}
