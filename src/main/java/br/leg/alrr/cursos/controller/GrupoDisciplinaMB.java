package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.GrupoDisciplina;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.GrupoDisciplinaDAO;
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
public class GrupoDisciplinaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private GrupoDisciplinaDAO grupoDisciplinaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private GrupoDisciplina grupoDisciplina;

    private ArrayList<GrupoDisciplina> grupoDisciplinas;

    private boolean removerGrupoDisciplina = false;

    //==========================================================================
    @PostConstruct
    public void init() {
        grupoDisciplina = new GrupoDisciplina();
        grupoDisciplina.setStatus(true);
        grupoDisciplinas = new ArrayList<>();
        listarGrupoDisciplina();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarGrupoDisciplina() {
        try {
            grupoDisciplinas = null;
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            grupoDisciplinas = (ArrayList<GrupoDisciplina>) grupoDisciplinaDAO.listarGrupoDisciplinaAtivosPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void limparForm() {
        grupoDisciplina = new GrupoDisciplina();
        removerGrupoDisciplina = false;
        grupoDisciplina.setStatus(true);
        listarGrupoDisciplina();
    }

    public String salvarGrupoDisciplina() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            grupoDisciplina.setUnidade(u.getUnidade());

            if (grupoDisciplina.getId() != null) {
                grupoDisciplinaDAO.atualizar(grupoDisciplina);
                FacesUtils.addInfoMessageFlashScoped("GrupoDisciplina atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método GrupoDisciplinaMB.salvarGrupoDisciplina() para atualizar o grupo "+ grupoDisciplina.getId()+".");
            } else {
                if (grupoDisciplinaDAO.grupoDisciplinaNaoCadastrado(grupoDisciplina.getNome())) {
                    grupoDisciplinaDAO.salvar(grupoDisciplina);
                    FacesUtils.addInfoMessageFlashScoped("GrupoDisciplina salvo com sucesso!");
                    Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método GrupoDisciplinaMB.salvarGrupoDisciplina() para salvar o grupo "+ grupoDisciplina.getId()+".");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("Grupo de Disciplina já cadastrado!!!");
                }

            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "grupo-disciplina.xhtml" + "?faces-redirect=true";
    }

    public void removerGrupoDisciplina() {
        try {
            if (removerGrupoDisciplina) {
                grupoDisciplinaDAO.remover(grupoDisciplina);
                FacesUtils.addInfoMessage("GrupoDisciplina removido com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método GrupoDisciplinaMB.removerGrupoDisciplina() para excluir o grupo "+ grupoDisciplina.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    public String cancelar() {
        return "grupo-disciplina.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public GrupoDisciplina getGrupoDisciplina() {
        return grupoDisciplina;
    }

    public void setGrupoDisciplina(GrupoDisciplina grupoDisciplina) {
        this.grupoDisciplina = grupoDisciplina;
    }

    public ArrayList<GrupoDisciplina> getGrupoDisciplinas() {
        return grupoDisciplinas;
    }

    public boolean isRemoverGrupoDisciplina() {
        return removerGrupoDisciplina;
    }

    public void setRemoverGrupoDisciplina(boolean removerGrupoDisciplina) {
        this.removerGrupoDisciplina = removerGrupoDisciplina;
    }

}
