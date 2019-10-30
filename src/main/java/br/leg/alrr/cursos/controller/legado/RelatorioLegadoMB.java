package br.leg.alrr.cursos.controller.legado;

import br.leg.alrr.cursos.model.Horario;
import br.leg.alrr.cursos.model.Matricula;
import br.leg.alrr.cursos.model.legado.Atividade;
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
import br.leg.alrr.cursos.util.Relatorio;
import java.io.Serializable;
import java.util.ArrayList;

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
public class RelatorioLegadoMB implements Serializable {

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


    private Long idAtividade;
    private Long idDiasDaSemana;
    private Long idHorario;
    private Long idPeriodo;

//==============================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        listarAtividades();
        listarDias();
        listarHorarios();
        listarPeriodos();

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
            periodos = (ArrayList<Periodo>) periodoDAO.listarTodos();
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }


    public void pesquisarALunosMatriculados() {
        try {
            pessoas = (ArrayList<Pessoa>) pessoaDAO.pesquisarALunosMatriculados(idAtividade, idPeriodo, idDiasDaSemana, idHorario);
            System.out.println(pessoas);
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage() + ": " + e.getCause());
        }
    }

   
    private void limparForm() {
        idAtividade = 0l;
        idDiasDaSemana = 0l;
        idHorario = 0l;
        idPeriodo = 0l;
        pessoas = new ArrayList<>();
    }
    
    public String exportarPDFPessoas() {
         try {
            Relatorio<Pessoa> report = new Relatorio<>();

            if (pessoas == null) {
                try {
                    if (pessoas.isEmpty()) {
//                        FacesMessages.error("Não há registros!");
                    } else {
                        report.getRelatorioLegado(pessoas);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                report.getRelatorioLegado(pessoas);
            }
        } catch (Exception e) {
//            FacesMessages.error("Erro ao gerar relatório: "+e.getMessage());
        }

        return "relatorio-legado";
    }
    
    
    public String cancelar(){
        limparForm();
        return "relatorio.xhtml" + "?faces-redirect=true";
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
}
