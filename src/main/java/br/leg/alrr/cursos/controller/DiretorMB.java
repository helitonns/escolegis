package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Diretor;
import br.leg.alrr.cursos.persistence.DiretorDAO;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
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
public class DiretorMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private DiretorDAO diretorDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Diretor diretor;

    private ArrayList<Diretor> diretores;

    private Diretor diretorSelecionado;

    private boolean removerDiretor = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarDiretor() {
        try {
            if (diretor.getId() != null) {
                diretorDAO.atualizar(diretor);
                FacesUtils.addInfoMessageFlashScoped("Diretor atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método DiretorMB.salvarDiretor() para atualizar o(a) diretor(a) "+ diretor.getId()+".");
            } else {
                diretorDAO.salvar(diretor);
                FacesUtils.addInfoMessageFlashScoped("Diretor salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método DiretorMB.salvarDiretor() para salvar o(a) diretor(a) "+ diretor.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "diretor.xhtml" + "?faces-redirect=true";
    }

    public void listarDiretores() {
        try {
            diretores = (ArrayList<Diretor>) diretorDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerDiretor() {
        try {
            if (removerDiretor) {
                diretorDAO.remover(diretorSelecionado);
                FacesUtils.addInfoMessage("Diretor removido com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método DiretorMB.removerDiretor() para excluir o(a) diretor(a) "+ diretor.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    private void limparForm(){
        diretor = new Diretor();
        diretor.setStatus(true);
        diretores = new ArrayList<>();
        removerDiretor = false;
        listarDiretores();
    }
    
    public String cancelar() {
        return "diretor.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Diretor getDiretor() {
        return diretor;
    }

    public void setDiretor(Diretor diretor) {
        this.diretor = diretor;
    }

    public ArrayList<Diretor> getDiretores() {
        return diretores;
    }

    public Diretor getDiretorSelecionado() {
        return diretorSelecionado;
    }

    public void setDiretorSelecionado(Diretor diretorSelecionado) {
        this.diretorSelecionado = diretorSelecionado;
    }

    public boolean isRemoverDiretor() {
        return removerDiretor;
    }

    public void setRemoverDiretor(boolean removerDiretor) {
        this.removerDiretor = removerDiretor;
    }

}
