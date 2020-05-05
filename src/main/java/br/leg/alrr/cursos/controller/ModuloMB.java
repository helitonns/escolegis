package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.BlocoModuloDisciplina;
import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Disciplina;
import br.leg.alrr.cursos.model.GrupoDisciplina;
import br.leg.alrr.cursos.model.Modulo;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.CursoDAO;
import br.leg.alrr.cursos.persistence.DisciplinaDAO;
import br.leg.alrr.cursos.persistence.GrupoDisciplinaDAO;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.persistence.ModuloDAO;
import br.leg.alrr.cursos.persistence.TurmaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;

import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class ModuloMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ModuloDAO moduloDAO;

    @EJB
    private CursoDAO cursoDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private DisciplinaDAO disciplinaDAO;

    @EJB
    private GrupoDisciplinaDAO grupoDdisciplinaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Modulo modulo;

    private ArrayList<Curso> cursos;
    private ArrayList<GrupoDisciplina> grupoDisciplinas;

    private long idGrupoDisciplina;
    private long idCurso;

    private ArrayList<Disciplina> disciplinas;
    private ArrayList<Disciplina> disciplinasSelecionadas;
    private ArrayList<Disciplina> disciplinasAdicionadas;
    private ArrayList<BlocoModuloDisciplina> blocoModuloDisciplinas;
    private BlocoModuloDisciplina blocoModuloDisciplina;

    private Disciplina removerDisciplina;
    private ArrayList<Modulo> modulosDoCurso;

    private boolean removerModulo;

    //==========================================================================
    @PostConstruct
    public void init() {
        //listagem iniciais
        listarCurso();
        listarGrupoDisciplina();

        //instanciando modulo
        modulo = new Modulo();
        modulo.setStatus(true);

        //construindo o PickList
        disciplinas = new ArrayList<>();
        disciplinasSelecionadas = new ArrayList<>();
        disciplinasAdicionadas = new ArrayList<>();
        blocoModuloDisciplinas = new ArrayList<>();

        removerModulo = false;

        //pega e seta o id do curso passado na variável idCurso
        if (FacesUtils.getBean("idCurso") != null) {
            idCurso = (Long) FacesUtils.getBean("idCurso");
            FacesUtils.setBean("idCurso", null);
        }
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarCurso() {
        try {
            cursos = null;
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            cursos = (ArrayList<Curso>) cursoDAO.listarCursosIniciadosPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarGrupoDisciplina() {
        try {
            grupoDisciplinas = null;
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            grupoDisciplinas = (ArrayList<GrupoDisciplina>) grupoDdisciplinaDAO.listarGrupoDisciplinaAtivosPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void selecionarGrupoDisciplina(ValueChangeEvent event) {
        try {
            if (event.getNewValue() != null) {
                idGrupoDisciplina = Long.parseLong(event.getNewValue().toString());
                disciplinas = (ArrayList<Disciplina>) disciplinaDAO.listarDisciplinasAtivasPorGrupoDisciplia(idGrupoDisciplina);
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarModulo() {
        try {

            Curso c = cursoDAO.buscarPorID(idCurso);
            modulo.setCurso(c);
            modulo.setDisciplinas(disciplinasAdicionadas);

            if (modulo.getId() != null) {
                moduloDAO.atualizar(modulo);
                FacesUtils.addInfoMessageFlashScoped("Módulo atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método ModuloMB.salvarModulo() para atualizar o módulo "+ modulo.getId()+".");
            } else {
                moduloDAO.salvar(modulo);
                FacesUtils.addInfoMessageFlashScoped("Módulo salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ModuloMB.salvarModulo() para salvar o módulo "+ modulo.getId()+".");
            }

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "modulo.xhtml" + "?faces-redirect=true";
    }

    public void removerModulo() {
        try {
            if (blocoModuloDisciplina != null) {
                modulo = moduloDAO.buscarPorID(blocoModuloDisciplina.getIdModulo());
                if (!turmaDAO.verificarSeModuloTemTurma(modulo)) {
                    moduloDAO.remover(modulo);
                    FacesUtils.addInfoMessage("Módulo removido com sucesso!");
                    Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método ModuloMB.removerModulo() para excluir o módulo "+ modulo.getId()+".");
                } else {
                    FacesUtils.addWarnMessage("O Módulo não pode ser removido, porque há turmas a ele vinculadas!");
                }
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void limparForm() {
        idGrupoDisciplina = 0l;
        idCurso = 0l;
        disciplinasAdicionadas = new ArrayList<>();
        disciplinasSelecionadas = new ArrayList<>();
        blocoModuloDisciplinas = new ArrayList<>();
        modulo = new Modulo();
        listarCurso();
        removerModulo = false;
    }

    public String cancelar() {
        return "modulo.xhtml" + "?faces-redirect=true";
    }

    public void adicionarDisciplina() {
        for (Disciplina ds : disciplinasSelecionadas) {
            disciplinasAdicionadas.add(ds);
        }
    }

    public void removerDisciplina() {
        try {
            for (Disciplina da : disciplinasAdicionadas) {
                if (da == removerDisciplina) {
                    disciplinasAdicionadas.remove(da);
                }
            }
        } catch (Exception e) {
        }
    }

    private void listarModulosDoCurso(Long id) throws DAOException {
        try {
            Curso c = cursoDAO.buscarPorID(id);

            modulosDoCurso = null;
            modulosDoCurso = new ArrayList<>();
            blocoModuloDisciplinas = new ArrayList<>();

            modulosDoCurso = (ArrayList<Modulo>) moduloDAO.listarModulosAtivosPorCurso(c);

            //PEGAR AS DISCIPLINAS DO MÓDULO
            for (Modulo m : modulosDoCurso) {
                m.setDisciplinas(moduloDAO.pegarDisciplinasDoModulo(m));
            }

            for (Modulo m : modulosDoCurso) {
                for (Disciplina d : m.getDisciplinas()) {
                    blocoModuloDisciplinas.add(new BlocoModuloDisciplina(m.getId(), m.getNome(), d.getId(), d.getNome()));
                }
            }
            Collections.sort(blocoModuloDisciplinas);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void selecionarCurso(ValueChangeEvent event) {
        try {
            Long id = Long.parseLong(event.getNewValue().toString());
            listarModulosDoCurso(id);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void setarModulo() {
        if (blocoModuloDisciplina != null) {
            try {
                modulo = moduloDAO.buscarPorID(blocoModuloDisciplina.getIdModulo());
                for (Disciplina d : modulo.getDisciplinas()) {
                    disciplinasAdicionadas.add(d);
                }
            } catch (DAOException e) {
                FacesUtils.addErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        moduloDAO = null;
        cursoDAO = null;
        turmaDAO = null;
        disciplinaDAO = null;
        grupoDdisciplinaDAO = null;
        modulo = null;
        cursos = null;
        grupoDisciplinas = null;
        disciplinas = null;
        disciplinasSelecionadas = null;
        disciplinasAdicionadas = null;
        blocoModuloDisciplinas = null;
        blocoModuloDisciplina = null;
        removerDisciplina = null;
        modulosDoCurso = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }
//==========================================================================

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public ArrayList<GrupoDisciplina> getGrupoDisciplinas() {
        return grupoDisciplinas;
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public long getIdGrupoDisciplina() {
        return idGrupoDisciplina;
    }

    public void setIdGrupoDisciplina(long idGrupoDisciplina) {
        this.idGrupoDisciplina = idGrupoDisciplina;
    }

    public ArrayList<Disciplina> getDisciplinasSelecionadas() {
        return disciplinasSelecionadas;
    }

    public void setDisciplinasSelecionadas(ArrayList<Disciplina> disciplinasSelecionadas) {
        this.disciplinasSelecionadas = disciplinasSelecionadas;
    }

    public ArrayList<Disciplina> getDisciplinasAdicionadas() {
        return disciplinasAdicionadas;
    }

    public void setDisciplinasAdicionadas(ArrayList<Disciplina> disciplinasAdicionadas) {
        this.disciplinasAdicionadas = disciplinasAdicionadas;
    }

    public Disciplina getRemoverDisciplina() {
        return removerDisciplina;
    }

    public void setRemoverDisciplina(Disciplina removerDisciplina) {
        this.removerDisciplina = removerDisciplina;
    }

    public long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(long idCurso) {
        this.idCurso = idCurso;
    }

    public ArrayList<Modulo> getModulosDoCurso() {
        return modulosDoCurso;
    }

    public void setModulosDoCurso(ArrayList<Modulo> modulosDoCurso) {
        this.modulosDoCurso = modulosDoCurso;
    }

    public ArrayList<BlocoModuloDisciplina> getBlocoModuloDisciplinas() {
        return blocoModuloDisciplinas;
    }

    public void setBlocoModuloDisciplinas(ArrayList<BlocoModuloDisciplina> blocoModuloDisciplinas) {
        this.blocoModuloDisciplinas = blocoModuloDisciplinas;
    }

    public BlocoModuloDisciplina getBlocoModuloDisciplina() {
        return blocoModuloDisciplina;
    }

    public void setBlocoModuloDisciplina(BlocoModuloDisciplina blocoModuloDisciplina) {
        this.blocoModuloDisciplina = blocoModuloDisciplina;
    }

    public boolean isRemoverModulo() {
        return removerModulo;
    }

    public void setRemoverModulo(boolean removerModulo) {
        this.removerModulo = removerModulo;
    }

}
