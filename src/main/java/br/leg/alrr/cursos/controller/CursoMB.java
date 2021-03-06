package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.CursoDAO;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.persistence.MatriculaDAO;
import br.leg.alrr.cursos.persistence.ModuloDAO;
import br.leg.alrr.cursos.persistence.TurmaDAO;
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
public class CursoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CursoDAO cursoDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private ModuloDAO moduloDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Curso curso;

    private ArrayList<Curso> cursos;

    private boolean removerCurso = false;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarCurso() {
        try {
            cursos = null;
            cursos = new ArrayList<>();
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            cursos = (ArrayList<Curso>) cursoDAO.listarCursosIniciadosPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        curso = new Curso();
        curso.setStatus(true);
        curso.setIniciado(true);
        removerCurso = false;
        listarCurso();
    }

    public String salvarCurso() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            curso.setUnidade(u.getUnidade());

            if (curso.getId() != null) {
                cursoDAO.atualizar(curso);
                FacesUtils.addInfoMessageFlashScoped("Curso atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método CursoMB.salvarCurso() para atualizar o curso "+ curso.getId()+".");
            } else {
                cursoDAO.salvar(curso);
                FacesUtils.addInfoMessageFlashScoped("Curso salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método CursoMB.salvarCurso() para atualizar o curso "+ curso.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "curso.xhtml" + "?faces-redirect=true";
    }

    public String salvarCursoEIncluirModulo() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            curso.setUnidade(u.getUnidade());

            if (curso.getId() != null) {
                cursoDAO.atualizar(curso);
                FacesUtils.addInfoMessage("Curso atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método CursoMB.salvarCurso() para atualizar o curso "+ curso.getId()+".");
            } else {
                cursoDAO.salvar(curso);
                FacesUtils.addInfoMessage("Curso salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método CursoMB.salvarCurso() para atualizar o curso "+ curso.getId()+".");
            }
            FacesUtils.setBean("idCurso", curso.getId());
            return "modulo.xhtml" + "?faces-redirect=true";
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return null;
    }

    public String concluirCurso() {
        try {
            ArrayList<Turma> turmas = new ArrayList<>();
            turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadasPorCurso(curso);

            //finalizando curso
            curso.setIniciado(false);
            cursoDAO.atualizar(curso);

            //finalizando matriculas
            cursoDAO.finalizarMatriculaPorCurso(curso);

            //finalizando turmas
            for (Turma t : turmas) {
                t.setIniciada(false);
                turmaDAO.atualizar(t);
            }
            FacesUtils.addInfoMessageFlashScoped("Curso concluído com sucesso!!!");
            Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método CursoMB.concluirCurso() para concluir o curso "+ curso.getId()+".");
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "curso.xhtml" + "?faces-redirect=true";
    }

    public void removerCurso() {
        try {
            if (removerCurso) {
                if (!moduloDAO.verificarSeCursoTemModulos(curso)) {
                    matriculaDAO.excluirMatriculaPorCurso(curso);
                    cursoDAO.remover(curso);
                    FacesUtils.addInfoMessage("Curso removido com sucesso!");
                    Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método CursoMB.removerCurso() para excluir o curso "+ curso.getId()+".");
                } else {
                    FacesUtils.addWarnMessage("O Curso não pode ser removido, porque há modulos a ele vinculados!");
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    public String cancelar() {
        return "curso.xhtml" + "?faces-redirect=true";
    }

    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        cursoDAO = null;
        turmaDAO = null;
        matriculaDAO = null;
        moduloDAO = null;
        curso = null;
        cursos = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }

    //==========================================================================
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public boolean isRemoverCurso() {
        return removerCurso;
    }

    public void setRemoverCurso(boolean removerCurso) {
        this.removerCurso = removerCurso;
    }

}
