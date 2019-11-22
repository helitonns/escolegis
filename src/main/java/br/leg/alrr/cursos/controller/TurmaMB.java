package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.BlocoModuloDisciplina;
import br.leg.alrr.cursos.business.DiasDaSemana;
import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Disciplina;
import br.leg.alrr.cursos.model.Horario;
import br.leg.alrr.cursos.model.Matricula;
import br.leg.alrr.cursos.model.Modulo;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.CursoDAO;
import br.leg.alrr.cursos.persistence.FrequenciaDAO;
import br.leg.alrr.cursos.persistence.HorarioDAO;
import br.leg.alrr.cursos.persistence.MatriculaDAO;
import br.leg.alrr.cursos.persistence.ModuloDAO;
import br.leg.alrr.cursos.persistence.TurmaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class TurmaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CursoDAO cursoDAO;

    @EJB
    private ModuloDAO moduloDAO;

    @EJB
    private HorarioDAO horarioDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private FrequenciaDAO frequenciaDAO;

    private ArrayList<Curso> cursos;
    private ArrayList<Horario> horarios;
    private ArrayList<Modulo> modulosDoCurso;
    private ArrayList<Turma> turmas;
    private ArrayList<Aluno> alunos;
    private ArrayList<Aluno> alunosSelecionados;
    private List<Aluno> alunosDaTurma;
    private ArrayList<BlocoModuloDisciplina> blocoModuloDisciplinas;
    private ArrayList<String> diasDaSemanaSelecionados;

    private Turma turma;
    private BlocoModuloDisciplina blocoModuloDisciplina;
    private ArrayList<String> diasDaSemana;
    private Modulo modulo;
    private Disciplina disciplina;
    private Aluno alunoExcluidoDaTurma;

    private long idCurso;
    private long idHorario;
    private boolean montarTurma;
    private boolean cancelaMatricula;
    private boolean removerTurma;

//==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        diasDaSemana.add("SEG");
        diasDaSemana.add("TER");
        diasDaSemana.add("QUA");
        diasDaSemana.add("QUI");
        diasDaSemana.add("SEX");
        diasDaSemana.add("SAB");
        diasDaSemana.add("DOM");
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

    public void selecionarCurso(ValueChangeEvent event) {
        try {
            Long id = Long.parseLong(event.getNewValue().toString());
            listarModulosDoCurso(id);
            montarTurma = false;

            //BUSCA AS TURMAS DO CURSO SELECIONADO
            turmas = null;
            turmas = new ArrayList<>();
            turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadasPorCurso(new Curso(id));

        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarModulosDoCurso(Long id) throws DAOException {
        try {
            Curso c = cursoDAO.buscarPorID(id);
            blocoModuloDisciplinas = null;
            blocoModuloDisciplinas = new ArrayList<>();
            modulosDoCurso = null;
            modulosDoCurso = new ArrayList<>();

            modulosDoCurso = (ArrayList<Modulo>) moduloDAO.listarModulosAtivosPorCurso(c);

            //PEGAR AS DISCIPLINAS DO MÓDULO
            for (Modulo m : modulosDoCurso) {
                m.setDisciplinas(moduloDAO.pegarDisciplinasDoModulo(m));
            }

            for (Modulo m : modulosDoCurso) {
                for (Disciplina d : m.getDisciplinas()) {
                    BlocoModuloDisciplina b = new BlocoModuloDisciplina(m.getId(), m.getNome(), d.getId(), d.getNome());
                    blocoModuloDisciplinas.add(b);
                }
            }
            Collections.sort(blocoModuloDisciplinas);

        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarHorarios() {
        try {
            horarios = null;
            horarios = (ArrayList<Horario>) horarioDAO.listarAtivos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void montarTurma() {
        montarTurma = true;
        trazerAlunosDoCurso();
    }

    private void trazerAlunosDoCurso() {
        try {
            Curso c = new Curso();
            ArrayList<Matricula> matriculas = new ArrayList<>();

            //Pega o curso do módulo
            for (Modulo m : modulosDoCurso) {
                if (m.getId().equals(blocoModuloDisciplina.getIdModulo())) {
                    c = m.getCurso();
                    //salva o módulo para posteriormente ser setado em turma
                    modulo = m;
                    sugerirNomeDaTurma(m, blocoModuloDisciplina.getIdDisciplina());
                    break;
                }
            }

            //Pega as matrículas do curso
            if (c.getId() != null) {
                matriculas = (ArrayList<Matricula>) matriculaDAO.listarMatriculasPorCurso(c.getId());
            }

            //Pega os alunos matriculados no curso
            for (Matricula m : matriculas) {
                alunos.add(m.getAluno());
            }

        } catch (DAOException ex) {
            Logger.getLogger(TurmaMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sugerirNomeDaTurma(Modulo m, Long idDisciplia) {
        for (Disciplina d : m.getDisciplinas()) {
            if (d.getId().equals(idDisciplia)) {
                turma.setNome(d.getNome());
                //salva a disciplina para posteriormente ser setada em turma
                disciplina = d;
                break;
            }
        }
    }

    public String salvarTurma() {
        try {

            turma.setHorario(new Horario(idHorario));
            turma.setDiasDaSemana(prepararInclusaoDiasDaSemana());
            if (turma.getId() != null) {

                if (!turmaDAO.turmaAlcancouSeuTermoFinal(turma)) {
                    //Adiciona-se aos alunos da turma os novos alunos
                    alunosDaTurma.addAll(alunosSelecionados);
                    turma.setAlunos(alunosDaTurma);
                } else {
                    FacesUtils.addWarnMessageFlashScoped("Não é possível matricular novos alunos na turma, pois ela já terminou!");
                }

                turmaDAO.atualizar(turma);
                FacesUtils.addInfoMessageFlashScoped("Turma atulizada com sucesso!");
            } else {
                turma.setAlunos(alunosSelecionados);
                turma.setModulo(modulo);
                turma.setDisciplina(disciplina);
                turmaDAO.salvar(turma);
                FacesUtils.addInfoMessageFlashScoped("Turma salva com sucesso!");
            }
            limparForm();

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "turma.xhtml" + "?faces-redirect=true";
    }

    public void concluirTurma() {
        try {
            turma.setIniciada(false);
            turmaDAO.atualizar(turma);
            limparForm();
            FacesUtils.addInfoMessage("Turma concluída com sucesso!");
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void limparForm() {
        idCurso = 0l;
        montarTurma = false;
        turma = new Turma();
        turma.setStatus(true);
        turma.setIniciada(true);
        modulo = new Modulo();
        disciplina = new Disciplina();
        alunoExcluidoDaTurma = new Aluno();

        blocoModuloDisciplinas = new ArrayList<>();
        horarios = new ArrayList<>();
        alunos = new ArrayList<>();
        alunosSelecionados = new ArrayList<>();
        alunosDaTurma = new ArrayList<>();
        diasDaSemana = new ArrayList<>();
        diasDaSemanaSelecionados = new ArrayList<>();
        turmas = new ArrayList<>();

        listarCurso();
        listarHorarios();
        cancelaMatricula = false;
        removerTurma = false;
    }

    private String prepararInclusaoDiasDaSemana() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < diasDaSemanaSelecionados.size(); i++) {
            DiasDaSemana d = DiasDaSemana.getDiasDaSemanaPelaAbreviacao(diasDaSemanaSelecionados.get(i));
            sb.append(d.getValue());

            if ((i + 1) >= diasDaSemanaSelecionados.size()) {
            } else {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public void prepararEdicaoDeTurma() {
        try {
            montarTurma = true;

            idHorario = turma.getHorario().getId();

            // TRANSFORMANDO A STRING EM UM ARRAY PARA PEGAR CADA DIA DA SEMANA SEPARADAMENTE
            String[] s = turma.getDiasDaSemana().split(",");
            for (String item : s) {
                DiasDaSemana d = DiasDaSemana.fromValue(item.trim());
                diasDaSemanaSelecionados.add(d.getAbreviacao());
            }

            Curso c = turma.getModulo().getCurso();
            ArrayList<Matricula> matriculas = new ArrayList<>();

            //PEGAR OS ALUNOS JÁ CADASTRADOS NA TURMA
            alunosDaTurma = new ArrayList<>();
            alunosDaTurma = turma.getAlunos();

            //Pegar as matrículas do curso
            if (c.getId() != null) {
                matriculas = (ArrayList<Matricula>) matriculaDAO.listarMatriculasPorCurso(c.getId());
            }

            //PEGAR OS ALUNOS MATRICULADOS NO CURSO
            for (Matricula m : matriculas) {
                alunos.add(m.getAluno());
            }

            //REMOVER DA LISTA DOS ALUNOS MATRICULADOS NO CURSO OS ALUNOS JÁ MATRICULADOS NA TURMA
            ArrayList<Aluno> alunosParaRemacao = new ArrayList<>();
            for (Aluno a : alunos) {
                for (Aluno at : alunosDaTurma) {
                    if (a.getId().equals(at.getId())) {
                        alunosParaRemacao.add(a);
                        break;
                    }
                }
            }

            if (alunosParaRemacao.size() > 0) {
                for (Aluno a : alunosParaRemacao) {
                    alunos.remove(a);
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro ao preparar edição de turma.");
        }
    }

    public void removerAlunoDaTurma() {
        try {
            if (cancelaMatricula) {
                for (Aluno a : alunosDaTurma) {
                    if (a.getId().equals(alunoExcluidoDaTurma.getId())) {
                        alunosDaTurma.remove(a);
                        turmaDAO.atualizar(turma);
                        FacesUtils.addInfoMessage("Matricula cancelada com sucesso!");
                        limparForm();
                        break;
                    }
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro ao cancelar matrícula do aluno!");
        }

    }

    public void removerTurma() {
        try {
            if (removerTurma) {
                frequenciaDAO.excluirFrequenciaPorTurma(turma);
                turmaDAO.remover(turma);
                FacesUtils.addInfoMessage("Turma removida com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    public String cancelar() {
        return "turma.xhtml" + "?faces-redirect=true";
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
        moduloDAO = null;
        horarioDAO = null;
        matriculaDAO = null;
        turmaDAO = null;
        frequenciaDAO = null;
        cursos = null;
        horarios = null;
        modulosDoCurso = null;
        turmas = null;
        alunos = null;
        alunosSelecionados = null;
        alunosDaTurma = null;
        blocoModuloDisciplinas = null;
        diasDaSemanaSelecionados = null;
        turma = null;
        blocoModuloDisciplina = null;
        diasDaSemana = null;
        modulo = null;
        disciplina = null;
        alunoExcluidoDaTurma = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }
//==========================================================================

    public ArrayList<Curso> getCursos() {
        return cursos;
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

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public boolean isMontarTurma() {
        return montarTurma;
    }

    public void setMontarTurma(boolean montarTurma) {
        this.montarTurma = montarTurma;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public ArrayList<Aluno> getAlunosSelecionados() {
        return alunosSelecionados;
    }

    public void setAlunosSelecionados(ArrayList<Aluno> alunosSelecionados) {
        this.alunosSelecionados = alunosSelecionados;
    }

    public ArrayList<String> getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(ArrayList<String> diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
    }

    public ArrayList<String> getDiasDaSemanaSelecionados() {
        return diasDaSemanaSelecionados;
    }

    public void setDiasDaSemanaSelecionados(ArrayList<String> diasDaSemanaSelecionados) {
        this.diasDaSemanaSelecionados = diasDaSemanaSelecionados;
    }

    public ArrayList<Turma> getTurmas() {
        return turmas;
    }

    public List<Aluno> getAlunosDaTurma() {
        return alunosDaTurma;
    }

    public long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(long idHorario) {
        this.idHorario = idHorario;
    }

    public Aluno getAlunoExcluidoDaTurma() {
        return alunoExcluidoDaTurma;
    }

    public void setAlunoExcluidoDaTurma(Aluno alunoExcluidoDaTurma) {
        this.alunoExcluidoDaTurma = alunoExcluidoDaTurma;
    }

    public boolean isCancelaMatricula() {
        return cancelaMatricula;
    }

    public void setCancelaMatricula(boolean cancelaMatricula) {
        this.cancelaMatricula = cancelaMatricula;
    }

    public boolean isRemoverTurma() {
        return removerTurma;
    }

    public void setRemoverTurma(boolean removerTurma) {
        this.removerTurma = removerTurma;
    }

}
