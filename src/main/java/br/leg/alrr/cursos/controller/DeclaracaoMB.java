package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Diretor;
import br.leg.alrr.cursos.model.Matricula;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.persistence.AlunoDAO;
import br.leg.alrr.cursos.persistence.CursoDAO;
import br.leg.alrr.cursos.persistence.DiretorDAO;
import br.leg.alrr.cursos.persistence.FrequenciaDAO;
import br.leg.alrr.cursos.persistence.MatriculaDAO;
import br.leg.alrr.cursos.persistence.TurmaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import br.leg.alrr.cursos.util.GeneratorPDF;
import java.io.IOException;
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
public class DeclaracaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AlunoDAO alunoDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private CursoDAO cursoDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private FrequenciaDAO frequenciaDAO;

    @EJB
    private DiretorDAO diretorDAO;

    private ArrayList<Aluno> alunos;
    private ArrayList<Turma> turmas;
    private ArrayList<Matricula> matriculas;

    private Aluno aluno;
    private Turma turma;
    private Curso curso;
    private Matricula matricula;
    private GeneratorPDF gerarPdf;

    private int porcentagemDePresenca;
    private boolean exibirTabelaDeTurmas;
    private boolean exibirTabelaDeCurso;

//==============================================================================
    @PostConstruct
    public void init() {
        limparForm();
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
            FacesUtils.addErrorMessage(e.getMessage());
        }
        aluno = new Aluno();
    }

    public void prepararDeclaracaoDeMatricula() {
        try {
            exibirTabelaDeTurmas = true;
            exibirTabelaDeCurso = false;
            turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadasPorAluno(aluno);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void prepararDeclaracaoDeConclusao() {
        try {
            exibirTabelaDeCurso = true;
            exibirTabelaDeTurmas = false;

            //traz os cursos concluidos
            matriculas = (ArrayList<Matricula>) matriculaDAO.listarMatriculasConcluidasPorAluno(aluno);

            //guarda os cursos em que o aluno não passou
            ArrayList<Matricula> cursosEmQueOAlunoNaoPassou = new ArrayList<>();

            //para cada curso deve-se trazer as turmas
            for (Matricula m : matriculas) {

                //passa-se o curso, contido em matrícula, para trazer as respectivas turmas
                ArrayList<Turma> tt = (ArrayList<Turma>) turmaDAO.listarTurmasConcluidasPorCurso(m.getCurso());

                //verificar em cada turma se o aluno passou
                for (Turma t : tt) {

                    //conta a quantidade de aulas que houve da turma
                    Long quantidadeDeAulas = frequenciaDAO.contarFrequenciaDaTurma(t);

                    //conta a quantidade de aulas que o aluno assistiu
                    Long quantidadeDePresencas = frequenciaDAO.contarFrequenciaDoAlunoPorTurma(aluno, t);

                    //calcula se o aluno assistiu a porcentagem mínima para passar na disciplina
                    if (quantidadeDeAulas > 0) {
                        if (((quantidadeDePresencas * 100) / quantidadeDeAulas) < porcentagemDePresenca) {

                            //neste caso o aluno não passou na disciplina. Ou seja, ele não passou no curso.
                            cursosEmQueOAlunoNaoPassou.add(m);
                            break;
                        }
                    }

                }
            }

            //remover das matriculas os cursos nos quais o aluno não passou
            matriculas.removeAll(cursosEmQueOAlunoNaoPassou);

        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void imprimirDeclaracaoDeMatricula() {
        try {
            gerarPdf = new GeneratorPDF();
            aluno.setTurma(turma);

            ArrayList<Diretor> diretores = (ArrayList<Diretor>) diretorDAO.listarDiretoresAtivos();
            aluno.setDiretor(diretores.get(0));

            gerarPdf.declaracaoMatricula(aluno);
        } catch (IOException | DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void imprimirDeclaracaoDeConclusao() {
        try {
            gerarPdf = new GeneratorPDF();
            int cargaHorariaCurso = cursoDAO.pesquisarCargaHorariaDoCurso(curso);

            ArrayList<Diretor> diretores = (ArrayList<Diretor>) diretorDAO.listarDiretoresAtivos();

            curso.setCargaHoraria(cargaHorariaCurso);
            aluno.setCurso(curso);
            aluno.setDiretor(diretores.get(0));

            gerarPdf.declaracaoConclusaoCurso(aluno);
        } catch (IOException | DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void imprimirDeclaracaoDeConclusaoDeTodosOsCursos() {
        try {
            gerarPdf = new GeneratorPDF();

            //setando diretor e carga horaria
            ArrayList<Diretor> diretores = (ArrayList<Diretor>) diretorDAO.listarDiretoresAtivos();
            for (Matricula m : matriculas) {
                m.getAluno().setDiretor(diretores.get(0));
                int cargaHorariaCurso = cursoDAO.pesquisarCargaHorariaDoCurso(m.getCurso());
                m.getCurso().setCargaHoraria(cargaHorariaCurso);
            }

            gerarPdf.declaracaoTodosCursosConcluidos(matriculas);
        } catch (IOException | DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        porcentagemDePresenca = 75;
        exibirTabelaDeTurmas = false;
        exibirTabelaDeCurso = false;

        aluno = new Aluno();
        turma = new Turma();
        matricula = new Matricula();

        alunos = new ArrayList<>();
        turmas = new ArrayList<>();
        matriculas = new ArrayList<>();
    }

    public String cancelar() {
        return "declaracao.xhtml" + "?faces-redirect=true";
    }

//==============================================================================
    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public ArrayList<Turma> getTurmas() {
        return turmas;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public boolean isExibirTabelaDeTurmas() {
        return exibirTabelaDeTurmas;
    }

    public int getPorcentagemDePresenca() {
        return porcentagemDePresenca;
    }

    public void setPorcentagemDePresenca(int porcentagemDePresenca) {
        this.porcentagemDePresenca = porcentagemDePresenca;
    }

    public ArrayList<Matricula> getMatriculas() {
        return matriculas;
    }

    public boolean isExibirTabelaDeCurso() {
        return exibirTabelaDeCurso;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

}
