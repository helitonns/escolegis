package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Matricula;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.AlunoDAO;
import br.leg.alrr.cursos.persistence.CursoDAO;
import br.leg.alrr.cursos.persistence.MatriculaDAO;
import br.leg.alrr.cursos.persistence.TurmaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
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
public class MatriculaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CursoDAO cursoDAO;

    @EJB
    private AlunoDAO alunoDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private TurmaDAO turmaDAO;

    private ArrayList<Curso> cursos;
    private ArrayList<Aluno> alunos;
    private ArrayList<Matricula> alunosJaMatriculados;

    private Curso curso;
    private Aluno aluno;
    private Matricula matricula;

    private boolean mostrarAlunosJaMatriculados;
    private boolean cancelaMatricula;

//==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        // VERIFICA SE HÁ ALGUM ALUNO NA SESSÃO PARA SER MATRICULADO
        try {
            if (FacesUtils.getBean("aluno") != null) {
                alunos.add((Aluno) FacesUtils.getBean("aluno"));
                FacesUtils.removeBean("aluno");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro ao tentar matricular aluno.");
        }
    }

    private void listarCursosAtivos() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            cursos = (ArrayList<Curso>) cursoDAO.listarCursosIniciadosPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void pesquisarAluno() {
        try {
            alunos = new ArrayList<>();

            if (aluno.getCpf() != null && !aluno.getCpf().isEmpty()) {
                Aluno a = alunoDAO.pesquisarPorCPF(aluno.getCpf());
                alunos.add(a);
            } else if (aluno.getNome() != null && !aluno.getNome().isEmpty()) {
                alunos = (ArrayList<Aluno>) alunoDAO.pesquisarPorNome(aluno.getNome());
            } else {
                FacesUtils.addWarnMessage("Indique o CPF ou o nome do aluno!");
            }
        } catch (DAOException e) {
            aluno = new Aluno();
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String matricular() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            matricula.setUnidade(u.getUnidade());

            Long idCurso = curso.getId();
            Long idALuno = aluno.getId();

            matricula.setAluno(aluno);
            matricula.setCurso(curso);

            //verifica se o curso já alcançou seu termo final
            //enquanto ele não tiver alcançada realiazar as funções de matricular e editar matricula
            if (!cursoDAO.cursoAlcancouSeuTermoFinal(curso)) {
                //atualizar
                if (matricula.getId() != null) {
                    matriculaDAO.atualizar(matricula);
                    FacesUtils.addInfoMessageFlashScoped("Matrícula atualizada com sucesso!!!");
                } //salvar
                else {
                    GregorianCalendar gc = new GregorianCalendar();
                    matricula.setDataMatricula(gc.getTime());
                    matricula.setStatus(true);

                    boolean b = matriculaDAO.podeMatricular(idCurso, idALuno);

                    if (b) {
                        matriculaDAO.salvar(matricula);
                        FacesUtils.addInfoMessageFlashScoped("Matrícula salva com sucesso!!!");
                    } else {
                        FacesUtils.addWarnMessageFlashScoped("O aluno já está matriculado neste curso!");
                    }
                }
            } else {
                FacesUtils.addWarnMessageFlashScoped("Não é mais possível matricular aluno neste curso, pois ele já terminou!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage() + ": " + e.getCause().toString());
        }
        return "matricula.xhtml" + "?faces-redirect=true";
    }

    public void cancelarMatricula() {
        try {
            if (cancelaMatricula) {

                //Listar as turmas do curso para cancelar a matricula na respectiva turma
                ArrayList<Turma> turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadasPorCurso(matricula.getCurso());
                for (Turma t : turmas) {
                    for (Aluno a : t.getAlunos()) {
                        if (a.getId().equals(matricula.getAluno().getId())) {
                            t.getAlunos().remove(a);
                            turmaDAO.atualizar(t);
                            break;
                        }
                    }
                }

                //cancelar matricula
                matriculaDAO.remover(matricula);
                FacesUtils.addInfoMessage("Matrícula cancelada com sucesso!!!");
                limparForm();
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro ao cacelar matrícula!!!");
        }

    }

    private void limparForm() {
        cursos = new ArrayList<>();
        alunos = new ArrayList<>();

        curso = new Curso();
        aluno = new Aluno();
        matricula = new Matricula();
        matricula.setStatus(true);

        listarCursosAtivos();

        mostrarAlunosJaMatriculados = false;
        cancelaMatricula = false;
    }

    public void selecionarCurso(ValueChangeEvent event) {
        try {
            Curso c = (Curso) event.getNewValue();
            alunosJaMatriculados = (ArrayList<Matricula>) matriculaDAO.listarMatriculasPorCurso(c.getId());
            mostrarAlunosJaMatriculados = true;
        } catch (DAOException | NumberFormatException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String cancelar() {
        return "matricula.xhtml" + "?faces-redirect=true";
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

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public ArrayList<Matricula> getAlunosJaMatriculados() {
        return alunosJaMatriculados;
    }

    public boolean isMostrarAlunosJaMatriculados() {
        return mostrarAlunosJaMatriculados;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public boolean isCancelaMatricula() {
        return cancelaMatricula;
    }

    public void setCancelaMatricula(boolean cancelaMatricula) {
        this.cancelaMatricula = cancelaMatricula;
    }

}
