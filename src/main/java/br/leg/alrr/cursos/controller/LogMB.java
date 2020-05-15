package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.LogSistema;
import br.leg.alrr.cursos.model.Usuario;
import br.leg.alrr.cursos.persistence.AutorizacaoDAO;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
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
public class LogMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private LogSistemaDAO logSistemaDAO;

    @EJB
    private AutorizacaoDAO autorizacaoDAO;

    private ArrayList<Usuario> usuarios;
    private ArrayList<LogSistema> logs;

    private LocalDate data1ParaPesquisa;
    private LocalDate data2ParaPesquisa;

    private Long idUsuario;
    private String tipoDeAcao;
    private TipoAcao tipoAcao = TipoAcao.ENTRAR;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        listarUsuariosDoSistema();
        definirAsDatasDaSemana();

    }

    private void listarUsuariosDoSistema() {
        try {
            String[] s = FacesUtils.getURL().split("/");
            usuarios = (ArrayList<Usuario>) autorizacaoDAO.listarUsuariosQueTemPermissaoNoSistema(s[1]);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void definirAsDatasDaSemana() {
        LocalDate hoje = LocalDate.now();
        data1ParaPesquisa = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        data2ParaPesquisa = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
    }

    public void listarLogs() {
        try {
            logs = null;
            logs = new ArrayList<>();
            
            LocalDateTime ldt1 = LocalDateTime.of(data1ParaPesquisa, LocalTime.MIN);
            LocalDateTime ldt2 = LocalDateTime.of(data2ParaPesquisa, LocalTime.MAX);

            if (idUsuario != 0l && !tipoDeAcao.equals("0")) {
                logs = (ArrayList<LogSistema>) logSistemaDAO.listarLogsPorParametro(TipoAcao.fromValue(tipoDeAcao), new Usuario(idUsuario), ldt1, ldt2);
            } 
            else if (idUsuario == 0l && tipoDeAcao.equals("0")) {
                logs = (ArrayList<LogSistema>) logSistemaDAO.listarLogsPorParametro(ldt1, ldt2);
            } 
            else if (idUsuario == 0) {
                logs = (ArrayList<LogSistema>) logSistemaDAO.listarLogsPorParametro(TipoAcao.fromValue(tipoDeAcao), ldt1, ldt2);
            } 
            else if (tipoDeAcao.equals("0")) {
                logs = (ArrayList<LogSistema>) logSistemaDAO.listarLogsPorParametro(new Usuario(idUsuario), ldt1, ldt2);
            }
            
            idUsuario = 0l;
            tipoDeAcao = "0";

        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparForm() {
        idUsuario = 0l;
        usuarios = new ArrayList<>();
    }

    public String cancelar() {
        return "estatistica.xhtml" + "?faces-redirect=true";
    }

//    private void contarAcessosDoDia() {
//        try {
//            quantidadeDeAcesso = acessoDAO.contarAcessosPorData(LocalDate.now());
//        } catch (DAOException e) {
//            FacesUtils.addErrorMessage(e.getMessage());
//        }
//    }
//
//    public void listarAcessosDoDia() {
//        try {
//            acessos = null;
//            acessos = new ArrayList<>();
//            horasQuantidades = new ArrayList<>();
//            acessos = (ArrayList<Acesso>) acessoDAO.listarAcessosPorData(dataParaPesquisa);
//        } catch (DAOException e) {
//            FacesUtils.addErrorMessage(e.getMessage());
//        }
//    }
//    private void listarAcessosDoPeriodo() {
//        try {
//            acessos = null;
//            acessos = new ArrayList<>();
//            acessos = (ArrayList<Acesso>) acessoDAO.listarAcessosPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa);
//        } catch (DAOException e) {
//            FacesUtils.addErrorMessage(e.getMessage());
//        }
//    }
    //==========================================================================
    //==========================================================================
    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        usuarios = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }

    //==========================================================================
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getData1ParaPesquisa() {
        return data1ParaPesquisa;
    }

    public void setData1ParaPesquisa(LocalDate data1ParaPesquisa) {
        this.data1ParaPesquisa = data1ParaPesquisa;
    }

    public LocalDate getData2ParaPesquisa() {
        return data2ParaPesquisa;
    }

    public void setData2ParaPesquisa(LocalDate data2ParaPesquisa) {
        this.data2ParaPesquisa = data2ParaPesquisa;
    }

    public TipoAcao getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(TipoAcao tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public String getTipoDeAcao() {
        return tipoDeAcao;
    }

    public void setTipoDeAcao(String tipoDeAcao) {
        this.tipoDeAcao = tipoDeAcao;
    }

    public ArrayList<LogSistema> getLogs() {
        return logs;
    }
    
    

}
