package br.leg.alrr.cursos.controller.legado;

import br.leg.alrr.cursos.model.Horario;
import br.leg.alrr.cursos.model.legado.Atividade;
import br.leg.alrr.cursos.model.legado.AtividadePessoa;
import br.leg.alrr.cursos.model.legado.Dias;
import br.leg.alrr.cursos.model.legado.Periodo;
import br.leg.alrr.cursos.model.legado.Pessoa;
import br.leg.alrr.cursos.persistence.HorarioDAO;
import br.leg.alrr.cursos.persistence.legado.AtividadeDAO;
import br.leg.alrr.cursos.persistence.legado.AtividadePessoaDAO;
import br.leg.alrr.cursos.persistence.legado.DiasDAO;
import br.leg.alrr.cursos.persistence.legado.PeriodoDAO;
import br.leg.alrr.cursos.persistence.legado.PessoaDAO;
import br.leg.alrr.cursos.util.DAOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author heliton
 */
@ManagedBean
@ViewScoped
public class MatriculaLegadaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AtividadePessoaDAO atividadePessoaDAO;

    @EJB
    private AtividadeDAO atividadeDAO;

    @EJB
    private DiasDAO diasDAO;

    @EJB
    private HorarioDAO horarioDAO;

    @EJB
    private PeriodoDAO periodoDAO;

    @EJB
    private PessoaDAO pessoaDAO;

    private ArrayList<Atividade> atividades;
    private ArrayList<Dias> dias;
    private ArrayList<Horario> horarios;
    private ArrayList<Periodo> periodos;
    private ArrayList<Pessoa> pessoas;
    private ArrayList<Pessoa> pessoasJaMatriculadas;

    private Pessoa pessoa;

    private Long idAtividade;
    private Long idDiasDaSemana;
    private Long idHorario;
    private Long idPeriodo;
    private boolean mostrarAlunosJaMatriculados;

//==============================================================================
    @PostConstruct
    public void init() {
        listarAtividades();
        listarDias();
        listarHorarios();
        listarPeriodos();

        limparForm();
    }

    private void listarAtividades() {
        try {
            atividades = (ArrayList<Atividade>) atividadeDAO.listarTodos();
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    private void listarDias() {
        try {
            dias = (ArrayList<Dias>) diasDAO.listarTodos();
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    private void listarHorarios() {
        try {
            horarios = (ArrayList<Horario>) horarioDAO.listarTodos();
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    private void listarPeriodos() {
        try {
            periodos = (ArrayList<Periodo>) periodoDAO.listarTodosAtivos();
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    public void prepararMatricula() {
        pesquisarAluno();
        listarAlunosJaMatriculadosNoCurso();
    }

    public void pesquisarAluno() {
        try {
            pessoas = new ArrayList<>();

            if (pessoa.getCpf() != null && !pessoa.getCpf().isEmpty()) {
                pessoas = (ArrayList<Pessoa>) pessoaDAO.pesquisarPorCPF(pessoa.getCpf());
            } else if (pessoa.getNome() != null && !pessoa.getNome().isEmpty()) {
                pessoas = (ArrayList<Pessoa>) pessoaDAO.pesquisarPorNome(pessoa.getNome());
            } else {
//                FacesMessages.warning("Indique o CPF ou o nome do aluno!");
            }
        } catch (DAOException e) {
            pessoa = new Pessoa();
//            FacesMessages.error(e.getMessage());
        }
    }

    private void listarAlunosJaMatriculadosNoCurso() {
        try {
            pessoasJaMatriculadas = (ArrayList<Pessoa>) pessoaDAO.listarAlunosJaMatriculadosNoCurso(idAtividade, idPeriodo, idDiasDaSemana, idHorario);
            mostrarAlunosJaMatriculados = true;
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage() + ": " + e.getCause());
        }
    }

    public void matricular() {
        try {
            //verifica se todos os parâmetros foram selecionados
            if (idAtividade == 0l || idDiasDaSemana == 0l || idHorario == 0l || idPeriodo == 0l) {
//                FacesMessages.warning("Selecione todos os parâmetros para realizar a matrícula!");
            } else {
                //perccore a lista de período para verficar se a matricula do aluno será feita em um curso que ainda não acabou
                for (Periodo p : periodos) {
                    if (Objects.equals(p.getId(), idPeriodo)) {
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.add(GregorianCalendar.DAY_OF_MONTH, -1);
                        //se o curso não acabou então faça a matricula
                        if (!gc.getTime().after(p.getDataDeTermino())) {
                            AtividadePessoa ap = new AtividadePessoa();
                            ap.setIdAtividade(idAtividade);
                            ap.setIdDias(idDiasDaSemana);
                            ap.setIdHorario(idHorario);
                            ap.setIdPeriodo(idPeriodo);
                            ap.setIdPessoa(pessoa.getId());
                            atividadePessoaDAO.salvar(ap);
//                            FacesMessages.info("Aluno matriculado com sucesso!!!");
                            pessoa = new Pessoa();
                            pessoas = new ArrayList<>();
                            pessoasJaMatriculadas = new ArrayList<>();
                            mostrarAlunosJaMatriculados = false;
                            break;
                        } else {
//                            FacesMessages.warning("Não é mais possível matricular aluno neste curso, pois ele já terminou!");
                        }
                    }
                }
            }
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    private void limparForm() {
        idAtividade = 0l;
        idDiasDaSemana = 0l;
        idHorario = 0l;
        idPeriodo = 0l;
        mostrarAlunosJaMatriculados = false;

        pessoa = new Pessoa();
        pessoas = new ArrayList<>();
        pessoasJaMatriculadas = new ArrayList<>();
    }

//==============================================================================
    public ArrayList<Atividade> getAtividades() {
        return atividades;
    }

    public ArrayList<Dias> getDias() {
        return dias;
    }

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public ArrayList<Periodo> getPeriodos() {
        return periodos;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public ArrayList<Pessoa> getPessoas() {
        return pessoas;
    }

    public Long getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Long idAtividade) {
        this.idAtividade = idAtividade;
    }

    public Long getIdDiasDaSemana() {
        return idDiasDaSemana;
    }

    public void setIdDiasDaSemana(Long idDiasDaSemana) {
        this.idDiasDaSemana = idDiasDaSemana;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public boolean isMostrarAlunosJaMatriculados() {
        return mostrarAlunosJaMatriculados;
    }

    public ArrayList<Pessoa> getPessoasJaMatriculadas() {
        return pessoasJaMatriculadas;
    }

}
