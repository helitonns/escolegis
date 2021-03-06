package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Horario;
import br.leg.alrr.cursos.persistence.HorarioDAO;
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
public class HorarioMB implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private HorarioDAO horarioDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;
    
    private Horario horario;
    private ArrayList<Horario> horarios;
    
    private Horario horarioSelecionado;

    private boolean removerHorario = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public void limparForm() {
        horario = new Horario();
        horario.setStatus(true);
        horarios = new ArrayList<>();
        removerHorario = false;
        listarHorarios();
    }

    public String salvarHorario() {
        try {
            if (horario.getId() != null) {
                horarioDAO.atualizar(horario);
                FacesUtils.addInfoMessageFlashScoped("Horário atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método HorarioMB.salvarHorario() para atualizar o horário "+ horario.getId()+".");
            } else {
                //verifica se o horário já está cadastrado, se não procede ao cadastramento
                if (horarioDAO.horarioNaoCadastrado(horario.getDescricao())) {
                    horarioDAO.salvar(horario);
                    FacesUtils.addInfoMessageFlashScoped("Horário salvo com sucesso!");
                    Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método HorarioMB.salvarHorario() para salvar o horário "+ horario.getId()+".");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("Horário já cadastrado!!!");
                }
            }
            limparForm();

        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "horario.xhtml" + "?faces-redirect=true";
    }

    private void listarHorarios() {
        try {
            horarios = (ArrayList<Horario>) horarioDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerHorario() {
        try {
            if (removerHorario) {
                horarioDAO.remover(horarioSelecionado);
                FacesUtils.addInfoMessage("Horário removido com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método HorarioMB.removerHorario() para excluir o horário "+ horario.getId()+".");
            }
            removerHorario = false;
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String cancelar() {
        return "horario.xhtml" + "?faces-redirect=true";
    }

    // ==========================================================================
    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public Horario getHorarioSelecionado() {
        return horarioSelecionado;
    }

    public void setHorarioSelecionado(Horario horarioSelecionado) {
        this.horarioSelecionado = horarioSelecionado;
    }

    public boolean isRemoverHorario() {
        return removerHorario;
    }

    public void setRemoverHorario(boolean removerHorario) {
        this.removerHorario = removerHorario;
    }

}
