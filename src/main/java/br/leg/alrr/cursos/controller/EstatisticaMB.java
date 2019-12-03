package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Acesso;
import br.leg.alrr.cursos.model.Usuario;
import br.leg.alrr.cursos.persistence.AcessoDAO;
import br.leg.alrr.cursos.persistence.AutorizacaoDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class EstatisticaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AcessoDAO acessoDAO;

    @EJB
    private AutorizacaoDAO autorizacaoDAO;

    private ArrayList<Acesso> acessos;
    private ArrayList<HoraQuantidade> horasQuantidades;
    private ArrayList<UsuarioQuantidade> usuarioQuantidades;
    private ArrayList<Usuario> usuarios;

    private Long quantidadeDeAcesso;

    private LocalDate dataParaPesquisa;
    private LocalDate data1ParaPesquisa;
    private LocalDate data2ParaPesquisa;

    private LineChartModel grafico;
    private PieChartModel graficoPizza;

    private Long idUsuario;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        if (FacesUtils.getURL().contains("estatistica-usuario")) {
            listarUsuariosDoSistema();
            definirAsDatasDaSemana();
            listarAcessosDoPeriodo();
            contarOsAcessosParaCadaUsuario();
            criarGraficoPizza();
        } else {
            listarAcessosDoDia();
            criarAsHorasDoDia();
            contarOsAcessosParaCadaHora();
            criarGrafico();
        }
    }

    private void contarAcessosDoDia() {
        try {
            quantidadeDeAcesso = acessoDAO.contarAcessosPorData(LocalDate.now());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void listarAcessosDoDia() {
        try {
            acessos = null;
            acessos = new ArrayList<>();
            horasQuantidades = new ArrayList<>();
            acessos = (ArrayList<Acesso>) acessoDAO.listarAcessosPorData(dataParaPesquisa);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        dataParaPesquisa = LocalDate.now();
        idUsuario = 0l;
        usuarios = new ArrayList<>();
    }

    public String cancelar() {
        return "estatistica.xhtml" + "?faces-redirect=true";
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

    private void listarAcessosDoPeriodo() {
        try {
            acessos = null;
            acessos = new ArrayList<>();
            acessos = (ArrayList<Acesso>) acessoDAO.listarAcessosPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void listarAcessosDoPeriodoPorUsuario() {
        try {
            acessos = null;
            acessos = new ArrayList<>();
            if (idUsuario != 0l) {
                acessos = (ArrayList<Acesso>) acessoDAO.listarAcessosPorUsuarioEIntervaloDeDatas(new Usuario(idUsuario), data1ParaPesquisa, data2ParaPesquisa);
            }else{
                acessos = (ArrayList<Acesso>) acessoDAO.listarAcessosPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa);
            }
            
            contarOsAcessosParaCadaUsuario();
            criarGraficoPizza();
            
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //==========================================================================
    // MÉTODOS PARA CRIAR O GRÁFICO
    private void criarAsHorasDoDia() {
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(7, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(8, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(9, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(10, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(11, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(12, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(13, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(14, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(15, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(16, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(17, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(18, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(19, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(20, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(21, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(22, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(23, 0), 0l));
    }

    private void contarOsAcessosParaCadaHora() {
        for (HoraQuantidade hq : horasQuantidades) {
            for (Acesso a : acessos) {
                if (a.getMomentoDoAcesso().getHour() == hq.hora.getHour()) {
                    hq.setQuantidade(hq.getQuantidade() + 1);
                }
            }
        }

        ArrayList<HoraQuantidade> horasParaExcluir = new ArrayList<>();

        for (HoraQuantidade hq : horasQuantidades) {
            if (hq.getQuantidade() == 0) {
                horasParaExcluir.add(hq);
            }
        }

        horasQuantidades.removeAll(horasParaExcluir);
    }

    private LineChartModel popularGrafico() {
        LineChartModel model = new LineChartModel();
        ChartSeries acessos = new ChartSeries();
        acessos.setLabel("acessos");

        for (HoraQuantidade hq : horasQuantidades) {
            acessos.set(hq.getHora().toString(), hq.getQuantidade());
        }

        model.addSeries(acessos);
        return model;
    }

    private void criarGrafico() {
        grafico = popularGrafico();
        grafico.setTitle("Acessos do dia");
        grafico.setLegendPosition("e");
        grafico.setShowPointLabels(true);
        grafico.getAxes().put(AxisType.X, new CategoryAxis("Horas"));

        Axis yAxis = grafico.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade");
        yAxis.setMin(0);
        yAxis.setTickAngle(5);
        yAxis.setTickCount(5);
    }

    //--------------------------------------------------------------------------
    private void contarOsAcessosParaCadaUsuario() {
        try {
            usuarioQuantidades = null;
            usuarioQuantidades = new ArrayList<>();

            //Quando for escolhido um usuário em específico
            if (idUsuario != 0l) {
                for (Usuario u : usuarios) {
                    if (u.getId().equals(idUsuario)) {
                        usuarioQuantidades.add(new UsuarioQuantidade(u, acessoDAO.contarAcessosPorUsuarioEIntervaloDeDatas(u, data1ParaPesquisa, data2ParaPesquisa)));
                        break;
                    }
                }
            } //Quando não for escolhido um usuário em específico, ou seja, todos os usuário
            else {
                for (Usuario u : usuarios) {
                    usuarioQuantidades.add(new UsuarioQuantidade(u, acessoDAO.contarAcessosPorUsuarioEIntervaloDeDatas(u, data1ParaPesquisa, data2ParaPesquisa)));
                }

                ArrayList<UsuarioQuantidade> usuariosParaExcluir = new ArrayList<>();

                for (UsuarioQuantidade uq : usuarioQuantidades) {
                    if (uq.getQuantidade() == 0) {
                        usuariosParaExcluir.add(uq);
                    }
                }
                usuarioQuantidades.removeAll(usuariosParaExcluir);
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void criarGraficoPizza() {
        graficoPizza = new PieChartModel();

        for (UsuarioQuantidade uq : usuarioQuantidades) {
            graficoPizza.set(uq.usuario.getLogin(), uq.getQuantidade());
        }

        graficoPizza.setTitle("Quantidade de acessos por usuário no período");
        graficoPizza.setLegendPosition("e");
        graficoPizza.setFill(false);
        graficoPizza.setShowDataLabels(true);
        graficoPizza.setDiameter(150);
        graficoPizza.setShadow(false);
    }

    //==========================================================================
    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        acessoDAO = null;
        acessos = null;
        quantidadeDeAcesso = null;
        dataParaPesquisa = null;
        horasQuantidades = null;
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
    public ArrayList<Acesso> getAcessos() {
        return acessos;
    }

    public Long getQuantidadeDeAcesso() {
        return quantidadeDeAcesso;
    }

    public LocalDate getDataParaPesquisa() {
        return dataParaPesquisa;
    }

    public void setDataParaPesquisa(LocalDate dataParaPesquisa) {
        this.dataParaPesquisa = dataParaPesquisa;
    }

    public LineChartModel getGrafico() {
        return grafico;
    }

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

    public PieChartModel getGraficoPizza() {
        return graficoPizza;
    }

    //==========================================================================
    private class HoraQuantidade {

        public LocalTime hora;
        public Long quantidade;

        public HoraQuantidade() {
        }

        public HoraQuantidade(LocalTime hora, Long quantidade) {
            this.hora = hora;
            this.quantidade = quantidade;
        }

        public LocalTime getHora() {
            return hora;
        }

        public void setHora(LocalTime hora) {
            this.hora = hora;
        }

        public Long getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Long quantidade) {
            this.quantidade = quantidade;
        }
    }

    private class UsuarioQuantidade {

        private Usuario usuario;
        private Long quantidade;

        public UsuarioQuantidade() {
        }

        public UsuarioQuantidade(Usuario u, Long quantidade) {
            this.usuario = u;
            this.quantidade = quantidade;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public Long getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Long quantidade) {
            this.quantidade = quantidade;
        }
    }

}
