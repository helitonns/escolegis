package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Disciplina;
import br.leg.alrr.cursos.model.GrupoDisciplina;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.DisciplinaDAO;
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
public class DisciplinaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private DisciplinaDAO disciplinaDAO;

    @EJB
    private GrupoDisciplinaDAO grupoDisciplinaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Disciplina disciplina;

    private ArrayList<Disciplina> disciplinas;
    private ArrayList<GrupoDisciplina> grupoDisciplinas;

    private boolean removerDisciplina = false;
    private Long idGrupoDisciplina = (long) 0;

    //==========================================================================
    @PostConstruct
    public void init() {
        disciplina = new Disciplina();
        disciplina.setStatus(true);
        disciplinas = new ArrayList<>();
        listarGrupoDisciplina();
        listarDisciplina();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarDisciplina() {
        try {
            disciplinas = null;
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            disciplinas = (ArrayList<Disciplina>) disciplinaDAO.listarDisciplinasAtivasPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
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
        disciplina = new Disciplina();
        removerDisciplina = false;
        disciplina.setStatus(true);
        idGrupoDisciplina = 0l;
        removerDisciplina = false;
        listarDisciplina();
    }

    public String salvarDisciplina() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            disciplina.setUnidade(u.getUnidade());
            
            GrupoDisciplina gd = grupoDisciplinaDAO.buscarPorID(idGrupoDisciplina);
            disciplina.setGrupoDisciplina(gd);

            if (disciplina.getId() != null) {
                disciplinaDAO.atualizar(disciplina);
                FacesUtils.addInfoMessageFlashScoped("Disciplina atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método DisciplinaMB.salvarDisciplina() para atualizar a disciplina "+ disciplina.getId()+".");
            } else {
                if (disciplinaDAO.disciplinaNaoCadastrada(disciplina.getNome())) {
                    disciplinaDAO.salvar(disciplina);
                    FacesUtils.addInfoMessageFlashScoped("Disciplina salva com sucesso!");
                    Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método DisciplinaMB.salvarDisciplina() para salvar a disciplina "+ disciplina.getId()+".");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("Disciplina já cadastrada!!!");
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "disciplina.xhtml" + "?faces-redirect=true";
    }

    public void removerDisciplina() {
        try {
            if (removerDisciplina) {
                disciplinaDAO.remover(disciplina);
                FacesUtils.addInfoMessage("Disciplina removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método DisciplinaMB.removerDisciplina() para salvar a disciplina "+ disciplina.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    public String cancelar() {
        return "disciplina.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public boolean isRemoverDisciplina() {
        return removerDisciplina;
    }

    public void setRemoverDisciplina(boolean removerDisciplina) {
        this.removerDisciplina = removerDisciplina;
    }

    public ArrayList<GrupoDisciplina> getGrupoDisciplinas() {
        return grupoDisciplinas;
    }

    public Long getIdGrupoDisciplina() {
        return idGrupoDisciplina;
    }

    public void setIdGrupoDisciplina(Long idGrupoDisciplina) {
        this.idGrupoDisciplina = idGrupoDisciplina;
    }

}
